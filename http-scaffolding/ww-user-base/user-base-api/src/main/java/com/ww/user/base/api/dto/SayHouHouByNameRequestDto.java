package com.ww.user.base.api.dto;

import com.ww.common.base.dto.RequestBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SayHouHouByNameRequestDto extends RequestBaseDto {
    private String name;
}
