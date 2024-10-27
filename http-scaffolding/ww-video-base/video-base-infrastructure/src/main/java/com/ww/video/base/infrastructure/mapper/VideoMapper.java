package com.ww.video.base.infrastructure.mapper;

import com.ww.video.base.api.entity.VideoEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VideoMapper {
    VideoEntity getByVideoId(long videoId);
}
