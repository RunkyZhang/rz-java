package com.vf.video.base.interfaces.dubbo;

import com.vf.common.base.dto.RpcResult;
import com.vf.video.base.api.service.SomeService;
import com.vf.video.base.application.UserService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class DemoServiceImpl implements SomeService {
    @Resource
    private UserService userService;

    @Override
    public RpcResult<String> sayHelloByName(String name) {
        String value = userService.getByUserId(1);
        return RpcResult.success(name + ",hello!---" + value);
    }
}
