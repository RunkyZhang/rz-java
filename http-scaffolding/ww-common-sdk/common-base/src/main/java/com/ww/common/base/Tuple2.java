package com.ww.common.base;

import lombok.Data;

@Data
public class Tuple2<T1, T2> {
    private T1 value1;
    private T2 value2;

    public Tuple2() {
    }

    public Tuple2(T1 value1, T2 value2) {
        this.value1 = value1;
        this.value2 = value2;
    }
}
