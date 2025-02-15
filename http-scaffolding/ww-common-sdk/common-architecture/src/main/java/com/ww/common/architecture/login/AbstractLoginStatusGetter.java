package com.ww.common.architecture.login;

import com.ww.common.base.dto.RequestBaseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
public abstract class AbstractLoginStatusGetter {
    private static final String BUSINESS_GROUP_KEY = "businessGroup";
    private static final String POSITION_TYPE_ID_KEY = "positionTypeId";
    private static final String ORGANIZATION_TYPE_KEY = "organizationType";
    private static final String CHANNEL_KEY = "channel";
    private static final String AUTHORIZATION = "Authorization";
    private static final int DEFAULT_CHANNEL = 3;
    private static final int DEFAULT_BUSINESS_GROUP = 1;
    private static final int DEFAULT_POSITION_TYPE_ID = 7;
    private static final String DEFAULT_ORGANIZATION_TYPE = "";

    public abstract int getMode();

    public LoginStatus getLoginStatus(HttpServletRequest request) {
        LoginStatus loginStatus = new LoginStatus();
        loginStatus.setMode(getMode());
        loginStatus.setBusinessGroup(getHeaderValue(request, BUSINESS_GROUP_KEY, DEFAULT_BUSINESS_GROUP));
        loginStatus.setPositionTypeId(getHeaderValue(request, POSITION_TYPE_ID_KEY, DEFAULT_POSITION_TYPE_ID));
        loginStatus.setChannel(getHeaderValue(request, CHANNEL_KEY, DEFAULT_CHANNEL));
        loginStatus.setOrganizationType(getHeaderValue(request, ORGANIZATION_TYPE_KEY, DEFAULT_ORGANIZATION_TYPE));

        return loginStatus;
    }

    public void setLoginStatus(Object[] parameters, LoginStatus loginStatus) {
        if (null == parameters) {
            return;
        }

        for (Object parameter : parameters) {
            if (null == parameter) {
                continue;
            }

            if (parameter instanceof RequestBaseDto) {
                RequestBaseDto requestBaseDto = (RequestBaseDto) parameter;
                requestBaseDto.setLsBusinessGroup(loginStatus.getBusinessGroup());
                requestBaseDto.setLsPositionTypeId(loginStatus.getPositionTypeId());
                requestBaseDto.setLsChannel(loginStatus.getChannel());
                requestBaseDto.setLsOrganizationType(loginStatus.getOrganizationType());
                requestBaseDto.setLsMode(loginStatus.getMode());
                requestBaseDto.setLsCurrentOrganizationId(loginStatus.getCurrentOrganizationId());
            }
        }
    }

    protected int getHeaderValue(HttpServletRequest request, String key, int defaultValue) {
        if (Objects.isNull(request)) {
            return defaultValue;
        }

        String value = request.getHeader(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("header({}) value({}) is not number", key, value, e);
        }

        return defaultValue;
    }

    protected String getHeaderValue(HttpServletRequest request, String key, String defaultValue) {
        if (Objects.isNull(request)) {
            return defaultValue;
        }

        String value = request.getHeader(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }

        return value;
    }
}
