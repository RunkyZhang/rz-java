package com.rz.web.demo.server.tool;

import com.rz.web.demo.server.JacksonHelper;
import com.rz.web.demo.server.dto.ContactDto;
import com.rz.web.demo.server.schema.EnumArgument;
import com.rz.web.demo.server.schema.InputSchema;
import com.rz.web.demo.server.schema.StringArgument;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class ListContactMcpTool implements McpTool {
    @Override
    public String getName() {
        return "listContact";
    }

    @Override
    public String getDescription() {
        return "简介：runky的列举联系人信息；" +
                "功能：查询全部联系人的姓名和工号信息，无需任何参数；" +
                "Contact类：name为联系人姓名，code为联系人工号;"+
                "返回值：Contact类型集合的json字符串。";
    }

    @Override
    public String getInputSchema() {
        return new InputSchema()
                .withId(this.getName())
                .build();
    }

    @Override
    public McpSchema.CallToolResult callback(McpSyncServerExchange exchange, Map<String, Object> arguments) {
        log.info("arguments: {}", arguments);

        List<ContactDto> contactDtos = new ArrayList<>();
        ContactDto contactDto = new ContactDto();
        contactDto.setName("一鸣");
        contactDto.setCode("00514152");
        contactDtos.add(contactDto);
        contactDto = new ContactDto();
        contactDto.setName("仁杰");
        contactDto.setCode("00545579");
        contactDtos.add(contactDto);

        // 工具实现
        McpSchema.TextContent textContent = new McpSchema.TextContent(
                Collections.singletonList(McpSchema.Role.USER),
                1.0,
                JacksonHelper.toJson(contactDtos, true)
        );
        return new McpSchema.CallToolResult(Collections.singletonList(textContent), false);
    }
}
