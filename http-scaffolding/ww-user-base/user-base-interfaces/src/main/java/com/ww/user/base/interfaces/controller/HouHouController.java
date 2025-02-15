package com.ww.user.base.interfaces.controller;

import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.dto.SayHouHouByNameRequestDto;
import com.ww.user.base.api.entity.AccountSystemEntity;
import com.ww.user.base.api.entity.UserEntity;
import com.ww.user.base.api.service.HouHouApi;
import com.ww.user.base.application.AccountSystemService;
import com.ww.user.base.application.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
public class HouHouController implements HouHouApi {
    @Resource
    private UserService userService;
    @Resource
    private AccountSystemService accountSystemService;

    // curl --request POST --url http://localhost:7070/houhou --header 'Content-Type: application/json' --data '{"name": "houhou"}'
    // http的路径和method定义在接口中
    @Override
    public RpcResult<String> sayHouHouByName(@RequestBody SayHouHouByNameRequestDto requestDto) {
        Assert.notNull(requestDto, "Assert.notNull: requestDto");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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