package com.ww.video.base.application;

import com.ww.video.base.api.entity.VideoEntity;
import com.ww.video.base.domain.VideoDomain;
import com.ww.video.base.infrastructure.rpc.RpcProxy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class VideoService {
    @Resource
    private VideoDomain videoDomain;
    @Resource
    private RpcProxy rpcProxy;

    public String getByVideoId(long videoId) {
        VideoEntity videoEntity = videoDomain.getByVideoId(videoId);
        String value = rpcProxy.sayHello("doooeeee");

        return value + videoEntity.toString();
    }
}
