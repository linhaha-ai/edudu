-- ----------------------------
-- AI早教陪伴平台 - 业务表SQL
-- 包含：前端用户、孩子管理、AI故事、每日任务、成长记录、家长助手、订阅管理
-- ----------------------------

-- ----------------------------
-- 0、前端用户表（与后台sys_user区分，面向家长用户）
-- ----------------------------
drop table if exists edu_user;
create table edu_user
(
    user_id     bigint(20) not null auto_increment comment '用户ID',
    union_id    varchar(128) default '' comment '微信UnionID',
    open_id     varchar(128) default '' comment '微信OpenID',
    phone       varchar(20)  default '' comment '手机号',
    nickname    varchar(50)  default '' comment '昵称',
    avatar      varchar(255) default '' comment '头像',
    gender      char(1)      default '0' comment '性别（0未知 1男 2女）',
    birthday    date         default null comment '生日',
    parent_id   bigint(20)   default null comment '家长用户ID（关联父账户）',
    password    varchar(100) default '' comment '密码（可选，手机号登录可为空）',
    status      char(1)      default '0' comment '状态（0正常 1禁用）',
    del_flag    char(1)      default '0' comment '删除标志（0存在 2删除）',
    login_ip    varchar(128) default '' comment '最后登录IP',
    login_date  datetime comment '最后登录时间',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (user_id),
    unique key uk_phone (phone),
    unique key uk_union_id (union_id),
    key idx_open_id (open_id)
) engine = innodb
  auto_increment = 1 comment = '前端用户表';

-- 初始化-前端用户数据
insert into edu_user
values (1, '', '', '13800138000', '测试家长', '', '1', null, '', '0', '0', '127.0.0.1', sysdate(), 'admin', sysdate(),
        '', null, '测试账号');


-- ----------------------------
-- 1、孩子信息表
-- ----------------------------
drop table if exists edu_child;
create table edu_child
(
    child_id    bigint(20)  not null auto_increment comment '孩子ID',
    user_id     bigint(20)  not null comment '所属用户ID',
    name        varchar(50) not null comment '孩子姓名',
    gender      char(1)      default '0' comment '性别（0男 1女）',
    birth_date  date         default null comment '出生日期',
    avatar      varchar(255) default '' comment '头像地址',
    personality varchar(100) default '' comment '性格特点',
    del_flag    char(1)      default '0' comment '删除标志（0存在 2删除）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (child_id),
    key idx_user_id (user_id)
) engine = innodb
  auto_increment = 1 comment = '孩子信息表';

-- 初始化-孩子信息表数据
insert into edu_child
values (1, 1, '小明', '0', '2022-01-15', '', '活泼好动', '0', 'admin', sysdate(), '', null, '测试数据');


-- ----------------------------
-- 2、故事记录表
-- ----------------------------
drop table if exists edu_story_record;
create table edu_story_record
(
    id          bigint(20)   not null auto_increment comment '故事ID',
    user_id     bigint(20)   not null comment '用户ID',
    child_id    bigint(20)   not null comment '孩子ID',
    age         int(3)       default 0 comment '孩子年龄',
    scene       varchar(50)  default '' comment '故事场景（睡前/吃饭/发脾气等）',
    goal        varchar(100) default '' comment '教育目标（学会分享/勇敢等）',
    title       varchar(255) not null comment '故事标题',
    content     text         not null comment '故事内容',
    moral       varchar(255) default '' comment '教育意义',
    model_type  varchar(50)  default 'openai' comment 'AI模型类型',
    tokens_used int(11)      default 0 comment '消耗token数',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id),
    key idx_user_child (user_id, child_id)
) engine = innodb
  auto_increment = 1 comment = '故事记录表';

-- 初始化-故事记录表数据
insert into edu_story_record
values (1, 1, 1, 4, '睡前', '勇敢', '勇敢的小兔子', '从前有一只小兔子，它很胆小。有一天...', '勇气是面对恐惧时依然向前走',
        'openai', 500, 'admin', sysdate(), '', null);


-- ----------------------------
-- 3、故事互动问题表
-- ----------------------------
drop table if exists edu_story_interaction;
create table edu_story_interaction
(
    id          bigint(20)   not null auto_increment comment '互动ID',
    story_id    bigint(20)   not null comment '故事ID',
    question    varchar(255) not null comment '互动问题',
    sort        int(4) default 1 comment '排序',
    create_time datetime comment '创建时间',
    primary key (id),
    key idx_story_id (story_id)
) engine = innodb
  auto_increment = 1 comment = '故事互动问题表';

-- 初始化-故事互动问题表数据
insert into edu_story_interaction
values (1, 1, '小兔子是怎么变得勇敢的？', 1, sysdate());
insert into edu_story_interaction
values (2, 1, '如果你遇到害怕的事情会怎么做？', 2, sysdate());


