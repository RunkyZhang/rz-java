package com.rz.web.demo.server.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
@Data
public class Argument {
    protected String type;
    protected String description;
    protected String format;

    public Argument() {
    }

    public Argument(String type) {
        Assert.hasText(type, "Assert.hasText: type");
        this.type = type;
    }

    @JsonIgnore
    protected String name;
    @JsonIgnore
    protected boolean required;
}
