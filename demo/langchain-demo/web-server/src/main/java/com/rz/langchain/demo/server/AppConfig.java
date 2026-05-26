package com.rz.langchain.demo.server;

import com.rz.langchain.demo.server.rag.BailianScoringModel;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.bgesmallzhv15q.BgeSmallZhV15QuantizedEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.scoring.ScoringModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import dev.langchain4j.store.memory.chat.InMemoryChatMemoryStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Value("${ai.model.bailian.codeplan.apiKey}")
    public String bailianApiKey;

    @Value("${ai.model.deepseek.apiKey}")
    public String deepseekApiKey;

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

    @Bean("glm_5")
    public OpenAiChatModel openAiChatModel_Qlm_5() {
        return OpenAiChatModel.builder()
                .baseUrl("https://coding.dashscope.aliyuncs.com/v1")
                .apiKey(bailianApiKey)
                .modelName("glm-5")
                .returnThinking(true)
                .timeout(Duration.ofSeconds(300))
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .strictJsonSchema(true)
                .logRequests(true)
                .logResponses(true)
                .build();
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
    public OpenAiChatModel openAiChatModel_DeepSeek_v4_flash() {
        return OpenAiChatModel.builder()
                .baseUrl("https://api.deepseek.com")
                .apiKey(deepseekApiKey)
                .modelName("deepseek-v4-flash")
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
    public ChatMemory ChatMemory() {
        // memoryId: 一般被设计为sessionId
        return MessageWindowChatMemory.builder()
                .id(1111)
                .maxMessages(50)
                .chatMemoryStore(new InMemoryChatMemoryStore())
                .build();
    }

    @Bean
    public EmbeddingStoreIngestor embeddingStoreIngestor(InMemoryEmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
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

        return EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(500, 100))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
    }

    @Bean
    public InMemoryEmbeddingStore<TextSegment> embeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return new BgeSmallZhV15QuantizedEmbeddingModel();
    }

    @Bean
    public ScoringModel scoringModel() {
        return new BailianScoringModel("qwen3-rerank", 5, null);

//        return XinferenceScoringModel.builder()
//                .baseUrl("https://dashscope.aliyuncs.com/compatible-api/")
//                .apiKey("sk-e5f93a0caa364d548214a24101642a64")
//                .modelName("qwen3-rerank")
//                .timeout(Duration.ofSeconds(60))
//                .maxRetries(1)
//                .logRequests(true)
//                .logResponses(true)
//                .build();
    }

    //    ScoringModel model = XinferenceScoringModel.builder()
    //            .baseUrl(baseUrl())
    //            .apiKey(apiKey())
    //            .modelName(modelName())
    //            .timeout(Duration.ofSeconds(60))
    //            .maxRetries(1)
    //            .logRequests(true)
    //            .logResponses(true)
    //            .build();
    //
    //    @Test
    //    void should_score_single_text() {
    //        String text =
    //                "北京市（Beijing），简称“京”，古称燕京、北平，是中华人民共和国首都、直辖市、国家中心城市、超大城市， [185]国务院批复确定的中国政治中心、文化中心、国际交往中心、科技创新中心， [1]中国历史文化名城和古都之一，世界一线城市。 [3] [142] [188]截至2023年10月，北京市下辖16个区，总面积16410.54平方千米。 [82] [193] [195]2023年末，北京市常住人口2185.8万人。 [214-215]";
    //        String query = "中国首都是哪座城市";
    //        Response<Double> response = model.score(text, query);
    //        assertThat(response.content()).isCloseTo(0.661, withPercentage(3));
    //        TokenUsage tokenUsage = response.tokenUsage();
    //        assertThat(tokenUsage.totalTokenCount())
    //                .isEqualTo(tokenUsage.inputTokenCount() + tokenUsage.outputTokenCount());
    //        assertThat(response.finishReason()).isNull();
    //    }
    //
    //    @Test
    //    void should_score_multiple_segments_with_all_parameters() {
    //        TextSegment segment1 = TextSegment.from(
    //                "上海市（Shanghai），简称“沪”，别名“申”，是中华人民共和国直辖市， [38]位于中国东部，地处长江入海口， [175]境域北界长江，东濒东海，南临杭州湾，西接江苏省和浙江省，总面积6340.5平方千米， [38]下辖16个区。 [37]截至2022年末，全市常住人口2475.89万人， [204]上海话属吴语方言太湖片。 [159]市政府驻地上海市黄浦区人民大道200号。 [173]");
    //        TextSegment segment2 = TextSegment.from(
    //                "北京市（Beijing），简称“京”，古称燕京、北平，是中华人民共和国首都、直辖市、国家中心城市、超大城市， [185]国务院批复确定的中国政治中心、文化中心、国际交往中心、科技创新中心， [1]中国历史文化名城和古都之一，世界一线城市。 [3] [142] [188]截至2023年10月，北京市下辖16个区，总面积16410.54平方千米。 [82] [193] [195]2023年末，北京市常住人口2185.8万人。 [214-215]");
    //        List<TextSegment> segments = asList(segment1, segment2);
    //        String query = "中国首都是哪座城市";
    //        Response<List<Double>> response = model.scoreAll(segments, query);
    //        List<Double> scores = response.content();
    //        assertThat(scores).hasSize(2);
    //        assertThat(scores.get(0)).isLessThan(scores.get(1));
    //        TokenUsage tokenUsage = response.tokenUsage();
    //        assertThat(tokenUsage.totalTokenCount())
    //                .isEqualTo(tokenUsage.inputTokenCount() + tokenUsage.outputTokenCount());
    //        assertThat(response.finishReason()).isNull();
    //    }
}
