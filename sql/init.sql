-- =====================================================
-- 大学生心理健康咨询系统 - 数据库初始化脚本
-- Database: mental_health
-- =====================================================

CREATE DATABASE IF NOT EXISTS mental_health DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE mental_health;

-- 1. 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `gender` TINYINT DEFAULT 0 COMMENT '性别 0-未知 1-男 2-女',
    `age` INT DEFAULT NULL COMMENT '年龄',
    `role` VARCHAR(20) NOT NULL DEFAULT 'STUDENT' COMMENT '角色 STUDENT/COUNSELOR/ADMIN',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-正常',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 咨询师信息表
DROP TABLE IF EXISTS `counselor`;
CREATE TABLE `counselor` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '咨询师ID',
    `user_id` BIGINT NOT NULL COMMENT '关联用户ID',
    `title` VARCHAR(50) DEFAULT NULL COMMENT '职称',
    `specialty` VARCHAR(255) DEFAULT NULL COMMENT '擅长领域',
    `description` TEXT COMMENT '个人简介',
    `certification` VARCHAR(255) DEFAULT NULL COMMENT '资质证书',
    `work_years` INT DEFAULT 0 COMMENT '从业年限',
    `max_daily_slots` INT DEFAULT 5 COMMENT '每日最大可约人数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='咨询师信息表';

-- 3. 心理测评量表表
DROP TABLE IF EXISTS `assessment_scale`;
CREATE TABLE `assessment_scale` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '量表ID',
    `name` VARCHAR(100) NOT NULL COMMENT '量表名称',
    `description` TEXT COMMENT '量表描述',
    `type` VARCHAR(50) DEFAULT NULL COMMENT '类型 ANXIETY/DEPRESSION/STRESS',
    `total_score` INT DEFAULT 100 COMMENT '满分',
    `question_count` INT DEFAULT 0 COMMENT '题目数量',
    `status` TINYINT DEFAULT 1 COMMENT '状态 0-停用 1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心理测评量表表';

