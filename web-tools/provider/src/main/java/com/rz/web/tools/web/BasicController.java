package com.rz.web.tools.web;

import com.rz.web.tools.entity.UserEntity;
import com.rz.web.tools.mapper.UserMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class BasicController {
    @Resource
    private UserMapper userMapper;

    // http://127.0.0.1:8888/hello?name=lisi
    @RequestMapping("/hello")
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        UserEntity userEntity = userMapper.getByUserId(100000000);

        return name + " Hello: " + userEntity.toString();
    }
}
