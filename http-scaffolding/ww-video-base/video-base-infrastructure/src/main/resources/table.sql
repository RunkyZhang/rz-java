CREATE TABLE `vf_video`
(
    `user_id`         bigint       NOT NULL DEFAULT 0 COMMENT '用户Id',
    `title`           varchar(31)  NOT NULL DEFAULT '' COMMENT '标题',
    `icon`            varchar(512) NOT NULL DEFAULT '' COMMENT '头像',
    `url`             varchar(512) NOT NULL DEFAULT '' COMMENT '链接',
    `category_id`     int          NOT NULL DEFAULT 1 COMMENT '状态（0，1叠加）。正常，冻结，注销',
    `sub_category_id` int          NOT NULL DEFAULT 0 COMMENT '注册来源。app，网页',
    `status`          int          NOT NULL DEFAULT 1 COMMENT '状态。离线，在线，直播',
    `user_tags_json`   varchar(256) NOT NULL DEFAULT '' COMMENT '用户标签json',
    `system_tags_json` varchar(256) NOT NULL DEFAULT '' COMMENT '系统标签json',

    `id`              bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_at`       timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_at`       timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`         int          NOT NULL DEFAULT 0 COMMENT '版本号并发用',
    `deleted`         tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
    `operator`        varchar(15)  NOT NULL DEFAULT '' COMMENT '操作人',

    PRIMARY KEY (`id`),

    KEY               `idx_title` (`title`),
    KEY               `idx_user_id` (`user_id`),
    KEY               `idx_create_at` (`create_at`)

) DEFAULT CHARACTER SET=utf8mb4 AUTO_INCREMENT=1000 COMMENT='视频信息表';
/*
CREATE TABLE `vf_video_statistics`
(
    `video_id`          bigint      NOT NULL DEFAULT 0 COMMENT '视频Id',
    `liked_count`       bigint      NOT NULL DEFAULT 0 COMMENT '点赞量',
    `shared_count`      bigint      NOT NULL DEFAULT 0 COMMENT '分享量',
    `watched_count`     bigint      NOT NULL DEFAULT 0 COMMENT '播放量',
    `comment_count`     bigint      NOT NULL DEFAULT 0 COMMENT '评论量',
    `recommended_count` bigint      NOT NULL DEFAULT 0 COMMENT '推荐量',
    `favorite_count`    bigint      NOT NULL DEFAULT 0 COMMENT '收藏量',

    `id`                bigint      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_at`         timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_at`         timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`           int         NOT NULL DEFAULT 0 COMMENT '版本号并发用',
    `deleted`            tinyint     NOT NULL DEFAULT 0 COMMENT '是否删除',
    `operator`          varchar(15) NOT NULL DEFAULT '' COMMENT '操作人',

    PRIMARY KEY (`id`),

    KEY                 `idx_video_id` (`video_id`),
    KEY                 `idx_create_at` (`create_at`)
) DEFAULT CHARACTER SET=utf8mb4 AUTO_INCREMENT=1000 COMMENT='视频统计表';

CREATE TABLE `vf_video_comment`
(
    `video_id`          bigint        NOT NULL DEFAULT 0 COMMENT '视频Id',
    `target_type`       int           NOT NULL DEFAULT 0 COMMENT '三方类型',
    `target_comment_id` bigint        NOT NULL DEFAULT 0 COMMENT '目标评论id',
    `target_user_id`    bigint        NOT NULL DEFAULT 0 COMMENT '目标用户Id',
    `liked_count`       bigint        NOT NULL DEFAULT 0 COMMENT '点赞量',
    `content_json`      varchar(1023) NOT NULL DEFAULT '' COMMENT '内容json',

    `id`                bigint        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_at`         timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_at`         timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`           int           NOT NULL DEFAULT 0 COMMENT '版本号并发用',
    `deleted`            tinyint       NOT NULL DEFAULT 0 COMMENT '是否删除',
    `operator`          varchar(51)   NOT NULL DEFAULT '' COMMENT '操作人',

    PRIMARY KEY (`id`),

    KEY                 `idx_user_id` (`user_id`),
    KEY                 `idx_open_id` (`open_id`),
    KEY                 `idx_create_at` (`create_at`)
) DEFAULT CHARACTER SET=utf8mb4 AUTO_INCREMENT=1000 COMMENT='视频评论表';
 */