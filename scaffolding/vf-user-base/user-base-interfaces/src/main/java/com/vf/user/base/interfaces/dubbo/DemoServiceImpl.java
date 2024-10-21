package com.vf.user.base.interfaces.dubbo;

import com.vf.common.base.dto.RpcResult;
import com.vf.user.base.api.dto.SayHelloByNameRequestDto;
import com.vf.user.base.api.entity.UserEntity;
import com.vf.user.base.api.service.DemoService;
import com.vf.user.base.application.UserService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class DemoServiceImpl implements DemoService {
    @Resource
    private UserService userService;

    @Override
    public RpcResult<String> sayHelloByName(SayHelloByNameRequestDto requestDto) {
        UserEntity user = userService.getByUserId(1);
        return RpcResult.success(requestDto.getName() + ",hello!---" + user.toString());
    }
}