-- ----------------------------
-- 4、每日任务模板表
-- ----------------------------
drop table if exists edu_daily_task;
create table edu_daily_task
(
    id          bigint(20)   not null auto_increment comment '任务模板ID',
    age_min     int(3)       default 0 comment '适用年龄下限',
    age_max     int(3)       default 6 comment '适用年龄上限',
    theme       varchar(50)  default '' comment '任务主题（颜色/动物/情绪/语言）',
    title       varchar(255) not null comment '任务名称',
    goal        varchar(255) default '' comment '训练目标',
    steps       text         not null comment '操作步骤（JSON格式）',
    tips        varchar(255) default '' comment '家长注意事项',
    duration    varchar(50)  default '10分钟' comment '建议时长',
    status      char(1)      default '0' comment '状态（0正常 1停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
) engine = innodb
  auto_increment = 1 comment = '每日任务模板表';

-- 初始化-每日任务模板数据
insert into edu_daily_task
values (1, 2, 4, '颜色', '认识红色', '训练孩子对红色的认知',
        '[{\"step\":\"准备红色的物品\",\"order\":1},{\"step\":\"指着物品说\"这是红色\"\",\"order\":2},{\"step\":\"让孩子找红色\",\"order\":3}]',
        '选择孩子熟悉的物品', '5分钟', '0', 'admin', sysdate(), '', null, '认识颜色主题');
insert into edu_daily_task
values (2, 0, 2, '语言', '语言输入', '促进语言发育，多听多说',
        '[{\"step\":\"指认物品\",\"order\":1},{\"step\":\"重复说名称\",\"order\":2},{\"step\":\"鼓励模仿\",\"order\":3}]',
        '语速放慢，吐字清晰', '10分钟', '0', 'admin', sysdate(), '', null, '语言启蒙主题');


-- ----------------------------
-- 5、用户任务记录表
-- ----------------------------
drop table if exists edu_task_record;
create table edu_task_record
(
    id           bigint(20) not null auto_increment comment '记录ID',
    user_id      bigint(20) not null comment '用户ID',
    child_id     bigint(20) not null comment '孩子ID',
    task_id      bigint(20) not null comment '任务模板ID',
    task_title   varchar(255) default '' comment '任务名称',
    status       char(1)      default '0' comment '状态（0未完成 1已完成）',
    feedback     varchar(255) default '' comment '完成反馈',
    completed_at datetime comment '完成时间',
    record_date  date         default null comment '记录日期',
    create_by    varchar(64)  default '' comment '创建者',
    create_time  datetime comment '创建时间',
    update_by    varchar(64)  default '' comment '更新者',
    update_time  datetime comment '更新时间',
    primary key (id),
    key idx_user_child (user_id, child_id),
    key idx_record_date (record_date)
) engine = innodb
  auto_increment = 1 comment = '用户任务记录表';


-- ----------------------------
-- 6、成长记录表
-- ----------------------------
drop table if exists edu_growth_record;
create table edu_growth_record
(
    id          bigint(20)  not null auto_increment comment '记录ID',
    user_id     bigint(20)  not null comment '用户ID',
    child_id    bigint(20)  not null comment '孩子ID',
    type        varchar(50) not null comment '记录类型（sleep睡眠/diet饮食/learning学习/health健康）',
    value       varchar(255) default '' comment '记录值',
    unit        varchar(20)  default '' comment '单位（如：小时、次、个）',
    note        varchar(500) default '' comment '备注说明',
    record_date date        not null comment '记录日期',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id),
    key idx_child_date (child_id, record_date)
) engine = innodb
  auto_increment = 1 comment = '成长记录表';

-- 初始化-成长记录表数据
insert into edu_growth_record
values (1, 1, 1, 'sleep', '10', '小时', '睡得很香', '2026-04-01', 'admin', sysdate(), '', null);
insert into edu_growth_record
values (2, 1, 1, 'diet', '3', '次', '三餐正常', '2026-04-01', 'admin', sysdate(), '', null);


-- ----------------------------
-- 7、成长报告表
-- ----------------------------
drop table if exists edu_growth_report;
create table edu_growth_report
(
    id           bigint(20) not null auto_increment comment '报告ID',
    user_id      bigint(20) not null comment '用户ID',
    child_id     bigint(20) not null comment '孩子ID',
    period varchar (50) default '' comment '报告周期（如：2026年3月）',
    summary      text        default null comment '总体评价',
    strengths    text        default null comment '优点（JSON数组）',
    improvements text        default null comment '建议（JSON数组）',
    advice       text        default null comment '家长建议',
    report_date  date       not null comment '报告日期',
    create_by    varchar(64) default '' comment '创建者',
    create_time  datetime comment '创建时间',
    primary key (id),
    key idx_child_date (child_id, report_date)
) engine = innodb
  auto_increment = 1 comment = '成长报告表';


