package com.rz.s.boot.demo.application;

import com.rz.s.boot.demo.application.block.Blocker;
import com.rz.s.boot.demo.application.block.BlockerSetting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class DemoService {
    private final Blocker blocker;
    private BlockerSetting setting = null;

    public String getTime(int value) {
        return new Date().toString() + "===" + value;
    }

    public Object add() {
        if (null == setting) {
            setting = new BlockerSetting();
            setting.setErrorDuration(20000);
            setting.setErrorTimes(5);
            setting.setRetryDuration(10000);
            setting.setRunner(o -> {
                if (0 == System.currentTimeMillis() % 2) {
                    throw new RuntimeException("cuolecuole");
                }

                return "aaaaa";
            });
            setting.setFailBack(o -> {
                return "failed";
            });

            blocker.addSetting(setting);
            return "add";
        } else {
            try {
                return blocker.run(setting, new ArrayList<>());
            } catch (Exception e) {
                return e.getMessage();
            }
        }
    }
}
