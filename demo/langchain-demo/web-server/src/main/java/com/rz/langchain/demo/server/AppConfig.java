package com.rz.langchain.demo.server;

import com.rz.langchain.demo.server.agent.*;
import com.rz.langchain.demo.server.assistant.ChatAssistant;
import com.rz.langchain.demo.server.assistant.FormatAddressAssistant;
import com.rz.langchain.demo.server.assistant.LocalAssistant;
import com.rz.langchain.demo.server.dto.PlayCardsDto;
import com.rz.langchain.demo.server.filter.Filter4UserMessage;
import com.rz.langchain.demo.server.filter.FilterMapper;
import com.rz.langchain.demo.server.rag.BailianScoringModel;
import com.rz.langchain.demo.server.tools.ToolsSelector;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.agent.ErrorRecoveryResult;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.anthropic.AnthropicChatModel;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallzhv15q.BgeSmallZhV15QuantizedEmbeddingModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.moderation.ModerationModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiModerationModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.scoring.ScoringModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.aggregator.ReRankingContentAggregator;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.chroma.ChromaApiVersion;
import dev.langchain4j.store.embedding.chroma.ChromaEmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Slf4j
@Configuration
public class AppConfig {
    @Value("${ai.model.bailian.codeplan.apiKey}")
    public String bailianApiKey;
    @Value("${ai.model.deepseek.apiKey}")
    public String deepseekApiKey;
    @Value("${ai.model.opencode.go.apiKey}")
    public String openCodeApiKey;
    @Value("${ai.model.volcengine.codeplan.apiKey}")
    public String volcengineApiKey;
    @Value("${ai.rag.withInMemoryEmbeddingStore.switch:false}")
    private boolean withInMemoryEmbeddingStore;