-- ----------------------------
-- 8、家长问题表
-- ----------------------------
drop table if exists edu_parent_question;
create table edu_parent_question
(
    id          bigint(20) not null auto_increment comment '问题ID',
    user_id     bigint(20) not null comment '用户ID',
    child_id    bigint(20) not null comment '孩子ID',
    question    text       not null comment '问题描述',
    status      char(1)     default '0' comment '状态（0待回答 1已回答）',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64) default '' comment '更新者',
    update_time datetime comment '更新时间',
    primary key (id),
    key idx_user_child (user_id, child_id)
) engine = innodb
  auto_increment = 1 comment = '家长问题表';


-- ----------------------------
-- 9、AI回答表
-- ----------------------------
drop table if exists edu_parent_answer;
create table edu_parent_answer
(
    id          bigint(20) not null auto_increment comment '回答ID',
    question_id bigint(20) not null comment '问题ID',
    analysis    text        default null comment '问题原因分析',
    solutions   text        default null comment '解决方案（JSON数组）',
    example     text        default null comment '沟通示例',
    model_type  varchar(50) default 'openai' comment 'AI模型类型',
    tokens_used int(11)     default 0 comment '消耗token数',
    create_by   varchar(64) default '' comment '创建者',
    create_time datetime comment '创建时间',
    primary key (id),
    key idx_question_id (question_id)
) engine = innodb
  auto_increment = 1 comment = 'AI回答表';


-- ----------------------------
-- 10、订阅表
-- ----------------------------
drop table if exists edu_subscription;
create table edu_subscription
(
    id          bigint(20)  not null auto_increment comment '订阅ID',
    user_id     bigint(20)  not null comment '用户ID',
    plan        varchar(50) not null comment '订阅计划（monthly月卡/yearly年卡）',
    status      char(1)      default '0' comment '状态（0未激活 1有效 2过期）',
    start_time  datetime    not null comment '开始时间',
    end_time    datetime    not null comment '结束时间',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id),
    key idx_user_id (user_id)
) engine = innodb
  auto_increment = 1 comment = '订阅表';


-- ----------------------------
-- 11、订阅计划配置表
-- ----------------------------
drop table if exists edu_subscription_plan;
create table edu_subscription_plan
(
    id            bigint(20)   not null auto_increment comment '计划ID',
    plan_code     varchar(50)  not null comment '计划代码（monthly/yearly）',
    plan_name     varchar(100) not null comment '计划名称（月卡/年卡）',
    price         decimal(10, 2) default 0.00 comment '价格',
    duration_days int(5)         default 0 comment '时长（天）',
    features      text           default null comment '功能权益（JSON数组）',
    status        char(1)        default '0' comment '状态（0正常 1停用）',
    sort          int(4)         default 1 comment '排序',
    create_by     varchar(64)    default '' comment '创建者',
    create_time   datetime comment '创建时间',
    update_by     varchar(64)    default '' comment '更新者',
    update_time   datetime comment '更新时间',
    remark        varchar(500)   default null comment '备注',
    primary key (id)
) engine = innodb
  auto_increment = 1 comment = '订阅计划配置表';

-- 初始化-订阅计划配置数据
insert into edu_subscription_plan
values (1, 'monthly', '月度会员', 19.00, 30, '["AI故事无限使用","每日早教任务","成长记录分析","家长助手"]', '0', 1,
        'admin', sysdate(), '', null, '月度订阅');
insert into edu_subscription_plan
values (2, 'yearly', '年度会员', 169.00, 365,
        '["AI故事无限使用","每日早教任务","成长记录分析","家长助手","优先体验新功能"]', '0', 2, 'admin', sysdate(), '',
        null, '年度订阅');


-- ----------------------------
-- 12、AI配置表
-- ----------------------------
drop table if exists edu_ai_config;
create table edu_ai_config
(
    id           bigint(20)   not null auto_increment comment '配置ID',
    config_key   varchar(100) not null comment '配置键',
    config_value varchar(500) default '' comment '配置值',
    config_type  varchar(50)  default 'openai' comment '模型类型',
    status       char(1)      default '0' comment '状态（0正常 1停用）',
    create_by    varchar(64)  default '' comment '创建者',
    create_time  datetime comment '创建时间',
    update_by    varchar(64)  default '' comment '更新者',
    update_time  datetime comment '更新时间',
    remark       varchar(500) default null comment '备注',
    primary key (id),
    unique key uk_config_key (config_key)
) engine = innodb
  auto_increment = 1 comment = 'AI配置表';

-- 初始化-AI配置数据
insert into edu_ai_config
values (1, 'ai.api-key', '', 'openai', '0', 'admin', sysdate(), '', null, 'API密钥');
insert into edu_ai_config
values (2, 'ai.model', 'gpt-4o-mini', 'openai', '0', 'admin', sysdate(), '', null, '模型名称');
insert into edu_ai_config
values (3, 'ai.timeout', '30', 'openai', '0', 'admin', sysdate(), '', null, '超时时间（秒）');


