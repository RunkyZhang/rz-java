///*
// * Copyright 2013-2018 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.ww.video.base.interfaces.web;
//
//import com.ww.video.base.application.VideoService;
//import com.ww.video.base.infrastructure.ConfigSource;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
//import javax.annotation.Resource;
//
///**
// * @author <a href="mailto:chenxilzx1@gmail.com">theonefx</a>
// */
//@RestController
//public class BasicController {
//    @Resource
//    private VideoService userService;
//    @Resource
//    private ConfigSource configSource;
//    @Resource
//    private RestTemplate restTemplate;
//
//    @Value("${useLocalCache:false}")
//    private boolean useLocalCache;
//
//    @RequestMapping("")
//    @ResponseBody
//    public String test(@RequestParam(name = "name", defaultValue = "unknown user") String name) {
//        name += userService.getByVideoId(1000);
//        return name + "===" + configSource.getServerAddress() + "---" + configSource.getUserName() + "---" + useLocalCache;
//    }
//}
