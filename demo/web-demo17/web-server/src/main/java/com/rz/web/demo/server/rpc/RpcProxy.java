package com.rz.web.demo.server.rpc;

import com.rz.web.demo.server.dto.WcImResultDto;
import com.rz.web.demo.server.dto.WcImSendRequestDto;
import feign.Request;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@Service
public class RpcProxy {
    private final Map<String, Request.Options> options = new HashMap<>();

    @Resource
    private WeComImFeignClient weComImFeignClient;

    public void wcImWebhookSend(WcImSendRequestDto wcImSendRequestDto) {
        Assert.notNull(wcImSendRequestDto, "Assert.notNull: wcImSendRequestDto");

        WcImResultDto result = weComImFeignClient.webhookSend(wcImSendRequestDto, wcImSendRequestDto.getRobotKey());
        if (null == result) {
            throw new RuntimeException("-1; result is null");
        }
        if (result.getErrcode() != WcImResultDto.SUCCESS_CODE) {
            throw new RuntimeException(result.getErrcode() + "; " + result.getErrmsg());
        }
    }
}
