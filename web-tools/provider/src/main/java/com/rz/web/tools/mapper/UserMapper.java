package com.rz.web.tools.mapper;

import com.rz.web.tools.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    UserEntity getByUserId(int userId);
}
