package com.rz.web.tools.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class EntityBase implements Serializable {
    protected Date createAt;
    protected Date modifyAt;
    protected int version;
    protected String operator = "";
    protected boolean deleted = false;
}
