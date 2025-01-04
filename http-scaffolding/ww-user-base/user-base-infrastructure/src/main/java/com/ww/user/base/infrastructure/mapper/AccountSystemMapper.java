package com.ww.user.base.infrastructure.mapper;

import com.ww.user.base.api.entity.AccountSystemEntity;
import com.ww.user.base.api.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AccountSystemMapper {
    AccountSystemEntity selectById(long videoId);

    // 如果不使用xml中的resultMap，可以使用as的方式设置表字段和类字段名字一样（例如：phone_no as phoneNo）
    @ResultMap("BaseResultMap")
    @Select("select * from ww_account_system where user_id = #{userId,jdbcType=BIGINT}")
    List<AccountSystemEntity> selectByUserId(long userId);
}
