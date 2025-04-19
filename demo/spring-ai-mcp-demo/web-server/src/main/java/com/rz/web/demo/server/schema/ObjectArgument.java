package com.rz.web.demo.server.schema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ObjectArgument extends Argument {
    private Map<String, Argument> properties;

    public ObjectArgument() {
        super("object");
    }
}
