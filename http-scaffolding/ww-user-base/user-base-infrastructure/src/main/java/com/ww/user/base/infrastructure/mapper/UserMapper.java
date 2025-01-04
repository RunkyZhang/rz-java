package com.ww.user.base.infrastructure.mapper;

import com.ww.user.base.api.entity.UserEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface UserMapper {
    UserEntity selectById(long id);

    @ResultMap("BaseResultMap")
    @MapKey("id")
    @Select("<script>"+
            "  select *" +
            "  from ww_user" +
            "  where deleted != 0" +
            "  and id in" +
            "  <foreach collection='ids' item='id' index='index' separator=',' open='(' close=')'>" +
            "    #{id,jdbcType=BIGINT}" +
            "  </foreach>" +
            "</script>")
    Map<Long, UserEntity> selectByIds(@Param("ids") Set<Long> ids);
}
