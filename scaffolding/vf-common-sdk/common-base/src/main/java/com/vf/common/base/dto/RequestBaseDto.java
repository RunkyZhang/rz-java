package com.vf.common.base.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class RequestBaseDto extends BaseDto {
    private long userId;
    private String deviceId;
    private String token;
}
