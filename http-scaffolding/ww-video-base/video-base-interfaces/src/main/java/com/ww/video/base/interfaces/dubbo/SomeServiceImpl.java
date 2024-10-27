//package com.ww.video.base.interfaces.dubbo;
//
//import com.ww.common.base.dto.RpcResult;
//import com.ww.video.base.api.dto.SayHelloByNameRequestDto;
//import com.ww.video.base.api.service.SomeService;
//import com.ww.video.base.application.VideoService;
//import org.apache.dubbo.config.annotation.DubboService;
//import org.springframework.util.Assert;
//
//import javax.annotation.Resource;
//
//@DubboService
//public class SomeServiceImpl implements SomeService {
//    @Resource
//    private VideoService userService;
//
//    /**
//     * 根据名字say hello
//     *
//     * @return name + hello
//     * telnet localhost 16622
//     * invoke com.ww.video.base.api.service.SomeService.sayHelloByName({name:"111"})
//     */
//    @Override
//    public RpcResult<String> sayHelloByName(SayHelloByNameRequestDto requestDto) {
//        Assert.notNull(requestDto, "Assert.notNull: requestDto");
//
//        String value = userService.getByVideoId(1000);
//        return RpcResult.success(requestDto.getName() + ",hello!---" + value);
//    }
//}