-- ----------------------------
-- 13、故事场景字典表
-- ----------------------------
drop table if exists edu_dict_scene;
create table edu_dict_scene
(
    id          bigint(20) not null auto_increment comment '字典ID',
    dict_label  varchar(100) default '' comment '字典标签',
    dict_value  varchar(100) default '' comment '字典键值',
    dict_sort   int(4)       default 1 comment '字典排序',
    status      char(1)      default '0' comment '状态（0正常 1停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
) engine = innodb
  auto_increment = 1 comment = '故事场景字典表';

-- 初始化-故事场景数据
insert into edu_dict_scene
values (1, '睡前', 'bedtime', 1, '0', 'admin', sysdate(), '', null, '睡前故事场景');
insert into edu_dict_scene
values (2, '吃饭', 'mealtime', 2, '0', 'admin', sysdate(), '', null, '用餐故事场景');
insert into edu_dict_scene
values (3, '发脾气', 'tantrum', 3, '0', 'admin', sysdate(), '', null, '情绪管理场景');
insert into edu_dict_scene
values (4, '起床', 'morning', 4, '0', 'admin', sysdate(), '', null, '起床故事场景');
insert into edu_dict_scene
values (5, '早教', 'education', 5, '0', 'admin', sysdate(), '', null, '早教故事场景');


-- ----------------------------
-- 14、教育目标字典表
-- ----------------------------
drop table if exists edu_dict_goal;
create table edu_dict_goal
(
    id          bigint(20) not null auto_increment comment '字典ID',
    dict_label  varchar(100) default '' comment '字典标签',
    dict_value  varchar(100) default '' comment '字典键值',
    dict_sort   int(4)       default 1 comment '字典排序',
    status      char(1)      default '0' comment '状态（0正常 1停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
) engine = innodb
  auto_increment = 1 comment = '教育目标字典表';

-- 初始化-教育目标数据
insert into edu_dict_goal
values (1, '学会分享', 'share', 1, '0', 'admin', sysdate(), '', null, '分享教育目标');
insert into edu_dict_goal
values (2, '勇敢', 'brave', 2, '0', 'admin', sysdate(), '', null, '勇气培养目标');
insert into edu_dict_goal
values (3, '不哭闹', 'calm', 3, '0', 'admin', sysdate(), '', null, '情绪管理目标');
insert into edu_dict_goal
values (4, '诚实', 'honest', 4, '0', 'admin', sysdate(), '', null, '品德教育目标');
insert into edu_dict_goal
values (5, '友爱', 'kind', 5, '0', 'admin', sysdate(), '', null, '社交能力目标');


-- ----------------------------
-- 15、任务主题字典表
-- ----------------------------
drop table if exists edu_dict_theme;
create table edu_dict_theme
(
    id          bigint(20) not null auto_increment comment '字典ID',
    dict_label  varchar(100) default '' comment '字典标签',
    dict_value  varchar(100) default '' comment '字典键值',
    dict_sort   int(4)       default 1 comment '字典排序',
    status      char(1)      default '0' comment '状态（0正常 1停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
) engine = innodb
  auto_increment = 1 comment = '任务主题字典表';

-- 初始化-任务主题数据
insert into edu_dict_theme
values (1, '颜色', 'color', 1, '0', 'admin', sysdate(), '', null, '颜色认知主题');
insert into edu_dict_theme
values (2, '动物', 'animal', 2, '0', 'admin', sysdate(), '', null, '动物认知主题');
insert into edu_dict_theme
values (3, '情绪', 'emotion', 3, '0', 'admin', sysdate(), '', null, '情绪管理主题');
insert into edu_dict_theme
values (4, '语言', 'language', 4, '0', 'admin', sysdate(), '', null, '语言启蒙主题');
insert into edu_dict_theme
values (5, '数字', 'number', 5, '0', 'admin', sysdate(), '', null, '数学启蒙主题');


-- ----------------------------
-- 16、成长记录类型字典表
-- ----------------------------
drop table if exists edu_dict_growth_type;
create table edu_dict_growth_type
(
    id          bigint(20) not null auto_increment comment '字典ID',
    dict_label  varchar(100) default '' comment '字典标签',
    dict_value  varchar(100) default '' comment '字典键值',
    dict_unit   varchar(50)  default '' comment '默认单位',
    dict_sort   int(4)       default 1 comment '字典排序',
    status      char(1)      default '0' comment '状态（0正常 1停用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id)
) engine = innodb
  auto_increment = 1 comment = '成长记录类型字典表';

-- 初始化-成长记录类型数据
insert into edu_dict_growth_type
values (1, '睡眠', 'sleep', '小时', 1, '0', 'admin', sysdate(), '', null, '睡眠记录');
insert into edu_dict_growth_type
values (2, '饮食', 'diet', '次', 2, '0', 'admin', sysdate(), '', null, '饮食记录');
insert into edu_dict_growth_type
values (3, '学习', 'learning', '分钟', 3, '0', 'admin', sysdate(), '', null, '学习记录');
insert into edu_dict_growth_type
values (4, '健康', 'health', '', 4, '0', 'admin', sysdate(), '', null, '健康记录');


