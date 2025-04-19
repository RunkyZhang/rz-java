package com.rz.web.demo.server.schema;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StringArgument extends Argument {
    public StringArgument() {
        super("string");
    }
}
