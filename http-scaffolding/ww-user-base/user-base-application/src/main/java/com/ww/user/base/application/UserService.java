package com.ww.user.base.application;

import com.ww.user.base.api.entity.UserEntity;
import com.ww.user.base.domain.UserDomain;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {
    @Resource
    private UserDomain userDomain;

    public UserEntity getByUserId(long userId) {
        return userDomain.getByUserId(userId);
    }

    public List<UserEntity> getByPhoneNo(String phoneNo) {
        return userDomain.getByPhoneNo(phoneNo);
    }

    public Map<Long, UserEntity> getByUserIds(Set<Long> userIds) {
        return userDomain.getByUserIds(userIds);
    }
}
