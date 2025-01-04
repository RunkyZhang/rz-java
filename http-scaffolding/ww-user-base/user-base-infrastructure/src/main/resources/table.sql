CREATE TABLE `vf_user`
(
    `phone_no`  varchar(63)  NOT NULL DEFAULT '' COMMENT '手机号，带国家吗',
    `e_mail`    varchar(127) NOT NULL DEFAULT '' COMMENT '邮箱',
    `status`    int          NOT NULL DEFAULT 1 COMMENT '状态（0，1叠加）。正常，冻结，注销',
    `source`    int          NOT NULL DEFAULT 0 COMMENT '注册来源。app，网页',
    `mode`      int          NOT NULL DEFAULT 0 COMMENT '注册方式。手机号，邮箱，三方',

    `id`        bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_at` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`   int          NOT NULL DEFAULT 0 COMMENT '版本号并发用',
    `deleted`   tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
    `operator`  varchar(15)  NOT NULL DEFAULT '' COMMENT '操作人',

    PRIMARY KEY (`id`),

    KEY `idx_phone_no` (`phone_no`),
    KEY `idx_e_mail` (`e_mail`),
    KEY `idx_create_at` (`create_at`)

) DEFAULT CHARACTER SET = utf8mb4 COLLATE=utf8mb4_unicode_ci AUTO_INCREMENT = 100000000 COMMENT ='用户注册表';

CREATE TABLE `ww_account_system`
(
    `name`               varchar(63) NOT NULL DEFAULT '' COMMENT '名称',
    `user_id`            bigint      NOT NULL DEFAULT 0 COMMENT '用户Id',
    `app_code`           int         NOT NULL DEFAULT 0 COMMENT '应用名称（枚举）',
    `business_unit_code` int         NOT NULL DEFAULT 0 COMMENT '事业部Id（枚举）',

    `id`                 bigint      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_at`          timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_at`          timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`            int         NOT NULL DEFAULT 0 COMMENT '版本号并发用',
    `deleted`            tinyint     NOT NULL DEFAULT 0 COMMENT '是否删除',
    `operator`           varchar(15) NOT NULL DEFAULT '' COMMENT '操作人',

    PRIMARY KEY (`id`),

    KEY `idx_phone_no` (`user_id`),
    KEY `idx_create_at` (`create_at`)

) DEFAULT CHARACTER SET = utf8mb4 COLLATE=utf8mb4_unicode_ci AUTO_INCREMENT = 100000000 COMMENT ='账号体系表';


/*普通索引索引*/ ALTER TABLE employees
    ADD INDEX idx_houhou (houhou);
/*唯一索引索引*/ ALTER TABLE employees
    ADD UNIQUE idx_houhou (houhou);


/*
CREATE TABLE `vf_user_device`
(
    `user_id`      bigint       NOT NULL DEFAULT 0 COMMENT '用户Id',
    `device_id`    varchar(63)  NOT NULL DEFAULT '' COMMENT '设备Id',
    `login_time`   timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登陆时间',
    `publish_time` timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '签发时间',
    `signature`    varchar(255) NOT NULL DEFAULT '' COMMENT '设备Id',

    `id`           bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_at`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_at`    timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`      int          NOT NULL DEFAULT 0 COMMENT '版本号并发用',
    `deleted`   tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
    `operator`     varchar(15)  NOT NULL DEFAULT '' COMMENT '操作人',

    PRIMARY KEY (`id`),

    KEY            `idx_user_id` (`user_id`),
    KEY            `idx_device_id` (`device_id`),
    KEY            `idx_create_at` (`create_at`)
) DEFAULT CHARACTER SET=utf8mb4 AUTO_INCREMENT=1000 COMMENT='用户登陆表';

