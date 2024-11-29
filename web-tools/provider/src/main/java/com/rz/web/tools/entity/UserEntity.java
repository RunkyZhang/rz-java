package com.rz.web.tools.entity;

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



