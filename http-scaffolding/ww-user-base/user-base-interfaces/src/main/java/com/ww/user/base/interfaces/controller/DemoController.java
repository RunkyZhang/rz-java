package com.ww.user.base.interfaces.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.dto.SayHelloByNameRequestDto;
import com.ww.user.base.api.entity.AccountSystemEntity;
import com.ww.user.base.api.entity.UserEntity;
import com.ww.user.base.api.service.DemoApi;
import com.ww.user.base.application.AccountSystemService;
import com.ww.user.base.application.UserService;
import com.ww.user.base.infrastructure.ConfigSource;
import com.ww.user.base.infrastructure.rpc.RpcProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@RestController
public class DemoController implements DemoApi {
    @Resource
    private UserService userService;
    @Resource
    private AccountSystemService accountSystemService;
    @Resource
    private ConfigSource configSource;
    @Resource
    private RpcProxy rpcProxy;

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;
    @Value("${spring.cloud.nacos.config.server-addr}")
    private String nacosConfigAddress;
    @Value("${spring.cloud.nacos.config.namespace}")
    private String nacosConfigNamespace;

    // @RequestMapping 会自动生成post，get，delete等多个接口
    @SentinelResource(value = "DemoController.getConfig", blockHandler = "getConfigLimited", blockHandlerClass = ApiFlowLimiting.class)
    @GetMapping("/getConfig")
    public RpcResult<String> getConfig(@RequestParam(name = "name", defaultValue = "unknown user") String name) throws NacosException {
        name += "：" + configSource.getServerAddress() + "---" + configSource.getUserName() + "---" + useLocalCache;

        String dataId = "redis.properties";
        String group = "DEFAULT_GROUP";
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, nacosConfigAddress);
        properties.put(PropertyKeyConst.NAMESPACE, nacosConfigNamespace);
        ConfigService configService = NacosFactory.createConfigService(properties);
        String configContent = configService.getConfig(dataId, group, 5000);

        List<Long> userIds = new ArrayList<>();
        userIds.add(100000001L);
        userIds.add(100000008L);

        List<UserEntity> userEntities = new ArrayList<>();
        UserEntity userEntity = userService.getByUserId(userIds.get(0));
        userEntities.add(userEntity);
        Map<Long, UserEntity> userEntitiesMap = userService.getByUserIds(new HashSet<>(userIds));
        userEntities.addAll(userEntitiesMap.values());

        if (0 == System.currentTimeMillis() % 2) {
            throw new RuntimeException("测试异常情况，几率为50%。");
        }

        return RpcResult.success(name + "---hello!---" + userEntities + "---" + configContent);
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

        Set<Long> userIds = new HashSet<>();
        userIds.add(100000000L);
        userIds.add(100000047L);
        List<AccountSystemEntity> accountSystemEntities = accountSystemService.getByUserId(100000015L);
        if (!CollectionUtils.isEmpty(accountSystemEntities)) {
            AccountSystemEntity accountSystemEntity = accountSystemEntities.get(0);
            userIds.add(accountSystemEntity.getUserId());
        }

        Map<Long, UserEntity> userEntities = userService.getByUserIds(userIds);
        return RpcResult.success(requestDto.getName() + "---" + userEntities.toString());
    }
}