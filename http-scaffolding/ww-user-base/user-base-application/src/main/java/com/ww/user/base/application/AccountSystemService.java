package com.ww.user.base.application;

import com.ww.user.base.api.entity.AccountSystemEntity;
import com.ww.user.base.domain.AccountSystemDomain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class AccountSystemService {
    @Resource
    private AccountSystemDomain accountSystemDomain;

    public AccountSystemEntity getById(long userId) {
        return accountSystemDomain.getById(userId);
    }

    public List<AccountSystemEntity> getByUserId(long userId) {
        return accountSystemDomain.getByUserId(userId);
    }
}
