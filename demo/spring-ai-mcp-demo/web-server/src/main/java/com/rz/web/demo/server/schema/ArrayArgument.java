package com.rz.web.demo.server.schema;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.Assert;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArrayArgument extends Argument {
    private Argument items;

    public ArrayArgument(Argument argument) {
        super("array");

        Assert.isTrue(argument instanceof StringArgument || argument instanceof NumberArgument || argument instanceof EnumArgument,
                "Assert.isTrue: argument instanceof StringArgument || argument instanceof NumberArgument || argument instanceof EnumArgument");

        items = argument;
    }
}
