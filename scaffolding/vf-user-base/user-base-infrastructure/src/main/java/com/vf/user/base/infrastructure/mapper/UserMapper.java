package com.vf.user.base.infrastructure.mapper;

import com.vf.user.base.api.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserEntity getByUserId(int userId);
}
