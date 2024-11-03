package com.ww.video.base.interfaces.controller;

import com.ww.common.base.dto.RpcResult;
import com.ww.video.base.api.dto.SayHelloByNameRequestDto;
import com.ww.video.base.api.service.SomeService;
import com.ww.video.base.application.VideoService;
import com.ww.video.base.infrastructure.ConfigSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class SomeController implements SomeService {
    @Resource
    private VideoService userService;
    @Resource
    private ConfigSource configSource;

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @RequestMapping("")
    @ResponseBody
    public String test(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        name += userService.getByVideoId(1000);
        return name + "===" + configSource.getServerAddress() + "---" + configSource.getUserName() + "---" + useLocalCache;
    }

    @Override
    @PostMapping("/sayHello")
    public RpcResult<String> sayHelloByName(SayHelloByNameRequestDto requestDto) {
        Assert.notNull(requestDto, "Assert.notNull: requestDto");

        String value = userService.getByVideoId(1000);
        return RpcResult.success(requestDto.getName() + ",hello!---" + value);
    }
}
