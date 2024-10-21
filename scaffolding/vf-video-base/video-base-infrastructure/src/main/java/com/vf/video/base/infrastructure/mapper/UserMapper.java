package com.vf.video.base.infrastructure.mapper;

import com.vf.video.base.api.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserEntity getByUserId(int userId);
}
