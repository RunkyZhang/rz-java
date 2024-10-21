package com.vf.video.base.infrastructure.mapper;

import com.vf.video.base.api.entity.VideoEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VideoMapper {
    VideoEntity getByVideoId(long videoId);
}
