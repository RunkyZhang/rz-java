/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ww.user.base.interfaces.web;

import com.ww.common.base.dto.RpcResult;
import com.ww.user.base.api.entity.UserEntity;
import com.ww.user.base.application.UserService;
import com.ww.user.base.infrastructure.ConfigSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RestController
public class BasicController {
    @Resource
    private UserService userService;
    @Resource
    private ConfigSource configSource;

    @Value("${useLocalCache:false}")
    private boolean useLocalCache;
//    @Value("${userName:1111}")
//    private String userName;
//    @Value("${serverAddress:11。11。11。11}")
//    private String serverAddress;

    @RequestMapping("")
    @ResponseBody
    public String test(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        return "Hello " + name + "===" + configSource.getServerAddress() + "---" + configSource.getUserName() + "---" + useLocalCache;
    }

    // http://127.0.0.1:8080/hello?name=lisi
    @RequestMapping("/hello")
    @ResponseBody
    public RpcResult<String> hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        UserEntity userEntity = userService.getByUserId(100000000);
        return RpcResult.success(name + ",hello!---" + userEntity.toString());
    }

    @RequestMapping(value = "/echo/{string}", method = RequestMethod.GET)
    public String echo(@PathVariable String string) {
        return "wwwwwwwwwwHello Nacos Discovery " + string;
    }
}
