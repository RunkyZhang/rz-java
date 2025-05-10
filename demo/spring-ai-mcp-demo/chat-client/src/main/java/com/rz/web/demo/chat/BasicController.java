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

package com.rz.web.demo.chat;

import com.rz.web.demo.chat.dto.RpcResult;
import com.rz.web.demo.chat.rpc.RpcProxy;
import com.rz.web.demo.chat.rpc.ServerFeignClient;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


/**
 * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
 */
@RestController
public class BasicController {
    @Resource
    private ServerFeignClient serverFeignClient;
    @Resource
    private RpcProxy rpcProxy;

    // http://127.0.0.1:8081/hello?name=lisi
    @GetMapping("/hello")
    @ResponseBody
    public String hello(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
        String result = serverFeignClient.hello(name);

        return "Client-Hello " + name + ": " + result;
    }

    @GetMapping("/chat")
    @ResponseBody
    public RpcResult<Object> chat(@RequestParam(name = "message", defaultValue = "现在是什么世纪？") String message) {
        return rpcProxy.chat(message);
    }

    // http://127.0.0.1:8081/html
    @RequestMapping("/html")
    public String html() {
        return "index.html";
    }
}
