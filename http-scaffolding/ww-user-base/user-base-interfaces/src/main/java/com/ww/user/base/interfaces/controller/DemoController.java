package com.ww.user.base.interfaces.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.dto.SayHelloByNameRequestDto;
import com.ww.user.base.api.entity.UserEntity;
import com.ww.user.base.api.service.DemoService;
import com.ww.user.base.application.UserService;
import com.ww.user.base.infrastructure.ConfigSource;
import com.ww.user.base.infrastructure.rpc.RpcProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class DemoController implements DemoService {
    @Resource
    private UserService userService;
    @Resource
    private ConfigSource configSource;
    @Resource
    private RpcProxy rpcProxy;

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;

    // @RequestMapping 会自动生成post，get，delete等多个接口
    @SentinelResource(value = "DemoController.getConfig", blockHandler = "getConfigLimited", blockHandlerClass = ApiFlowLimiting.class)
    @GetMapping("/getConfig")
    public RpcResult<String> getConfig(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        name += "：" + configSource.getServerAddress() + "---" + configSource.getUserName() + "---" + useLocalCache;
        List<Long> userIds = new ArrayList<>();
        userIds.add(100000001L);

        List<UserEntity> userEntities = new ArrayList<>();
        UserEntity userEntity = userService.getByUserId(userIds.get(0));
        userEntities.add(userEntity);
        userEntities.addAll(userService.getByPhoneNo(userEntity.getPhoneNo()));
        Map<Long, UserEntity> userEntitiesMap = userService.getByUserIds(new HashSet<>(userIds));
        userEntities.addAll(userEntitiesMap.values());

        if (0 == System.currentTimeMillis() % 2) {
            throw new RuntimeException("测试异常情况，几率为50%。");
        }

        return RpcResult.success(name + "---hello!---" + userEntities);
    }

    @GetMapping("/testInvokeFlowBreaking")
    public RpcResult<String> testInvokeFlowBreaking() {
        String name = rpcProxy.getName(System.currentTimeMillis() + "");

        return RpcResult.success(name);
    }

    // curl --request POST --url http://localhost:7070/sayHello --header 'Content-Type: application/json' --data '{"name": "houhou"}'
    // http的路径和method定义在接口中
    @Override
    public RpcResult<String> sayHelloByName(@RequestBody SayHelloByNameRequestDto requestDto) {
        Assert.notNull(requestDto, "Assert.notNull: requestDto");

        log.debug("log-test-debug--DemoController--hohouhou");
        log.info("log-test-info--DemoController--hohouhou");
        log.warn("log-test-warn--DemoController--hohouhou");
        log.error("log-test-error--DemoController--hohouhou");

        // TODO：
        List<UserEntity> userEntities = userService.getById2(100000000);

        UserEntity userEntity = userService.getByUserId(100000001L);
        return RpcResult.success(requestDto.getName() + "---" + userEntity.toString());
    }
}