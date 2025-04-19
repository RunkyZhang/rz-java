package com.rz.web.demo.server.schema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Data
public class NumberArgument extends Argument {
    public NumberArgument() {
        super("number");
    }
}
