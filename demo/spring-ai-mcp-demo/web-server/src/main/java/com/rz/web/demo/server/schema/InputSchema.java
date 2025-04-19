package com.rz.web.demo.server.schema;

import com.rz.web.demo.server.JacksonHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.*;

@Slf4j
@Data
public class InputSchema {
    private String type = "object";
    private String id;
    private final Map<String, Argument> properties = new HashMap<>();
    private final Set<String> required = new HashSet<>();

    public InputSchema withId(String id) {
        this.id = id;
        return this;
    }

    public InputSchema withString(String name, boolean required) {
        Assert.hasText(name, "Assert.hasText: name");

        StringArgument stringArgument = new StringArgument();
        stringArgument.setRequired(required);
        stringArgument.setName(name);
        this.properties.put(name, stringArgument);
        if (required) {
            this.required.add(name);
        } else {
            this.required.remove(name);
        }

        return this;
    }

    public InputSchema withNumber(String name, boolean required) {
        Assert.hasText(name, "Assert.hasText: name");

        NumberArgument numberArgument = new NumberArgument();
        numberArgument.setName(name);
        numberArgument.setRequired(required);
        this.properties.put(name, numberArgument);
        if (required) {
            this.required.add(name);
        } else {
            this.required.remove(name);
        }

        return this;
    }

    public InputSchema withEnum(String name, boolean required, List<Object> enums) {
        Assert.hasText(name, "Assert.hasText: name");
        Assert.notEmpty(enums, "Assert.notEmpty: enums");

        EnumArgument enumArgument = new EnumArgument(enums);
        enumArgument.setName(name);
        enumArgument.setRequired(required);
        enumArgument.setEnums(enums);
        this.properties.put(name, enumArgument);
        if (required) {
            this.required.add(name);
        } else {
            this.required.remove(name);
        }

        return this;
    }

    public InputSchema withArray(String name, boolean required, Argument argument) {
        Assert.hasText(name, "Assert.hasText: name");
        Assert.notNull(argument, "Assert.notNull: argument");

        ArrayArgument arrayArgument = new ArrayArgument(argument);
        arrayArgument.setName(name);
        arrayArgument.setRequired(required);
        this.properties.put(name, arrayArgument);
        if (required) {
            this.required.add(name);
        } else {
            this.required.remove(name);
        }

        return this;
    }

    public InputSchema withObject(String name, boolean required, Map<String, Argument> properties) {
        Assert.hasText(name, "Assert.hasText: name");
        Assert.notEmpty(properties, "Assert.notEmpty: properties");

        ObjectArgument objectArgument = new ObjectArgument();
        objectArgument.setName(name);
        objectArgument.setRequired(required);
        objectArgument.setProperties(properties);
        this.properties.put(name, objectArgument);
        if (required) {
            this.required.add(name);
        } else {
            this.required.remove(name);
        }

        return this;
    }

    public String build() {
        return JacksonHelper.toPrettyJson(this, true);
    }
}