    @Bean
    public SimpleLoopAgent simpleLoopAgent(AnshanPokerAgentA anshanPokerAgentA,
                                           AnshanPokerAgentB anshanPokerAgentB,
                                           @Qualifier("deepSeek_v4_pro") OpenAiChatModel model) {
        return AgenticServices
                .loopBuilder(SimpleLoopAgent.class)
                .subAgents(anshanPokerAgentA, anshanPokerAgentB)
                .maxIterations(50)
                .errorHandler((errorContext) -> {
                    log.error("errorContext: {}", errorContext);
                    return ErrorRecoveryResult.retry();
                })
                .beforeCall(o -> {
                    // 初始化参数，否者会报错
                    Object value = o.readState("remainingCards_A");
                    if (null == value) {
                        o.writeState("remainingCards_A", "");
                    }
                    value = o.readState("remainingCards_B");
                    if (null == value) {
                        o.writeState("remainingCards_B", "");
                    }
                    value = o.readState("playCards_A");
                    if (null == value) {
                        o.writeState("playCards_A", "");
                    }
                    value = o.readState("playCards_B");
                    if (null == value) {
                        o.writeState("playCards_B", "");
                    }
                    o.writeState("summary", "");

                    List<String> logs = new ArrayList<>();
                    o.writeState("logs", logs);
                })
                .exitCondition(o -> {
                    List<String> logs = (List<String>) o.readState("logs");
                    String state = (String) o.readState("state");
                    if ("发牌阶段".equals(state)) {
                        o.writeState("state", "打牌阶段");
                    }

                    // 有时候LLM会返回一个空字符串，如果不置空，会导致下一个LLM认为这个LLM又出了上一次出过的牌
                    // 所以这里置空，避免重复出牌，兜底逻辑认为这个LLM没有出牌
                    o.writeState("playCards_A", "");
                    o.writeState("playCards_B", "");

                    Object value = o.readState("playCardsInfo_A");
                    String playerStatusA = "";
                    String playCardsDtoJsonA = null == value ? "" : value.toString();
                    value = o.readState("playCardsInfo_B");
                    String playerStatusB = "";
                    String playCardsDtoJsonB = null == value ? "" : value.toString();

                    // 格式化LLM的返回参数。并设置到AgenticScope的状态中去对全部Agent共享数据
                    if (!StringUtils.isBlank(playCardsDtoJsonA)) {
                        PlayCardsDto playCardsDtoA;
                        try {
                            playCardsDtoA = JacksonHelper.toObj(playCardsDtoJsonA, PlayCardsDto.class, true);
                        } catch (Exception e) {
                            playCardsDtoA = new PlayCardsDto();
                            // TODO：当对手状态异常的时候。自己轮空不出牌，让对手在试一次
                            playCardsDtoA.setStatus("有牌-异常");
                            playCardsDtoA.setSubSummary("脑子出问题了，返回的Json格式不对。哎！这点小事都做不好？？！！");
                        }
                        String description = "大师A：";
                        // 游戏状态
                        if (!StringUtils.isBlank(playCardsDtoA.getStatus())) {
                            o.writeState("gameStatus_A", playCardsDtoA.getStatus());
                            playerStatusA = playCardsDtoA.getStatus();

                            description += "；状态：" + playCardsDtoA.getStatus();
                        }
                        // 出牌list
                        if (!CollectionUtils.isEmpty(playCardsDtoA.getPlayCards())) {
                            String playCardsJoinA = String.join(",", playCardsDtoA.getPlayCards());
                            o.writeState("playCards_A", playCardsJoinA);
                            logs.add("大师A: " + playCardsJoinA);

                            description += "；出牌：" + playCardsJoinA;
                        } else {
                            description += "；不出牌：[]";
                        }
                        // 剩余牌list
                        if (!CollectionUtils.isEmpty(playCardsDtoA.getRemainingCards())) {
                            o.writeState("remainingCards_A", String.join(",", playCardsDtoA.getRemainingCards()));
                        }
                        // 闲话
                        if (!StringUtils.isBlank(playCardsDtoA.getSubSummary())) {
                            description += "；小结：" + playCardsDtoA.getSubSummary();
                        }

                        o.writeState("playCardsInfo_A", "");
                        log.warn(description);
                    } else if (!StringUtils.isBlank(playCardsDtoJsonB)) {
                        PlayCardsDto playCardsDtoB = null;
                        try {
                            playCardsDtoB = JacksonHelper.toObj(playCardsDtoJsonB, PlayCardsDto.class, true);
                        } catch (Exception e) {
                            playCardsDtoB = new PlayCardsDto();
                            // TODO：当对手状态异常的时候。自己轮空不出牌，让对手在试一次
                            playCardsDtoB.setStatus("有牌-异常");
                            playCardsDtoB.setSubSummary("脑子出问题了，返回的Json格式不对。哎！这点小事都做不好？？！！");
                        }
                        String description = "大师B：";
                        // 游戏状态
                        if (!StringUtils.isBlank(playCardsDtoB.getStatus())) {
                            o.writeState("gameStatus_B", playCardsDtoB.getStatus());
                            playerStatusB = playCardsDtoB.getStatus();

                            description += "；状态：" + playCardsDtoB.getStatus();
                        }
                        // 出牌list
                        if (!CollectionUtils.isEmpty(playCardsDtoB.getPlayCards())) {
                            String playCardsJoinB = String.join(",", playCardsDtoB.getPlayCards());
                            o.writeState("playCards_B", playCardsJoinB);
                            logs.add("大师B: " + playCardsJoinB);

                            description += "；出牌：" + playCardsJoinB;
                        } else {
                            description += "；不出牌：[]";
                        }
                        // 剩余牌list
                        if (!CollectionUtils.isEmpty(playCardsDtoB.getRemainingCards())) {
                            o.writeState("remainingCards_B", String.join(",", playCardsDtoB.getRemainingCards()));
                        }
                        // 闲话
                        if (!StringUtils.isBlank(playCardsDtoB.getSubSummary())) {
                            description += "；小结：" + playCardsDtoB.getSubSummary();
                        }

                        o.writeState("playCardsInfo_B", "");
                        log.warn(description);
                    } else {
                        log.error("哪个LLM又抽风！只返回thinking，不返回text。当前局面是：{}", logs);
                    }

                    boolean result = "无牌-胜利".equals(playerStatusA);
                    result = "无牌-胜利".equals(playerStatusB);

                    if (result) {
                        String summary = model.chat(String.format("请根据以下牌局的日志进行尽量详细总结，样式美观：%s", String.join("\n", logs)));
                        o.writeState("summary", summary);
                    }

                    return result;
                })
//                .output(o -> {
//                    // 不生效！！！？？？
//                })
                .outputName("summary")
                .build();
    }

