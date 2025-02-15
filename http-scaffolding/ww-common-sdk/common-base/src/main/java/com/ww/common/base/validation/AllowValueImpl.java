package com.ww.common.base.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class AllowValueImpl implements ConstraintValidator<AllowValue, Object> {
    private String value;
    private boolean required;

    @Override
    public void initialize(AllowValue constraint) {
        this.value = constraint.value();
        this.required = constraint.required();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (null != obj) {
            if (Objects.nonNull(value)) {
                String[] split = value.split(",");
                for (String s : split) {
                    if (obj instanceof String) {
                        if (s.equals(obj)) {
                            return true;
                        }
                    } else if (obj instanceof Integer) {
                        if ((obj).equals(Integer.parseInt(s))) {
                            return true;
                        }
                    }
                }
            }
        } else {
            return !required;
        }
        return false;
    }
}