-- 4. 测评题目表
DROP TABLE IF EXISTS `assessment_question`;
CREATE TABLE `assessment_question` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '题目ID',
    `scale_id` BIGINT NOT NULL COMMENT '所属量表ID',
    `question_text` TEXT NOT NULL COMMENT '题目内容',
    `question_type` VARCHAR(20) DEFAULT 'SINGLE' COMMENT '题型',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `options` JSON COMMENT '选项JSON',
    PRIMARY KEY (`id`),
    KEY `idx_scale_id` (`scale_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测评题目表';

-- 5. 测评记录表
DROP TABLE IF EXISTS `assessment_record`;
CREATE TABLE `assessment_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `scale_id` BIGINT NOT NULL COMMENT '量表ID',
    `total_score` INT DEFAULT 0 COMMENT '总分',
    `result_level` VARCHAR(50) DEFAULT NULL COMMENT '结果等级 NORMAL/MILD/MODERATE/SEVERE',
    `result_detail` TEXT COMMENT '详细结果分析',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_scale_id` (`scale_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测评记录表';

-- 6. 测评作答详情表
DROP TABLE IF EXISTS `assessment_answer`;
CREATE TABLE `assessment_answer` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '作答ID',
    `record_id` BIGINT NOT NULL COMMENT '测评记录ID',
    `question_id` BIGINT NOT NULL COMMENT '题目ID',
    `selected_option` VARCHAR(10) DEFAULT NULL COMMENT '选中选项',
    `score` INT DEFAULT 0 COMMENT '得分',
    PRIMARY KEY (`id`),
    KEY `idx_record_id` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测评作答详情表';

-- 7. 咨询预约表
DROP TABLE IF EXISTS `appointment`;
CREATE TABLE `appointment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '预约ID',
    `student_id` BIGINT NOT NULL COMMENT '学生用户ID',
    `counselor_id` BIGINT NOT NULL COMMENT '咨询师ID',
    `appointment_date` DATE NOT NULL COMMENT '预约日期',
    `time_slot` VARCHAR(20) NOT NULL COMMENT '时间段',
    `type` VARCHAR(20) DEFAULT 'FACE' COMMENT '咨询方式 FACE/ONLINE',
    `reason` TEXT COMMENT '咨询原因',
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT '状态 PENDING/CONFIRMED/COMPLETED/CANCELLED',
    `remark` TEXT COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_student_id` (`student_id`),
    KEY `idx_counselor_id` (`counselor_id`),
    KEY `idx_appointment_date` (`appointment_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='咨询预约表';

-- 8. 咨询记录表
DROP TABLE IF EXISTS `consultation_record`;
CREATE TABLE `consultation_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `appointment_id` BIGINT DEFAULT NULL,
    `student_id` BIGINT NOT NULL,
    `counselor_id` BIGINT NOT NULL,
    `content` TEXT COMMENT '咨询内容记录',
    `evaluation` TEXT COMMENT '咨询师评估',
    `suggestion` TEXT COMMENT '咨询建议',
    `risk_level` VARCHAR(20) DEFAULT 'LOW' COMMENT '风险等级 LOW/MID/HIGH',
    `start_time` DATETIME DEFAULT NULL,
    `end_time` DATETIME DEFAULT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_appointment_id` (`appointment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='咨询记录表';

-- 9. 在线消息表
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID',
    `session_id` BIGINT NOT NULL COMMENT '会话ID',
    `sender_id` BIGINT NOT NULL COMMENT '发送者ID',
    `receiver_id` BIGINT NOT NULL COMMENT '接收者ID',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `msg_type` VARCHAR(20) DEFAULT 'TEXT' COMMENT '消息类型 TEXT/IMAGE',
    `is_read` TINYINT DEFAULT 0 COMMENT '是否已读',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_session_id` (`session_id`),
    KEY `idx_sender_id` (`sender_id`),
    KEY `idx_receiver_id` (`receiver_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='在线消息表';

-- 10. 会话表
DROP TABLE IF EXISTS `chat_session`;
CREATE TABLE `chat_session` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `student_id` BIGINT NOT NULL,
    `counselor_id` BIGINT NOT NULL,
    `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'ACTIVE/CLOSED',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_student_counselor` (`student_id`, `counselor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会话表';

-- 11. 心理健康文章表
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    `title` VARCHAR(200) NOT NULL COMMENT '文章标题',
    `content` LONGTEXT COMMENT '文章内容(富文本)',
    `summary` VARCHAR(500) DEFAULT NULL COMMENT '摘要',
    `cover_image` VARCHAR(255) DEFAULT NULL COMMENT '封面图',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '分类',
    `author_id` BIGINT DEFAULT NULL COMMENT '作者ID',
    `view_count` INT DEFAULT 0 COMMENT '浏览次数',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `status` VARCHAR(20) DEFAULT 'PUBLISHED' COMMENT 'DRAFT/PUBLISHED',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='心理健康文章表';

-- 12. 文章评论表
DROP TABLE IF EXISTS `article_comment`;
CREATE TABLE `article_comment` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `article_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `content` TEXT NOT NULL,
    `parent_id` BIGINT DEFAULT NULL COMMENT '父评论ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章评论表';

-- 13. 用户情绪打卡表
DROP TABLE IF EXISTS `mood_checkin`;
CREATE TABLE `mood_checkin` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `mood_type` VARCHAR(20) NOT NULL COMMENT 'HAPPY/CALM/SAD/ANXIOUS/ANGRY',
    `mood_score` INT DEFAULT 5 COMMENT '情绪评分1-10',
    `note` VARCHAR(500) DEFAULT NULL,
    `checkin_date` DATE NOT NULL,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_checkin_date` (`checkin_date`),
    UNIQUE KEY `uk_user_date` (`user_id`, `checkin_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户情绪打卡表';

-- 14. 系统日志表
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT DEFAULT NULL,
    `username` VARCHAR(50) DEFAULT NULL,
    `operation` VARCHAR(100) DEFAULT NULL,
    `method` VARCHAR(255) DEFAULT NULL,
    `params` TEXT,
    `ip` VARCHAR(50) DEFAULT NULL,
    `duration` BIGINT DEFAULT 0,
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';

-- 15. 文件上传记录表
DROP TABLE IF EXISTS `file_upload`;
CREATE TABLE `file_upload` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `file_name` VARCHAR(255) NOT NULL,
    `file_path` VARCHAR(500) NOT NULL,
    `file_size` BIGINT DEFAULT 0,
    `file_type` VARCHAR(100) DEFAULT NULL,
    `uploader_id` BIGINT DEFAULT NULL,
    `business_type` VARCHAR(50) DEFAULT NULL COMMENT 'AVATAR/ARTICLE',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件上传记录表';

-- ==== 初始数据 ====
-- 管理员 admin/admin123
INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `email`, `role`, `status`) VALUES
('admin', '$2a$10$ONIb99ug9639lkw8iVyUOePL5f8OQfsZrZEgRGyS9n7NYV0dmWekC', '系统管理员', '13800000000', 'admin@mentalhealth.com', 'ADMIN', 1);

-- 咨询师 密码123456
INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `email`, `role`, `status`) VALUES
('counselor1', '$2a$10$vYDSamwRadWVEe2A5QrbjeuCjhBzPh0yihKa8bcWCJHtlnz9hZSqS', '张心理', '13800000001', 'zhang@mentalhealth.com', 'COUNSELOR', 1),
('counselor2', '$2a$10$vYDSamwRadWVEe2A5QrbjeuCjhBzPh0yihKa8bcWCJHtlnz9hZSqS', '李咨询', '13800000002', 'li@mentalhealth.com', 'COUNSELOR', 1);

INSERT INTO `counselor` (`user_id`, `title`, `specialty`, `description`, `certification`, `work_years`, `max_daily_slots`) VALUES
(2, '高级心理咨询师', '情绪管理,焦虑缓解,人际关系', '10年心理咨询经验，擅长青少年心理健康辅导', '国家二级心理咨询师', 10, 6),
(3, '中级心理咨询师', '学业压力,职业规划,自我认知', '5年高校心理咨询工作经验', '国家三级心理咨询师', 5, 5);

INSERT INTO `assessment_scale` (`name`, `description`, `type`, `total_score`, `question_count`, `status`) VALUES
('焦虑自评量表(SAS)', '焦虑自评量表用于评出焦虑病人的主观感受，由Zung于1971年编制', 'ANXIETY', 80, 20, 1),
('抑郁自评量表(SDS)', '抑郁自评量表能直观地反映抑郁病人的主观感受，由Zung于1965年编制', 'DEPRESSION', 80, 20, 1),
('大学生压力问卷', '专门针对大学生群体设计的压力评估问卷', 'STRESS', 100, 20, 1);

INSERT INTO `assessment_question` (`scale_id`, `question_text`, `question_type`, `sort_order`, `options`) VALUES
(1, '我觉得比平常容易紧张和着急', 'SINGLE', 1, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我无缘无故地感到害怕', 'SINGLE', 2, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我容易心里烦乱或觉得惊恐', 'SINGLE', 3, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我觉得我可能将要发疯', 'SINGLE', 4, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我觉得一切都很好，也不会发生什么不幸', 'SINGLE', 5, '[{"label":"D","text":"没有或很少时间","score":4},{"label":"C","text":"少部分时间","score":3},{"label":"B","text":"相当多时间","score":2},{"label":"A","text":"绝大部分或全部时间","score":1}]');

INSERT INTO `article` (`title`, `content`, `summary`, `category`, `author_id`, `status`, `view_count`, `like_count`) VALUES
('大学生如何应对考试焦虑', '<h2>什么是考试焦虑</h2><p>考试焦虑是指因考试压力过大而引发的一系列生理和心理上的不良反应，包括心慌、失眠、注意力不集中等。</p><h2>科学应对策略</h2><p><strong>1. 认知调整：</strong>改变对考试的不合理认知，考试只是检验学习效果的一种手段。</p><p><strong>2. 放松训练：</strong>每天进行深呼吸练习和渐进性肌肉放松，缓解身体紧张。</p><p><strong>3. 时间管理：</strong>制定合理的复习计划，避免考前突击。</p>', '本文介绍了大学生考试焦虑的成因及科学应对方法', 'EMOTION', 1, 'PUBLISHED', 256, 48),
('建立良好宿舍人际关系的五个建议', '<h2>宿舍关系的重要性</h2><p>大学宿舍是同学们生活的主要场所，良好的宿舍关系有助于心理健康和学业发展。</p><h2>五个实用建议</h2><p><strong>建议一：学会倾听与尊重</strong> - 每个人都有不同的生活习惯，学会包容。</p><p><strong>建议二：明确边界</strong> - 做好自己的事情，不随意干涉他人。</p>', '宿舍人际关系是大学生常见的困扰，本文提供五个实用建议', 'RELATION', 2, 'PUBLISHED', 189, 35),
('认识抑郁症：不只是"心情不好"', '<h2>抑郁症的常见误区</h2><p>很多人认为抑郁症就是"想太多"、"太脆弱"，这是一种误解。抑郁症是真实存在的疾病。</p><h2>典型症状</h2><p>1. 持续的情绪低落<br>2. 兴趣减退<br>3. 睡眠障碍<br>4. 食欲改变<br>5. 注意力难以集中</p><h2>如何寻求帮助</h2><p>如果你或身边的人出现上述症状，请及时寻求专业心理咨询师的帮助。</p>', '科普抑郁症相关知识，帮助大家正确认识和面对心理健康问题', 'DEPRESSION', 2, 'PUBLISHED', 312, 67);

-- 情绪打卡示例数据（用于数据看板）
INSERT INTO `mood_checkin` (`user_id`, `mood_type`, `mood_score`, `note`, `checkin_date`) VALUES
(2, 'HAPPY', 8, '今天咨询了几位同学，很有成就感', '2026-05-22'),
(2, 'CALM', 6, '日常工作，一切平稳', '2026-05-23'),
(2, 'HAPPY', 9, '收到了学生的感谢信', '2026-05-24'),
(2, 'ANXIOUS', 4, '遇到一个比较棘手的案例', '2026-05-25'),
(2, 'CALM', 7, '阅读了一本专业书籍', '2026-05-26'),
(2, 'HAPPY', 8, '小组讨论收获很多', '2026-05-27'),
(2, 'CALM', 7, '整理咨询案例', '2026-05-28');
