package com.ww.common.base.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class RequestBaseDto extends BaseDto {
    private long lsUserId;
    private String lsDeviceId;
    private String lsToken;

    private int lsMode;
    private int lsBusinessGroup;
    private int lsPositionTypeId;
    private String lsOrganizationType;
    private String lsCurrentOrganizationId;
    private int lsChannel;
}
