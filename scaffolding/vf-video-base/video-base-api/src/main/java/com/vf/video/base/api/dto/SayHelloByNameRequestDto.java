package com.vf.video.base.api.dto;

import com.vf.common.base.dto.RequestBaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SayHelloByNameRequestDto extends RequestBaseDto {
    private String name;
}