CREATE TABLE `vf_user_union`
(
    `user_id`      bigint        NOT NULL DEFAULT 0 COMMENT '用户Id',
    `type`         int           NOT NULL DEFAULT 0 COMMENT '三方类型',
    `union_id`     varchar(63)   NOT NULL DEFAULT '' COMMENT '联合Id',
    `open_id`      varchar(63)   NOT NULL DEFAULT '' COMMENT '开放Id',
    `content_json` varchar(2000) NOT NULL DEFAULT '' COMMENT '扩展json',

    `id`           bigint        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_at`    timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_at`    timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`      int           NOT NULL DEFAULT 0 COMMENT '版本号并发用',
    `deleted`   tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
    `operator`     varchar(51)   NOT NULL DEFAULT '' COMMENT '操作人',

    PRIMARY KEY (`id`),

    KEY            `idx_user_id` (`user_id`),
    KEY            `idx_open_id` (`open_id`),
    KEY            `idx_create_at` (`create_at`)
) DEFAULT CHARACTER SET=utf8mb4 AUTO_INCREMENT=1000 COMMENT='用户三方信息表';

CREATE TABLE `vf_user_info`
(
    `user_id`      bigint        NOT NULL DEFAULT 0 COMMENT '用户Id',
    `name`         varchar(31)   NOT NULL DEFAULT '' COMMENT '昵称',
    `icon`         varchar(1023) NOT NULL DEFAULT '' COMMENT '头像',
    `country_code` int           NOT NULL DEFAULT 0 COMMENT '国家码',
    `status`       int           NOT NULL DEFAULT 1 COMMENT '状态。离线，在线，直播',

    `id`           bigint        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_at`    timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_at`    timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`      int           NOT NULL DEFAULT 0 COMMENT '版本号并发用',
    `deleted`   tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
    `operator`     varchar(15)   NOT NULL DEFAULT '' COMMENT '操作人',

    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_id` (`user_id`),

    KEY            `idx_name` (`name`),
    KEY            `idx_create_at` (`create_at`)
) DEFAULT CHARACTER SET=utf8mb4 AUTO_INCREMENT=1000 COMMENT='用户基本信息表';

CREATE TABLE `vf_user_asset`
(
    `user_id`   bigint      NOT NULL DEFAULT 0 COMMENT '用户Id',
    `type`      int         NOT NULL DEFAULT 0 COMMENT '资产类型',
    `count`     int         NOT NULL DEFAULT 0 COMMENT '数量',
    `name`      varchar(31) NOT NULL DEFAULT '' COMMENT '名字',
    `source`    int         NOT NULL DEFAULT 0 COMMENT '来源。购买，发放',
    `status`    int         NOT NULL DEFAULT 1 COMMENT '状态。冻结，正常',
    `expire`    timestamp            DEFAULT NULL COMMENT '过期时间',

    `id`        bigint      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_at` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_at` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`   int         NOT NULL DEFAULT 0 COMMENT '版本号并发用',
    `deleted`   tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
    `operator`  varchar(51) NOT NULL DEFAULT '' COMMENT '操作人',

    PRIMARY KEY (`id`),

    KEY         `idx_user_id` (`user_id`),
    KEY         `idx_source` (`source`),
    KEY         `idx_expire` (`expire`),
    KEY         `idx_create_at` (`create_at`)
) DEFAULT CHARACTER SET=utf8mb4 AUTO_INCREMENT=1000 COMMENT='用户资产表';

CREATE TABLE `vf_user_number`
(
    `user_id`   bigint      NOT NULL DEFAULT 0 COMMENT '用户Id',
    `number`    bigint      NOT NULL DEFAULT 0 COMMENT '分数。小数类型需要*100',
    `type`      int         NOT NULL DEFAULT 0 COMMENT '分数类型',
    `name`      varchar(31) NOT NULL DEFAULT '' COMMENT '名字',
    `source`    int         NOT NULL DEFAULT 0 COMMENT '来源。购买，发放',
    `status`    int         NOT NULL DEFAULT 1 COMMENT '状态。冻结，正常',

    `id`        bigint      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_at` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_at` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '更新时间',
    `version`   int         NOT NULL DEFAULT 0 COMMENT '版本号并发用',
    `deleted`   tinyint      NOT NULL DEFAULT 0 COMMENT '是否删除',
    `operator`  varchar(51) NOT NULL DEFAULT '' COMMENT '操作人',

    PRIMARY KEY (`id`),

    KEY         `idx_user_id` (`user_id`),
    KEY         `idx_source` (`source`),
    KEY         `idx_create_at` (`create_at`)
) DEFAULT CHARACTER SET=utf8mb4 AUTO_INCREMENT=1000 COMMENT='用户分数表';
*/