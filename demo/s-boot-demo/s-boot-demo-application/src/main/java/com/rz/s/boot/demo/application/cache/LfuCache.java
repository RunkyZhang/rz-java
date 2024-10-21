package com.rz.s.boot.demo.application.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LfuCache {
    private static final Map<Integer, Set<String>> fValuesCounts = new HashMap<>();
    private static final Map<String, Tow> values = new HashMap<>();
    private final int count = 100000;
    // TODO: 优化
    private int currentSize = 1;

    static {
        for (int i = 1; i <= 3; i++) {
            fValuesCounts.put(1, new HashSet<>());
        }
    }

    public String get(String key) {
        if (values.containsKey(key)) {
            Tow tow = values.get(key);
            tow.setCount(tow.getCount() + 1);

            Set<String> fValues = fValuesCounts.get(tow.getCount() - 1);
            fValues.remove(key);

            fValues = fValuesCounts.get(tow.getCount());
            fValues.add(key);

            return tow.getValue();
        }

        return null;
    }

    public void put(String key, String value) {
        Tow tow = null;
        if (values.containsKey(key)) {
            tow = values.get(key);
            tow.setValue(value);
            tow.setCount(tow.getCount() + 1);

            Set<String> fValues = fValuesCounts.get(tow.getCount() - 1);
            fValues.remove(key);

            fValues = fValuesCounts.get(tow.getCount());
            fValues.add(key);
        } else {
            if (values.size() > count) {
                clear();
            }

            tow = new Tow();
            tow.setValue(value);
            tow.setCount(1);
            values.put(key, tow);
            Set<String> fValues = fValuesCounts.get(tow.getCount());
            fValues.add(key);

        }
    }

    private void clear() {
        Set<String> fValues;
        if (0 < (fValues = fValuesCounts.get(1)).size()) {
            String key = null;
            for (String fValue : fValues) {
                key = fValue;
                break;
            }

            fValues.remove(key);
        } else if (0 < (fValues = fValuesCounts.get(2)).size()) {
            String key = null;
            for (String fValue : fValues) {
                key = fValue;
                break;
            }

            fValues.remove(key);
        } else if (0 < (fValues = fValuesCounts.get(3)).size()) {
            String key = null;
            for (String fValue : fValues) {
                key = fValue;
                break;
            }

            fValues.remove(key);
        }
    }
}
