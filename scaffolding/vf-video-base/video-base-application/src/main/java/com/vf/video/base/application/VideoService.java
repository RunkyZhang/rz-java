package com.vf.video.base.application;

import com.vf.video.base.api.entity.VideoEntity;
import com.vf.video.base.domain.UserDomain;
import com.vf.video.base.infrastructure.rpc.RpcProxy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VideoService {
    @Resource
    private UserDomain userDomain;
    @Resource
    private RpcProxy rpcProxy;

    public String getByVideoId(long videoId) {
        VideoEntity videoEntity = userDomain.getByVideoId(videoId);
        String value = rpcProxy.sayHello("doooeeee");

        return value + videoEntity.toString();
    }
}
