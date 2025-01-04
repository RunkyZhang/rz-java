package com.ww.user.base.domain;

import com.ww.user.base.api.entity.AccountSystemEntity;
import com.ww.user.base.infrastructure.mapper.AccountSystemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class AccountSystemDomain {
    public static final String cacheKey = "AccountSystemDomain_cache_key";

    @Resource
    private AccountSystemMapper accountSystemMapper;

    public AccountSystemEntity getById(long id) {
        return accountSystemMapper.selectById(id);
    }

    public List<AccountSystemEntity> getByUserId(long userId) {
        return accountSystemMapper.selectByUserId(userId);
    }
}
