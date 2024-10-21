package com.vf.common.base.dto;

import java.io.Serializable;
import java.util.Date;

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
    protected Integer isDel = 0;
}
