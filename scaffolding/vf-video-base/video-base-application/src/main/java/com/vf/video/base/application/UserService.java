package com.vf.video.base.application;

import com.vf.video.base.api.entity.UserEntity;
import com.vf.video.base.domain.UserDomain;
import com.vf.video.base.infrastructure.rpc.RpcProxy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserDomain userDomain;
    @Resource
    private RpcProxy rpcProxy;

    public String getByUserId(int userId) {
        String value = rpcProxy.sayHello("doooeeee");

        return value;
    }
}
