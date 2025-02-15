package com.ww.common.base.dto.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ww.common.base.dto.RequestBaseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import springfox.documentation.annotations.ApiIgnore;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class PageRequestBaseDto extends RequestBaseDto {
    @ApiModelProperty(value = "页数，从1开始")
    private Integer page = 1;
    @ApiModelProperty(value = "条数")
    private Integer rows = 5;

    @ApiIgnore
    @JsonIgnore
    public Integer getOffset() {
        if (this.page >= 2 && 0 != this.rows) {
            return (page - 1) * rows;
        }
        return 0;
    }

    @ApiIgnore
    @JsonIgnore
    public Integer getLimit() {
        if (0 != this.rows) {
            return this.rows;
        }
        return 5;
    }
}