-- ----------------------------
-- 17、每日任务分配表（每日自动生成的任务）
-- ----------------------------
drop table if exists edu_task_daily;
create table edu_task_daily
(
    id           bigint(20)   not null auto_increment comment '分配ID',
    user_id      bigint(20)   not null comment '用户ID',
    child_id     bigint(20)   not null comment '孩子ID',
    task_id      bigint(20)   not null comment '任务模板ID',
    title        varchar(255) not null comment '任务名称',
    goal         varchar(255) default '' comment '训练目标',
    steps        text         default null comment '操作步骤',
    tips         varchar(255) default '' comment '注意事项',
    duration     varchar(50)  default '10分钟' comment '建议时长',
    task_date    date         not null comment '任务日期',
    status       char(1)      default '0' comment '状态（0未完成 1已完成）',
    completed_at datetime comment '完成时间',
    create_by    varchar(64)  default '' comment '创建者',
    create_time  datetime comment '创建时间',
    update_by    varchar(64)  default '' comment '更新者',
    update_time  datetime comment '更新时间',
    primary key (id),
    key idx_user_child_date (user_id, child_id, task_date)
) engine = innodb
  auto_increment = 1 comment = '每日任务分配表';


-- ----------------------------
-- 18、收藏表
-- ----------------------------
drop table if exists edu_favorite;
create table edu_favorite
(
    id          bigint(20)  not null auto_increment comment '收藏ID',
    user_id     bigint(20)  not null comment '用户ID',
    child_id    bigint(20)  not null comment '孩子ID',
    type        varchar(50) not null comment '收藏类型（story故事/task任务）',
    source_id   bigint(20)  not null comment '来源ID（故事ID或任务ID）',
    title       varchar(255) default '' comment '收藏标题',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    primary key (id),
    key idx_user_id (user_id),
    unique key uk_user_source (user_id, type, source_id)
) engine = innodb
  auto_increment = 1 comment = '收藏表';


-- ----------------------------
-- 19、AI知识库文档表
-- ----------------------------
drop table if exists edu_ai_knowledge_doc;
create table edu_ai_knowledge_doc
(
    id          bigint(20)   not null auto_increment comment '文档ID',
    title       varchar(255) not null comment '文档标题',
    content     text         not null comment '文档内容',
    category    varchar(100) default null comment '分类',
    status      char(1)      default '1' comment '状态（0禁用 1启用）',
    create_by   varchar(64)  default '' comment '创建者',
    create_time datetime comment '创建时间',
    update_by   varchar(64)  default '' comment '更新者',
    update_time datetime comment '更新时间',
    remark      varchar(500) default null comment '备注',
    primary key (id),
    key idx_status (status),
    key idx_category (category)
) engine = innodb
  auto_increment = 1 comment = 'AI知识库文档表';


-- ----------------------------
-- 20、AI用户长期记忆表
-- ----------------------------
drop table if exists edu_ai_user_memory;
create table edu_ai_user_memory
(
    id           bigint(20)   not null auto_increment comment '记忆ID',
    user_id      bigint(20)   not null comment '用户ID',
    memory_key   varchar(100) not null comment '记忆键（如child_age, preference）',
    memory_value text         not null comment '记忆值',
    source       varchar(50) default 'conversation' comment '来源（conversation/manual/system）',
    create_by    varchar(64) default '' comment '创建者',
    create_time  datetime comment '创建时间',
    update_by    varchar(64) default '' comment '更新者',
    update_time  datetime comment '更新时间',
    primary key (id),
    unique key uk_user_key (user_id, memory_key),
    key idx_user_id (user_id)
) engine = innodb
  auto_increment = 1 comment = 'AI用户长期记忆表';


-- ----------------------------
-- 21、AI对话摘要表
-- ----------------------------
drop table if exists edu_ai_conversation_summary;
create table edu_ai_conversation_summary
(
    id              bigint(20)  not null auto_increment comment '摘要ID',
    conversation_id varchar(64) not null comment '会话ID',
    user_id         bigint(20)  not null comment '用户ID',
    summary         text        not null comment '对话摘要内容',
    message_count   int(11)     default 0 comment '已摘要的消息数',
    create_by       varchar(64) default '' comment '创建者',
    create_time     datetime comment '创建时间',
    update_by       varchar(64) default '' comment '更新者',
    update_time     datetime comment '更新时间',
    primary key (id),
    unique key uk_conversation (conversation_id),
    key idx_user_id (user_id)
) engine = innodb
  auto_increment = 1 comment = 'AI对话摘要表';


-- ----------------------------
-- 菜单SQL（教育模块菜单）
-- ----------------------------

-- ----------------------------
-- 教育模块菜单数据
-- ----------------------------
-- 一级菜单：早教中心
insert into sys_menu
values ('2000', '早教中心', '0', '5', 'education', null, '', '', 1, 0, 'M', '0', '0', '', 'education', 'admin',
        sysdate(), '', null, '早教中心目录');

