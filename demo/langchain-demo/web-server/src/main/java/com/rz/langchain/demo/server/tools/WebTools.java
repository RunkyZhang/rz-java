package com.rz.langchain.demo.server.tools;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.McpTransport;
import dev.langchain4j.mcp.client.transport.stdio.StdioMcpTransport;

import java.util.Map;

public class WebTools {
//    @Tool("Search the web using Google Search")
//    public String search(@P("The search query") String query) {
//        McpTransport transport = HttpMcpTransport.
//                .endpoint("https://mcp.langchain4j.dev")
//                .build();
//
//        McpClient mcpClient = new DefaultMcpClient.Builder()
//                .key("MyMCPClient")
//                .transport(transport)
//                .build();
//
//        String result = mcpClient.getPrompt("web-search", Map.of("query", query));
//
//        return result;
//    }


//    @Tool("Search the web using Google Search")
//    public String search(@P("The search query") String query) {
//        System.out.println("\n" + GREEN + "⏺ WebSearch" + RESET + "(" + DIM + query + RESET + ")");
//        var searchModel = GoogleAiGeminiChatModel.builder()
//                .apiKey(GEMINI_KEY)
//                .modelName(MODEL_NAME)
//                .allowGoogleSearch(true)
//                .build();
//        return searchModel.chat(query);
//    }
//
//    @Tool("Fetch the content of a specific URL")
//    public String fetch(@P("The URL to fetch") String url) {
//        System.out.println("\n" + GREEN + "⏺ WebFetch" + RESET + "(" + DIM + url + RESET + ")");
//        var fetchModel = GoogleAiGeminiChatModel.builder()
//                .apiKey(GEMINI_KEY)
//                .modelName(MODEL_NAME)
//                .allowUrlContext(true)
//                .build();
//        return fetchModel.chat("Please extract and summarize the content of this URL: " + url);
//    }
}