    @Bean()
    public AnshanPokerAgentA anshanPokerAgentA(@Qualifier("deepSeek_v4_pro") OpenAiChatModel model) {
        return AgenticServices.agentBuilder(AnshanPokerAgentA.class)
                .chatModel(model)
                // .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(50))
                .outputName("playCardsInfo_A")
                .build();
    }

    @Bean()
    public AnshanPokerAgentB anshanPokerAgentB(@Qualifier("volcengine_doubao_seed_2.0_pro") OpenAiChatModel model) {
        return AgenticServices.agentBuilder(AnshanPokerAgentB.class)
                .chatModel(model)
                // .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(50))
                .outputName("playCardsInfo_B")
                .build();
    }

    @Bean
    public SimpleSequenceAgent simpleSequenceAgent(FarthestDistanceReadAgent farthestDistanceReadAgent,
                                                   TableFormatAgent tableFormatAgent) {
        return AgenticServices
                .sequenceBuilder(SimpleSequenceAgent.class)
                .subAgents(farthestDistanceReadAgent, tableFormatAgent)
                .outputName("message")
                .build();
    }

    @Bean
    public FarthestDistanceReadAgent farthestDistanceReadAgent(@Qualifier("volcengine_glm-5.2") OpenAiChatModel model,
                                                               RetrievalAugmentor retrievalAugmentor) {
        return AgenticServices.agentBuilder(FarthestDistanceReadAgent.class)
                .chatModel(model)
                .retrievalAugmentor(retrievalAugmentor)
                .outputName("message")
                .build();
    }

    @Bean
    public TableFormatAgent tableFormatAgent(@Qualifier("deepSeek_v4_flash") AnthropicChatModel model) {
        return AgenticServices.agentBuilder(TableFormatAgent.class)
                .chatModel(model)
                .outputName("message")
                .build();
    }

    // 工具不生效，不知道是不是需要升级版本
    @Bean
    public ToolsAgent toolsAgent(@Qualifier("deepSeek_v4_flash") AnthropicChatModel model,
                                 ToolsSelector toolsSelector) {
        return AgenticServices.agentBuilder(ToolsAgent.class)
                .chatModel(model)
                .tools(toolsSelector.getExecutors()) // 没生效 TODO：
                .outputName("message")
                .build();
    }

    @Bean
    public ChatAssistant chatMasterAssistant(@Qualifier("opencode_go_qwen3.7_max") AnthropicChatModel model,
                                             @Qualifier("chatMemory-default") ChatMemory chatMemory,
                                             ChatMemoryProvider chatMemoryProvider,
                                             ToolsSelector toolsSelector,
                                             BiFunction<ChatRequest, Object, ChatRequest> chatRequestTransformer,
                                             ModerationModel moderationModel,
                                             EmbeddingStoreContentRetriever embeddingStoreContentRetriever,
                                             RetrievalAugmentor retrievalAugmentor) {
        return AiServices.builder(ChatAssistant.class)
                .chatModel(model)
                .tools(toolsSelector.getExecutors())
                .chatMemory(chatMemory)
                .chatMemoryProvider(chatMemoryProvider)
                .chatRequestTransformer(chatRequestTransformer)
                .moderationModel(moderationModel) // 貌似没生效
                //.contentRetriever(embeddingStoreContentRetriever) // contentRetriever 和 retrievalAugmentor只能用一个
                .retrievalAugmentor(retrievalAugmentor)
                .build();
    }

    @Bean
    public FormatAddressAssistant formatAddressAssistant(@Qualifier("opencode_go_qwen3.7_max") AnthropicChatModel model) {
        return AiServices.create(FormatAddressAssistant.class, model);
    }

    @Bean
    public LocalAssistant localAssistant(@Qualifier("deepSeek_v4_flash") AnthropicChatModel model) {
        return AiServices.create(LocalAssistant.class, model);
    }

