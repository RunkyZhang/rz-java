package com.vf.user.base.application;

import com.vf.user.base.api.entity.UserEntity;
import com.vf.user.base.domain.UserDomain;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {
    @Resource
    private UserDomain userDomain;

    public UserEntity getByUserId(int userId) {
        return userDomain.getByUserId(userId);
    }
}
