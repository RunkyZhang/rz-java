package com.ww.common.architecture.login;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SfaLoginStatusGetter extends AbstractLoginStatusGetter {
    @Override
    public int getMode() {
        return 0;
    }
}