    // http debug：SyncRequestExecutor
    @Bean("qwen_3_5_plus")
    public OpenAiChatModel openAiChatModel_Qwen_3_5_plus() {
        return OpenAiChatModel.builder()
                .baseUrl("https://coding.dashscope.aliyuncs.com/v1")
                .apiKey(bailianApiKey)
                .modelName("qwen3.5-plus")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .strictJsonSchema(true)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public StreamingChatModel streamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl("https://coding.dashscope.aliyuncs.com/v1")
                .apiKey(bailianApiKey)
                .modelName("qwen3.5-plus")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public ScoringModel scoringModel() {
        return new BailianScoringModel("qwen3-rerank", 5, null);
    }

    @Bean("deepSeek_v4_pro")
    public OpenAiChatModel openAiChatModel_DeepSeek_v4_pro() {
        return OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com")
                .apiKey(deepseekApiKey)
                .modelName("deepseek-v4-pro")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .strictJsonSchema(true)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean("deepSeek_v4_flash")
    public AnthropicChatModel anthropicChatModel_DeepSeek_v4_flash() {
        return AnthropicChatModel.builder()
                .baseUrl("https://api.deepseek.com/anthropic")
                .apiKey(deepseekApiKey)
                .modelName("deepseek-v4-flash")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public ModerationModel moderationModel() {
        return OpenAiModerationModel.builder()
                .baseUrl("https://api.deepseek.com")
                .apiKey(deepseekApiKey)
                .modelName("deepseek-v4-flash")
                .timeout(Duration.ofSeconds(300))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean("opencode_go_qwen3.7_max")
    public AnthropicChatModel anthropicChatModel_OpenCode_Go_Qwen_3_7_Max() {
        return AnthropicChatModel.builder()
                .baseUrl("https://opencode.ai/zen/go/v1")
                .apiKey(openCodeApiKey)
                .modelName("qwen3.7-max")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean("opencode_go_glm_5.1")
    public OpenAiChatModel openAiChatModel_opencode_go_Qlm_5_1() {
        return OpenAiChatModel.builder()
                .baseUrl("https://opencode.ai/zen/go/v1")
                .apiKey(openCodeApiKey)
                .modelName("glm-5.1")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .strictJsonSchema(true)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean("opencode_go_glm_5.2")
    public OpenAiChatModel openAiChatModel_opencode_go_Qlm_5_2() {
        return OpenAiChatModel.builder()
                .baseUrl("https://opencode.ai/zen/go/v1")
                .apiKey(openCodeApiKey)
                .modelName("glm-5.2")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .strictJsonSchema(true)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    // 兼容 Anthropic 接口协议工具：
    // https://ark.cn-beijing.volces.com/api/coding
    // 兼容 OpenAI 接口协议工具：
    // https://ark.cn-beijing.volces.com/api/coding/v3
    @Bean("volcengine_doubao_seed_2.0_pro")
    public OpenAiChatModel openAiChatModel_volcengine_doubao_seed_2_0_pro() {
        return OpenAiChatModel.builder()
                .baseUrl("https://ark.cn-beijing.volces.com/api/coding/v3")
                .apiKey(volcengineApiKey)
                .modelName("doubao-seed-2.0-pro")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .strictJsonSchema(true)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean("volcengine_glm-5.2")
    public OpenAiChatModel openAiChatModel() {
        return OpenAiChatModel.builder()
                .baseUrl("https://ark.cn-beijing.volces.com/api/coding/v3")
                .apiKey(volcengineApiKey)
                .modelName("glm-5.2")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .strictJsonSchema(true)
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean("chatMemory-1111")
    public ChatMemory chatMemory1111() {
        // memoryId: 一般被设计为sessionId
        return MessageWindowChatMemory.builder()
                .id(1111)
                .maxMessages(10)
                .chatMemoryStore(new InMemoryChatMemoryStore())
                .build();
    }

    @Bean("chatMemory-default")
    public ChatMemory chatMemoryDefault() {
        // memoryId: 一般被设计为sessionId
        return MessageWindowChatMemory.builder()
                .id("runky-default")
                .maxMessages(25)
                .chatMemoryStore(new InMemoryChatMemoryStore())
                .build();
    }

    @Bean
    public EmbeddingStoreIngestor embeddingStoreIngestor(InMemoryEmbeddingStore<TextSegment> inMemoryEmbeddingStore,
                                                         ChromaEmbeddingStore chromaEmbeddingStore,
                                                         EmbeddingModel embeddingModel) {
        // TokenCountEstimator：token用量估算器
        // recursive 方法实际上是一个高层封装，它将 DocumentByParagraphSplitter、DocumentByLineSplitter、DocumentBySentenceSplitter、DocumentByWordSplitter、DocumentByCharacterSplitter 串联成一个责任链，并自动配置好各自的 subSplitter。开发者无需手动构建分层结构，直接调用此方法即可获得最佳实践的分割器。
        // DocumentSplitters.recursive(...)
        // 按段落边界分割文档，保持段落的完整性。适用场景：适合结构化较好的文档，如文章、报告等。
        // DocumentByParagraphSplitter();
        // 按换行符分割文档，每行作为一个单元。。适用场景：适合代码文件、CSV 文件、日志文件等按行组织的内容。
        // DocumentByLineSplitter();
        // 按句子边界（句号、问号、感叹号等）分割文档。适用场景：需要保持句子完整性的场景，如问答系统、语义搜索。
        // DocumentBySentenceSplitter;
        // 按单词边界分割文档。适用场景：需要精细控制片段大小的场景，但可能破坏语义完整性。
        // DocumentByWordSplitter;
        // 最简单的分割方式，直接按字符数切割。适用场景：简单快速的分割，但可能在单词或句子中间切断
        // DocumentByCharacterSplitter;
        // 使用自定义正则表达式作为分隔符进行分割。适用场景：需要自定义分割规则的場景，如按章节、按特定标记分割。举例："(?m)^#\\s+"是按 Markdown 一级标题分割
        // DocumentByRegexSplitter;

        // TODO，注意：貌似maxOverlapSizeInChars不生效。下面是验证代码
//        String longString = "a".repeat(100);
//        longString += "b".repeat(100);
//        Document doc = Document.from(longString);
//        DocumentSplitter splitter = DocumentSplitters.recursive(100, 50);
//        List<TextSegment> segments = splitter.split(doc);
//        for(TextSegment seg : segments) {
//            System.out.println(seg.text().length() + " : " + seg.text().substring(0, Math.min(20, seg.text().length())));
//        }

        EmbeddingStore<TextSegment> embeddingStore = withInMemoryEmbeddingStore ? inMemoryEmbeddingStore : chromaEmbeddingStore;

        return EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(500, 100))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
    }

    @Bean
    public InMemoryEmbeddingStore<TextSegment> inMemoryEmbeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    // 本地安装
    // 构建虚拟环境（创建一个文件夹）：python3 -m venv chroma
    // 激活虚拟环境（只在当前shell生效，不影响全局变量，下次使用还要在/chroma平级目录执行激活）：source chroma/bin/activate
    // 安装chroma（可能python3需要升级python3 -m pip install --upgrade pip）：pip install chromadb
    // 启动服务（只监听ipv6）：chroma run --path ./data --port 8000
    @Bean
    public ChromaEmbeddingStore chromaEmbeddingStore() {
        // 默认使用 ChromaApiVersion.V2
        // TODO注意：Chroma服务启动默认只监听ipv6。所以123.0.0.1:8000访问不了。localhost:8000也访问不了因为Java组件中默认使用ipv4导致localhost解析成123.0.0.1。
        //          所以自能使用ipv6的本地地址[::1]:8000
        return ChromaEmbeddingStore.builder()
                .apiVersion(ChromaApiVersion.V2)
                .baseUrl("http://[::1]:8000")
                .tenantName("default") // 如果不传默认租户名，则默认使用default。如果没有则创建default租户
                .databaseName("default") // 如果不传默认数据库名，则默认使用default。如果没有则创建default数据库
                .collectionName("default")
                .timeout(Duration.ofSeconds(5))
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return new BgeSmallZhV15QuantizedEmbeddingModel();
    }

    @Bean
    public EmbeddingStoreContentRetriever embeddingStoreContentRetriever(
            InMemoryEmbeddingStore<TextSegment> inMemoryEmbeddingStore,
            ChromaEmbeddingStore chromaEmbeddingStore,
            EmbeddingModel embeddingModel,
            FilterMapper filterMapper) {

        EmbeddingStore<TextSegment> embeddingStore = withInMemoryEmbeddingStore ? inMemoryEmbeddingStore : chromaEmbeddingStore;

        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(10)
                .minScore(0.5)
                .displayName("chroma")
                .dynamicMaxResults(query -> {
                    System.out.println(query);
                    return 10;
                })
                .dynamicMinScore(query -> {
                    System.out.println(query);
                    return 0.5;
                }).dynamicFilter(query -> {
                    if (!(query.metadata().chatMessage() instanceof UserMessage userMessage)) {
                        return null;
                    }
                    if (StringUtils.isBlank(userMessage.name())) {
                        return null;
                    }
                    Filter4UserMessage filter4UserMessage = filterMapper.get(userMessage.name());
                    if (null == filter4UserMessage) {
                        return null;
                    }

                    filter4UserMessage.setUsed(true);
                    return filter4UserMessage.getFilter();
                })
                .build();
    }

    @Bean
    public ContentAggregator contentAggregator(ScoringModel scoringModel) {
        return ReRankingContentAggregator.builder()
                .scoringModel(scoringModel)
                // .querySelector() TODO: 待了解
                .minScore(0.5)
                .build();
    }

    // 默认内容注入器（Default Content Injector）
    // DefaultContentInjector 是 ContentInjector 的默认实现，它只是简单地修改text
    // {{userMessage}}：是用户消息
    // {{contents}}：是RAG内容（并带有前缀 Answer using the following information: ）
    // 如果删除{{userMessage}}和{{contents}}都会缺失对应内容
    @Bean
    public ContentInjector contentInjector() {
        return DefaultContentInjector.builder()
                .promptTemplate(PromptTemplate.from("{{userMessage}}\n{{contents}}\n你的回答要用以下文字结尾(注意换行)：吼吼\uD83D\uDC81\uD83D\uDC81\uD83D\uDC81"))
                .build();
    }

    @Bean
    public RetrievalAugmentor retrievalAugmentor(EmbeddingStoreContentRetriever embeddingStoreContentRetriever,
                                                 ContentAggregator contentAggregator,
                                                 ContentInjector contentInjector) {
        return DefaultRetrievalAugmentor.builder()
                .contentRetriever(embeddingStoreContentRetriever) // retrieve
                .contentAggregator(contentAggregator) // rerank
                //.queryTransformer() 用来重新包装Query对象
                //.queryRouter() 用来塞选多个contentRetriever中的哪几个contentRetriever。默认使用DefaultQueryRouter返回全部contentRetriever
                .contentInjector(contentInjector)
                .build();
    }

    @Bean
    public BiFunction<ChatRequest, Object, ChatRequest> chatRequestTransformer() {
        return (chatRequest, memoryId) -> {
            System.out.println(memoryId);

            List<ChatMessage> chatMessages = new ArrayList<>(chatRequest.messages());
            UserMessage userMessage = UserMessage.from("你的回答要用以下文字开头(注意换行)：吼吼\uD83D\uDE47\uD83D\uDE47\uD83D\uDE47\n");
            chatMessages.add(userMessage);

            ChatRequest.Builder chatRequestBuilder = cloneChatRequest(chatRequest);
            chatRequestBuilder.messages(chatMessages);

            return chatRequestBuilder.build();
        };
    }

    // 可动态new一个ChatMemory，可指定哪个ChatMemory，也默认使用chatMemory-default
    @Bean
    public ChatMemoryProvider chatMemoryProvider(@Qualifier("chatMemory-1111") ChatMemory chatMemory1111,
                                                 @Qualifier("chatMemory-default") ChatMemory chatMemoryDefault) {
        return memoryId -> {
            if (chatMemory1111.id().equals(memoryId)) {
                return chatMemory1111;
            } else {
                return chatMemoryDefault;
            }
        };
    }

    private ChatRequest.Builder cloneChatRequest(ChatRequest chatRequest) {
        if (null == chatRequest) {
            return null;
        }

        return ChatRequest.builder()
                .messages(chatRequest.messages())
                .modelName(chatRequest.modelName())
                .temperature(chatRequest.temperature())
                .topP(chatRequest.topP())
                .topK(chatRequest.topK())
                .frequencyPenalty(chatRequest.frequencyPenalty())
                .presencePenalty(chatRequest.presencePenalty())
                .maxOutputTokens(chatRequest.maxOutputTokens())
                .stopSequences(chatRequest.stopSequences())
                .toolSpecifications(chatRequest.toolSpecifications())
                .toolChoice(chatRequest.toolChoice())
                .responseFormat(chatRequest.responseFormat());
    }
}
