package com.ww.common.architecture.login;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginStatusGetterSelector {
    private final Map<Integer, AbstractLoginStatusGetter> loginStatusGetters = new HashMap<>();
    private final ApplicationContext applicationContext;

    @PostConstruct
    private void init() {
        Map<String, AbstractLoginStatusGetter> loginStatusGetterBeans = applicationContext.getBeansOfType(AbstractLoginStatusGetter.class);
        for (AbstractLoginStatusGetter loginStatusGetter : loginStatusGetterBeans.values()) {
            this.loginStatusGetters.put(loginStatusGetter.getMode(), loginStatusGetter);
        }
    }

    public AbstractLoginStatusGetter select(int mode) {
        AbstractLoginStatusGetter loginStatusGetter = loginStatusGetters.get(mode);
        if (null == loginStatusGetter) {
            throw new NotImplementedException("loginStatusGetter not found for mode: " + mode);
        }
        return loginStatusGetter;
    }
}