-- 二级菜单
insert into sys_menu
values ('2001', '孩子管理', '2000', '1', 'child', 'education/child/index', '', '', 1, 0, 'C', '0', '0',
        'education:child:list', 'peoples', 'admin', sysdate(), '', null, '孩子管理菜单');
insert into sys_menu
values ('2002', 'AI故事', '2000', '2', 'story', 'education/story/index', '', '', 1, 0, 'C', '0', '0',
        'education:story:list', 'story', 'admin', sysdate(), '', null, 'AI故事菜单');
insert into sys_menu
values ('2003', '每日任务', '2000', '3', 'task', 'education/task/index', '', '', 1, 0, 'C', '0', '0',
        'education:task:list', 'task', 'admin', sysdate(), '', null, '每日任务菜单');
insert into sys_menu
values ('2004', '成长记录', '2000', '4', 'growth', 'education/growth/index', '', '', 1, 0, 'C', '0', '0',
        'education:growth:list', 'growth', 'admin', sysdate(), '', null, '成长记录菜单');
insert into sys_menu
values ('2005', '家长助手', '2000', '5', 'parent', 'education/parent/index', '', '', 1, 0, 'C', '0', '0',
        'education:parent:list', 'parent', 'admin', sysdate(), '', null, '家长助手菜单');
insert into sys_menu
values ('2006', '订阅管理', '2000', '6', 'subscription', 'education/subscription/index', '', '', 1, 0, 'C', '0', '0',
        'education:subscription:list', 'vip', 'admin', sysdate(), '', null, '订阅管理菜单');

-- 二级菜单：系统设置
insert into sys_menu
values ('2007', '早教设置', '1', '10', 'eduConfig', null, '', '', 1, 0, 'M', '0', '0', '', 'config', 'admin', sysdate(),
        '', null, '早教设置目录');
insert into sys_menu
values ('2008', 'AI配置', '2007', '1', 'aiConfig', 'system/aiConfig/index', '', '', 1, 0, 'C', '0', '0',
        'education:aiConfig:list', 'ai', 'admin', sysdate(), '', null, 'AI配置菜单');
insert into sys_menu
values ('2009', '任务模板', '2007', '2', 'taskTemplate', 'system/taskTemplate/index', '', '', 1, 0, 'C', '0', '0',
        'education:taskTemplate:list', 'template', 'admin', sysdate(), '', null, '任务模板菜单');
insert into sys_menu
values ('2010', '订阅计划', '2007', '3', 'subscriptionPlan', 'system/subscriptionPlan/index', '', '', 1, 0, 'C', '0',
        '0', 'education:subscriptionPlan:list', 'plan', 'admin', sysdate(), '', null, '订阅计划菜单');

-- 按钮权限
insert into sys_menu
values ('3000', '孩子查询', '2001', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'education:child:query', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3001', '孩子新增', '2001', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'education:child:add', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3002', '孩子修改', '2001', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'education:child:edit', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3003', '孩子删除', '2001', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'education:child:remove', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3004', '孩子导出', '2001', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'education:child:export', '#', 'admin',
        sysdate(), '', null, '');

insert into sys_menu
values ('3010', '故事查询', '2002', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'education:story:query', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3011', '故事生成', '2002', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'education:story:generate', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3012', '故事收藏', '2002', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'education:story:favorite', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3013', '故事删除', '2002', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'education:story:remove', '#', 'admin',
        sysdate(), '', null, '');

insert into sys_menu
values ('3020', '任务查询', '2003', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'education:task:query', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3021', '任务完成', '2003', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'education:task:complete', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3022', '任务导出', '2003', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'education:task:export', '#', 'admin',
        sysdate(), '', null, '');

insert into sys_menu
values ('3030', '成长查询', '2004', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'education:growth:query', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3031', '成长新增', '2004', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'education:growth:add', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3032', '成长修改', '2004', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'education:growth:edit', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3033', '成长删除', '2004', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'education:growth:remove', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3034', '生成报告', '2004', '5', '', '', '', '', 1, 0, 'F', '0', '0', 'education:growth:report', '#', 'admin',
        sysdate(), '', null, '');

insert into sys_menu
values ('3040', '家长查询', '2005', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'education:parent:query', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3041', '家长提问', '2005', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'education:parent:ask', '#', 'admin',
        sysdate(), '', null, '');
insert into sys_menu
values ('3042', '家长删除', '2005', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'education:parent:remove', '#', 'admin',
        sysdate(), '', null, '');

insert into sys_menu
values ('3050', '订阅查询', '2006', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'education:subscription:query', '#',
        'admin', sysdate(), '', null, '');
insert into sys_menu
values ('3051', '订阅新增', '2006', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'education:subscription:add', '#',
        'admin', sysdate(), '', null, '');
insert into sys_menu
values ('3052', '订阅修改', '2006', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'education:subscription:edit', '#',
        'admin', sysdate(), '', null, '');

