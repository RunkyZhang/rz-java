package com.rz.langchain.demo.server.dto;
import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddressDto implements Serializable {
    /**
     * 国家
     */
    @Description("国家")
    private String country;

    /**
     * 省份
     */
    @Description("省份")
    private String province;

    /**
     * 城市
     */
    @Description("城市")
    private String city;

    /**
     * 区县
     */
    @Description("区县")
    private String district;

    /**
     * 街道/详细地址
     */
    private String street;

    /**
     * 姓名
     */
    @Description("姓名")
    private String name;

    /**
     * 电话
     */
    @Description("电话")
    private String phoneNo;

    /**
     * 备注
     */
    @Description("备注")
    private String remark;
}
