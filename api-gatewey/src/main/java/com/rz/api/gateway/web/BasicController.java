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

package com.rz.api.gateway.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RefreshScope
@Controller
public class BasicController {
    @Value("${useLocalCache:false}")
    private boolean useLocalCache;
    @Value("${userName:1111}")
    private String userName;
    @Value("${serverAddress:11。11。11。11}")
    private String serverAddress;

    // http://127.0.0.1:5050?name=lisi
    @RequestMapping("")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        name += "===" + serverAddress + "---" + userName + "---" + useLocalCache;
        return "Hello " + name;
    }
}
