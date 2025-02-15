package com.ww.common.architecture.login;

import lombok.Data;

@Data
public class LoginStatus {
    private int mode;
    private int businessGroup;
    private int positionTypeId;
    private String organizationType;
    private String currentOrganizationId;
    private int channel;
}
