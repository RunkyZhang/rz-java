package com.vf.user.base.api.entity;

import com.vf.common.base.dto.EntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserEntity extends EntityBase {
    private long id;
    private String phoneNo;
    private String eMail;
    private int status;
    private int source;
    private int mode;
}



