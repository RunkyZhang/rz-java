package com.ww.video.base.api.entity;

import com.ww.common.base.dto.EntityBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VideoEntity extends EntityBase {
    private long id;
    private long userId;
    private String title;
    private String icon;
    private String url;
    private int categoryId;
    private int subCategoryId;
    private int status;
    private String userTagsJson;
    private String systemTagsJson;
}



