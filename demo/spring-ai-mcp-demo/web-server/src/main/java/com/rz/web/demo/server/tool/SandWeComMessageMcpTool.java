package com.rz.web.demo.server.tool;

import com.rz.web.demo.server.dto.WcImSendRequestDto;
import com.rz.web.demo.server.dto.WcImSendTextContentDto;
import com.rz.web.demo.server.rpc.RpcProxy;
import com.rz.web.demo.server.schema.InputSchema;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
public class SandWeComMessageMcpTool implements McpTool {
    @Resource
    private RpcProxy rpcProxy;

    @Override
    public String getName() {
        return "sandWeComMessage";
    }

    @Override
    public String getDescription() {
        return "简介：runky发送消息工具；" +
                "功能：通过工号给对应的人发送企业微信消息；" +
                "参数说明：code为工号。确认消息to给谁；" +
                "参数说明：message为消息内容.";
    }

    @Override
    public String getInputSchema() {
        return new InputSchema()
                .withId(this.getName())
                .withString("code", true)
                .withString("message", true)
                .build();
    }


    @Override
    public McpSchema.CallToolResult callback(McpSyncServerExchange exchange, Map<String, Object> arguments) {
        log.info("arguments: {}", arguments);

        String code = (String) arguments.get("code");
        String message = (String) arguments.get("message");

        WcImSendRequestDto wcImSendRequestDto = new WcImSendRequestDto();
        wcImSendRequestDto.setRobotKey("02ed44a0-025f-4821-a10e-31d975e30c44");
        wcImSendRequestDto.setMsgtype("text");
        WcImSendTextContentDto wcImSendTextContentDto = new WcImSendTextContentDto();
        wcImSendTextContentDto.setContent(message);
        wcImSendTextContentDto.setMentioned_list(Collections.singletonList(code));
        wcImSendRequestDto.setText(wcImSendTextContentDto);
        rpcProxy.wcImWebhookSend(wcImSendRequestDto);

        // 工具实现
        McpSchema.TextContent textContent = new McpSchema.TextContent(
                Collections.singletonList(McpSchema.Role.USER),
                1.0,
                "done"
        );
        return new McpSchema.CallToolResult(Collections.singletonList(textContent), false);
    }
}
