package com.rz.langchain.demo.server.assistant;

import com.rz.langchain.demo.server.dto.AddressDto;
import dev.langchain4j.service.SystemMessage;

public interface FormatAddressAssistant {
    @SystemMessage("""
            你是一个AI地址格式化Agent，最擅长的是把一段包含省市区，姓名，电话等信息的字符串格式化成指定json字符串
            """)
    AddressDto format(String address);
}
