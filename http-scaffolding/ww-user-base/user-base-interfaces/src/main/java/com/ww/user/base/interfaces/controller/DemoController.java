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

    // @RequestMapping 会自动生成post，get，delete等多个接口
    @GetMapping("getConfig")
    public RpcResult<String> test(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        name += "===" + configSource.getServerAddress() + "---" + configSource.getUserName() + "---" + useLocalCache;
        UserEntity userEntity = userService.getByUserId(100000000);

        return RpcResult.success(name + ",hello!---" + userEntity.toString());
    }

    // curl --request POST --url http://localhost:7070/sayHello --header 'Content-Type: application/json' --data '{"name": "houhou"}'
    // http的路径和method定义在接口中
    @Override
    public RpcResult<String> sayHelloByName(@RequestBody SayHelloByNameRequestDto requestDto) {
        Assert.notNull(requestDto, "Assert.notNull: requestDto");

        UserEntity userEntity = userService.getByUserId(100000000);
        return RpcResult.success(requestDto.getName() + "---" + userEntity.toString());
    }
}