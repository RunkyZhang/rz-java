package com.ww.user.base.application;

import com.ww.user.base.api.entity.UserEntity;
import com.ww.user.base.domain.UserDomain;
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
