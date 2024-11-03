package com.ww.user.base.infrastructure.rpc;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/*
    #feign配置
    #自定义feignClient超时配置，third是name属性的值
    feign.client.config.third.connect-timeout=100
    feign.client.config.third.read-timeout=100

    #feign超时设置会覆盖ribbon，默认设置为10s、60s
    feign.client.config.default.connect-timeout=1000
    feign.client.config.default.read-timeout=5000
* */
@FeignClient(name = "third", url = "${dingtalk.domain:https://oapi.dingtalk.com}")
public interface ThirdDemoFeignClient {
    /**
     * 获取access_token
     *
     * @return
     */
    @GetMapping("/gettoken")
    String getToken(@RequestParam("appkey") String appkey, @RequestParam("appsecret") String appsecret);

    /**
     * 根据手机号获取userId
     *
     * @return
     */
    @PostMapping(value = "/topapi/v2/user/getbymobile?access_token={access_token}", headers = "Content-Type=application/json")
    String getUserId(@RequestBody String mobile, @RequestParam("access_token") String access_token);

    /**
     * 可以通过feign.Response获取Response Header
     *
     * @return
     */
    @GetMapping(value = "/search?q=aaa")
    Response search(@RequestParam(name = "q") String q);

    /**
     * 可以通过feign.Response获取Response Header
     *
     * @return
     */
    @PostMapping(value = "/dubbo/clientApi/queryByUserId")
    Response queryByUserId(@RequestBody Map<String, Object> body);
}
