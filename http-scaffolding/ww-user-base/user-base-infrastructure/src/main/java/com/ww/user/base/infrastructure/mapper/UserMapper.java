package com.ww.user.base.infrastructure.mapper;

import com.ww.user.base.api.entity.UserEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface UserMapper {
    UserEntity selectVFById(long id);

    List<UserEntity> selectById2(long id);

    // 如果不使用xml中的resultMap，可以使用as的方式设置表字段和类字段名字一样（例如：phone_no as phoneNo）
    @ResultMap("BaseResultMap")
    @Select("select * from vf_user where phone_no = #{phoneNo,jdbcType=VARCHAR}")
    List<UserEntity> selectByPhoneNo(String phoneNo);

    @ResultMap("BaseResultMap")
    @MapKey("id")
    @Select("<script>"+
            "  select *" +
            "  from vf_user" +
            "  where deleted != 0" +
            "  and id in" +
            "  <foreach collection='ids' item='id' index='index' separator=',' open='(' close=')'>" +
            "    #{id,jdbcType=BIGINT}" +
            "  </foreach>" +
            "</script>")
    Map<Long, UserEntity> selectByIds(@Param("ids") Set<Long> ids);
}