insert into sys_menu
values ('3060', 'AI配置查询', '2008', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'education:aiConfig:query', '#',
        'admin', sysdate(), '', null, '');
insert into sys_menu
values ('3061', 'AI配置修改', '2008', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'education:aiConfig:edit', '#', 'admin',
        sysdate(), '', null, '');

insert into sys_menu
values ('3070', '模板查询', '2009', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'education:taskTemplate:query', '#',
        'admin', sysdate(), '', null, '');
insert into sys_menu
values ('3071', '模板新增', '2009', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'education:taskTemplate:add', '#',
        'admin', sysdate(), '', null, '');
insert into sys_menu
values ('3072', '模板修改', '2009', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'education:taskTemplate:edit', '#',
        'admin', sysdate(), '', null, '');
insert into sys_menu
values ('3073', '模板删除', '2009', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'education:taskTemplate:remove', '#',
        'admin', sysdate(), '', null, '');

insert into sys_menu
values ('3080', '计划查询', '2010', '1', '', '', '', '', 1, 0, 'F', '0', '0', 'education:subscriptionPlan:query', '#',
        'admin', sysdate(), '', null, '');
insert into sys_menu
values ('3081', '计划新增', '2010', '2', '', '', '', '', 1, 0, 'F', '0', '0', 'education:subscriptionPlan:add', '#',
        'admin', sysdate(), '', null, '');
insert into sys_menu
values ('3082', '计划修改', '2010', '3', '', '', '', '', 1, 0, 'F', '0', '0', 'education:subscriptionPlan:edit', '#',
        'admin', sysdate(), '', null, '');
insert into sys_menu
values ('3083', '计划删除', '2010', '4', '', '', '', '', 1, 0, 'F', '0', '0', 'education:subscriptionPlan:remove', '#',
        'admin', sysdate(), '', null, '');

-- 给超级管理员角色添加教育模块菜单权限
insert into sys_role_menu
values ('1', '2000');
insert into sys_role_menu
values ('1', '2001');
insert into sys_role_menu
values ('1', '2002');
insert into sys_role_menu
values ('1', '2003');
insert into sys_role_menu
values ('1', '2004');
insert into sys_role_menu
values ('1', '2005');
insert into sys_role_menu
values ('1', '2006');
insert into sys_role_menu
values ('1', '2007');
insert into sys_role_menu
values ('1', '2008');
insert into sys_role_menu
values ('1', '2009');
insert into sys_role_menu
values ('1', '2010');
insert into sys_role_menu
values ('1', '3000');
insert into sys_role_menu
values ('1', '3001');
insert into sys_role_menu
values ('1', '3002');
insert into sys_role_menu
values ('1', '3003');
insert into sys_role_menu
values ('1', '3004');
insert into sys_role_menu
values ('1', '3010');
insert into sys_role_menu
values ('1', '3011');
insert into sys_role_menu
values ('1', '3012');
insert into sys_role_menu
values ('1', '3013');
insert into sys_role_menu
values ('1', '3020');
insert into sys_role_menu
values ('1', '3021');
insert into sys_role_menu
values ('1', '3022');
insert into sys_role_menu
values ('1', '3030');
insert into sys_role_menu
values ('1', '3031');
insert into sys_role_menu
values ('1', '3032');
insert into sys_role_menu
values ('1', '3033');
insert into sys_role_menu
values ('1', '3034');
insert into sys_role_menu
values ('1', '3040');
insert into sys_role_menu
values ('1', '3041');
insert into sys_role_menu
values ('1', '3042');
insert into sys_role_menu
values ('1', '3050');
insert into sys_role_menu
values ('1', '3051');
insert into sys_role_menu
values ('1', '3052');
insert into sys_role_menu
values ('1', '3060');
insert into sys_role_menu
values ('1', '3061');
insert into sys_role_menu
values ('1', '3070');
insert into sys_role_menu
values ('1', '3071');
insert into sys_role_menu
values ('1', '3072');
insert into sys_role_menu
values ('1', '3073');
insert into sys_role_menu
values ('1', '3080');
insert into sys_role_menu
values ('1', '3081');
insert into sys_role_menu
values ('1', '3082');
insert into sys_role_menu
values ('1', '3083');


-- ----------------------------
-- 字典类型数据
-- ----------------------------
insert into sys_dict_type
values (100, '故事场景', 'edu_story_scene', '0', 'admin', sysdate(), '', null, '故事场景列表');
insert into sys_dict_type
values (101, '教育目标', 'edu_education_goal', '0', 'admin', sysdate(), '', null, '教育目标列表');
insert into sys_dict_type
values (102, '任务主题', 'edu_task_theme', '0', 'admin', sysdate(), '', null, '任务主题列表');
insert into sys_dict_type
values (103, '成长记录类型', 'edu_growth_type', '0', 'admin', sysdate(), '', null, '成长记录类型列表');
insert into sys_dict_type
values (104, '订阅计划', 'edu_subscription_plan', '0', 'admin', sysdate(), '', null, '订阅计划列表');
insert into sys_dict_type
values (105, '订阅状态', 'edu_subscription_status', '0', 'admin', sysdate(), '', null, '订阅状态列表');
insert into sys_dict_type
values (106, '订阅支付方式', 'edu_pay_type', '0', 'admin', sysdate(), '', null, '支付方式列表');

