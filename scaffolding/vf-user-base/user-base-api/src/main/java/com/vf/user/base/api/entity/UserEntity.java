package com.vf.user.base.api.entity;

import com.vf.common.base.dto.EntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserEntity extends EntityBase {
    private int userId;
    private String username;
    private int age;
}



