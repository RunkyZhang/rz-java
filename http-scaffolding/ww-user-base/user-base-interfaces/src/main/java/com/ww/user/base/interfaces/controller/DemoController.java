package com.ww.user.base.interfaces.controller;

import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.dto.SayHelloByNameRequestDto;
import com.ww.user.base.api.entity.UserEntity;
import com.ww.user.base.api.service.DemoService;
import com.ww.user.base.application.UserService;
import com.ww.user.base.infrastructure.ConfigSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class DemoController implements DemoService {
    @Resource
    private UserService userService;
    @Resource
    private ConfigSource configSource;

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    @RequestMapping("")
    @ResponseBody
    public String test(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + name + "===" + configSource.getServerAddress() + "---" + configSource.getUserName() + "---" + useLocalCache;
    }

    // http://127.0.0.1:8080/hello?name=lisi
    @RequestMapping("/hello")
    @ResponseBody
    public RpcResult<String> hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        UserEntity userEntity = userService.getByUserId(100000000);
        return RpcResult.success(name + ",hello!---" + userEntity.toString());
    }

    // curl --request POST \
    //  --url http://localhost:8080/sayHello \
    //  --header 'Content-Type: application/json' \
    //  --data '{
    //  "name": "houhou"
    //}
    //'
    @Override
    @PostMapping("/sayHello")
    public RpcResult<String> sayHelloByName(@RequestBody SayHelloByNameRequestDto requestDto) {
        Assert.notNull(requestDto, "Assert.notNull: requestDto");

        UserEntity userEntity = userService.getByUserId(100000000);
        return RpcResult.success(requestDto.getName() + "---" + userEntity.toString());
    }
}