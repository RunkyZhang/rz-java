package com.ww.user.base.api.entity;

import com.ww.common.base.dto.EntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AccountSystemEntity extends EntityBase {
    private long id;
    private long userId;
    private String name;
    private int appCode;
    private int businessUnitCode;
}



