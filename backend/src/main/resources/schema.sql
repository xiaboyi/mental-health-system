-- H2 Database Schema for 大学生心理健康咨询系统

CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `real_name` VARCHAR(50) DEFAULT NULL,
    `phone` VARCHAR(20) DEFAULT NULL,
    `email` VARCHAR(100) DEFAULT NULL,
    `avatar` VARCHAR(255) DEFAULT NULL,
    `gender` TINYINT DEFAULT 0,
    `age` INT DEFAULT NULL,
    `role` VARCHAR(20) NOT NULL DEFAULT 'STUDENT',
    `status` TINYINT NOT NULL DEFAULT 1,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `deleted` TINYINT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS `counselor` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `title` VARCHAR(50) DEFAULT NULL,
    `specialty` VARCHAR(255) DEFAULT NULL,
    `description` TEXT,
    `certification` VARCHAR(255) DEFAULT NULL,
    `work_years` INT DEFAULT 0,
    `max_daily_slots` INT DEFAULT 5,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `assessment_scale` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `description` TEXT,
    `type` VARCHAR(50) DEFAULT NULL,
    `total_score` INT DEFAULT 100,
    `question_count` INT DEFAULT 0,
    `status` TINYINT DEFAULT 1,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `assessment_question` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `scale_id` BIGINT NOT NULL,
    `question_text` TEXT NOT NULL,
    `question_type` VARCHAR(20) DEFAULT 'SINGLE',
    `sort_order` INT DEFAULT 0,
    `options` TEXT
);

CREATE TABLE IF NOT EXISTS `assessment_record` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `scale_id` BIGINT NOT NULL,
    `total_score` INT DEFAULT 0,
    `result_level` VARCHAR(50) DEFAULT NULL,
    `result_detail` TEXT,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `assessment_answer` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `record_id` BIGINT NOT NULL,
    `question_id` BIGINT NOT NULL,
    `selected_option` VARCHAR(10) DEFAULT NULL,
    `score` INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS `appointment` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student_id` BIGINT NOT NULL,
    `counselor_id` BIGINT NOT NULL,
    `appointment_date` DATE NOT NULL,
    `time_slot` VARCHAR(20) NOT NULL,
    `type` VARCHAR(20) DEFAULT 'FACE',
    `reason` TEXT,
    `status` VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    `remark` TEXT,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `consultation_record` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `appointment_id` BIGINT DEFAULT NULL,
    `student_id` BIGINT NOT NULL,
    `counselor_id` BIGINT NOT NULL,
    `content` TEXT,
    `evaluation` TEXT,
    `suggestion` TEXT,
    `risk_level` VARCHAR(20) DEFAULT 'LOW',
    `start_time` TIMESTAMP DEFAULT NULL,
    `end_time` TIMESTAMP DEFAULT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `message` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `session_id` BIGINT NOT NULL,
    `sender_id` BIGINT NOT NULL,
    `receiver_id` BIGINT NOT NULL,
    `content` TEXT NOT NULL,
    `msg_type` VARCHAR(20) DEFAULT 'TEXT',
    `is_read` TINYINT DEFAULT 0,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `chat_session` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student_id` BIGINT NOT NULL,
    `counselor_id` BIGINT NOT NULL,
    `status` VARCHAR(20) DEFAULT 'ACTIVE',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_student_counselor` (`student_id`, `counselor_id`)
);

CREATE TABLE IF NOT EXISTS `article` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL,
    `content` TEXT,
    `summary` VARCHAR(500) DEFAULT NULL,
    `cover_image` VARCHAR(255) DEFAULT NULL,
    `category` VARCHAR(50) DEFAULT NULL,
    `author_id` BIGINT DEFAULT NULL,
    `view_count` INT DEFAULT 0,
    `like_count` INT DEFAULT 0,
    `status` VARCHAR(20) DEFAULT 'PUBLISHED',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `article_comment` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `article_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `content` TEXT NOT NULL,
    `parent_id` BIGINT DEFAULT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `mood_checkin` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL,
    `mood_type` VARCHAR(20) NOT NULL,
    `mood_score` INT DEFAULT 5,
    `note` VARCHAR(500) DEFAULT NULL,
    `checkin_date` DATE NOT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_user_date` (`user_id`, `checkin_date`)
);

CREATE TABLE IF NOT EXISTS `sys_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT DEFAULT NULL,
    `username` VARCHAR(50) DEFAULT NULL,
    `operation` VARCHAR(100) DEFAULT NULL,
    `method` VARCHAR(255) DEFAULT NULL,
    `params` TEXT,
    `ip` VARCHAR(50) DEFAULT NULL,
    `duration` BIGINT DEFAULT 0,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `file_upload` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `file_name` VARCHAR(255) NOT NULL,
    `file_path` VARCHAR(500) NOT NULL,
    `file_size` BIGINT DEFAULT 0,
    `file_type` VARCHAR(100) DEFAULT NULL,
    `uploader_id` BIGINT DEFAULT NULL,
    `business_type` VARCHAR(50) DEFAULT NULL,
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ========== 初始数据 ==========
-- admin/admin123 (BCrypt)
INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `email`, `role`, `status`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', '13800000000', 'admin@mentalhealth.com', 'ADMIN', 1);