-- 字典数据
insert into sys_dict_data
values (200, 1, '睡前', 'bedtime', 'edu_story_scene', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null,
        '睡前故事');
insert into sys_dict_data
values (201, 2, '吃饭', 'mealtime', 'edu_story_scene', '', 'success', 'N', '0', 'admin', sysdate(), '', null,
        '用餐故事');
insert into sys_dict_data
values (202, 3, '发脾气', 'tantrum', 'edu_story_scene', '', 'warning', 'N', '0', 'admin', sysdate(), '', null,
        '情绪管理');
insert into sys_dict_data
values (203, 4, '起床', 'morning', 'edu_story_scene', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '起床故事');
insert into sys_dict_data
values (204, 5, '早教', 'education', 'edu_story_scene', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '早教故事');

insert into sys_dict_data
values (210, 1, '学会分享', 'share', 'edu_education_goal', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null,
        '分享');
insert into sys_dict_data
values (211, 2, '勇敢', 'brave', 'edu_education_goal', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '勇气');
insert into sys_dict_data
values (212, 3, '不哭闹', 'calm', 'edu_education_goal', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '情绪');
insert into sys_dict_data
values (213, 4, '诚实', 'honest', 'edu_education_goal', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '品德');
insert into sys_dict_data
values (214, 5, '友爱', 'kind', 'edu_education_goal', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '社交');

insert into sys_dict_data
values (220, 1, '颜色', 'color', 'edu_task_theme', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '颜色');
insert into sys_dict_data
values (221, 2, '动物', 'animal', 'edu_task_theme', '', 'success', 'N', '0', 'admin', sysdate(), '', null, '动物');
insert into sys_dict_data
values (222, 3, '情绪', 'emotion', 'edu_task_theme', '', 'warning', 'N', '0', 'admin', sysdate(), '', null, '情绪');
insert into sys_dict_data
values (223, 4, '语言', 'language', 'edu_task_theme', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '语言');
insert into sys_dict_data
values (224, 5, '数字', 'number', 'edu_task_theme', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '数字');

insert into sys_dict_data
values (230, 1, '睡眠', 'sleep', 'edu_growth_type', '小时', 'primary', 'Y', '0', 'admin', sysdate(), '', null, '睡眠');
insert into sys_dict_data
values (231, 2, '饮食', 'diet', 'edu_growth_type', '次', 'success', 'N', '0', 'admin', sysdate(), '', null, '饮食');
insert into sys_dict_data
values (232, 3, '学习', 'learning', 'edu_growth_type', '分钟', 'warning', 'N', '0', 'admin', sysdate(), '', null,
        '学习');
insert into sys_dict_data
values (233, 4, '健康', 'health', 'edu_growth_type', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '健康');

insert into sys_dict_data
values (240, 1, '月卡', 'monthly', 'edu_subscription_plan', '', 'primary', 'Y', '0', 'admin', sysdate(), '', null,
        '月度订阅');
insert into sys_dict_data
values (241, 2, '年卡', 'yearly', 'edu_subscription_plan', '', 'success', 'N', '0', 'admin', sysdate(), '', null,
        '年度订阅');

insert into sys_dict_data
values (250, 1, '未激活', '0', 'edu_subscription_status', '', 'info', 'N', '0', 'admin', sysdate(), '', null, '未激活');
insert into sys_dict_data
values (251, 2, '有效', '1', 'edu_subscription_status', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '有效');
insert into sys_dict_data
values (252, 3, '过期', '2', 'edu_subscription_status', '', 'danger', 'N', '0', 'admin', sysdate(), '', null, '过期');

insert into sys_dict_data
values (260, 1, '微信支付', 'wechat', 'edu_pay_type', '', 'success', 'Y', '0', 'admin', sysdate(), '', null, '微信');
insert into sys_dict_data
values (261, 2, '支付宝', 'alipay', 'edu_pay_type', '', 'primary', 'N', '0', 'admin', sysdate(), '', null, '支付宝');


-- ----------------------------
-- 定时任务配置
-- ----------------------------
insert into sys_job
values (10, '每日早教任务生成', 'EDUCATION', 'eduTask.generateDailyTask', '0 0 8 * * ?', '3', '1', '1', 'admin',
        sysdate(), '', null, '每天早上8点为用户生成当日早教任务');
insert into sys_job
values (11, '订阅过期检查', 'EDUCATION', 'eduTask.checkSubscription', '0 0 0 * * ?', '3', '1', '1', 'admin', sysdate(),
        '', null, '每天检查订阅过期状态');
