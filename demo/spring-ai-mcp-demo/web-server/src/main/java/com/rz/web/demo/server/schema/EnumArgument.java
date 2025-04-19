package com.rz.web.demo.server.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.Assert;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EnumArgument extends Argument {
    @JsonProperty("enum")
    private List<Object> enums;

    public EnumArgument(List<Object> enums) {
        super("enum");

        Assert.notEmpty(enums, "Assert.notEmpty: enums");
        this.enums = enums;
    }
}