-- counselor1/123456, counselor2/123456
INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `email`, `role`, `status`) VALUES
('counselor1', '$2a$10$EixZaYVK1fsbw1ZfbX3OXe.P0jFGnJqGMcHGWrkDkGkAtRfD5FQO2', '张心理', '13800000001', 'zhang@mentalhealth.com', 'COUNSELOR', 1),
('counselor2', '$2a$10$EixZaYVK1fsbw1ZfbX3OXe.P0jFGnJqGMcHGWrkDkGkAtRfD5FQO2', '李咨询', '13800000002', 'li@mentalhealth.com', 'COUNSELOR', 1);

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
('大学生如何应对考试焦虑', '<h2>什么是考试焦虑</h2><p>考试焦虑是指因考试压力过大而引发的一系列生理和心理上的不良反应。</p><h2>应对策略</h2><p>1. 认知调整：改变对考试的不合理认知。</p><p>2. 放松训练：深呼吸练习。</p><p>3. 时间管理：制定合理复习计划。</p>', '本文介绍了大学生考试焦虑的成因及科学应对方法', 'EMOTION', 1, 'PUBLISHED', 256, 48),
('建立良好宿舍人际关系', '<h2>宿舍关系的重要性</h2><p>大学宿舍是同学们生活的主要场所。</p><p>五个实用建议：学会倾听与尊重、明确边界、换位思考、主动沟通、互相帮助。</p>', '宿舍人际关系是大学生常见的困扰', 'RELATION', 2, 'PUBLISHED', 189, 35),
('认识抑郁症', '<h2>什么是抑郁症</h2><p>抑郁症是真实存在的疾病，不仅仅是心情不好。</p><p>典型症状包括：持续情绪低落、兴趣减退、睡眠障碍等。请及时寻求专业帮助。</p>', '科普抑郁症相关知识', 'DEPRESSION', 2, 'PUBLISHED', 312, 67);

-- 情绪打卡示例数据
INSERT INTO `mood_checkin` (`user_id`, `mood_type`, `mood_score`, `note`, `checkin_date`) VALUES
(2, 'HAPPY', 8, '今天咨询了几位同学，很有成就感', '2026-05-22'),
(2, 'CALM', 6, '日常工作，一切平稳', '2026-05-23'),
(2, 'HAPPY', 9, '收到了学生的感谢信', '2026-05-24'),
(2, 'ANXIOUS', 4, '遇到一个比较棘手的案例', '2026-05-25'),
(2, 'CALM', 7, '阅读了一本专业书籍', '2026-05-26'),
(2, 'HAPPY', 8, '小组讨论收获很多', '2026-05-27'),
(2, 'CALM', 7, '整理咨询案例', '2026-05-28');
