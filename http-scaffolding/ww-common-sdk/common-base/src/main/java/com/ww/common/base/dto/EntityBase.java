package com.ww.common.base.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class EntityBase implements Serializable {
    /**
     * Column: create_time Remark: 添加时间
     */
    protected Date createAt;
    /**
     * Column: modify_time Remark: 修改时间
     */
    protected Date modifyAt;
    /**
     * Column: version Remark: 版本号
     */
    protected int version;
    /**
     * Column: operator Remark: 操作人
     */
    protected String operator = "";
    /**
     * Column: deleted Remark: 逻辑删除
     */
    protected boolean deleted = false;
}
