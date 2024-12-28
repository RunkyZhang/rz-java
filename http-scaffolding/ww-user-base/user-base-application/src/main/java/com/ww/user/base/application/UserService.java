package com.ww.user.base.application;

import com.ww.user.base.api.entity.UserEntity;
import com.ww.user.base.domain.UserDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class UserService {
    @Resource
    private UserDomain userDomain;

    public UserEntity getByUserId(long userId) {
        log.debug("log-test-debug--UserService--hohouhou");
        log.info("log-test-info--UserService--hohouhou");
        log.warn("log-test-warn--UserService--hohouhou");
        log.error("log-test-error--UserService--hohouhou");

        return userDomain.getByUserId(userId);
    }

    public List<UserEntity> getById2(long id) {
        return userDomain.getById2(id);
    }

    public List<UserEntity> getByPhoneNo(String phoneNo) {
        return userDomain.getByPhoneNo(phoneNo);
    }

    public Map<Long, UserEntity> getByUserIds(Set<Long> userIds) {
        return userDomain.getByUserIds(userIds);
    }
}
