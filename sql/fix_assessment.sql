-- =====================================================
-- 心理测评模块 - 完整题库数据
-- SAS/SDS各20题，压力问卷20题，共60题
-- =====================================================
USE mental_health;

-- 确保 assessment_answer 表存在
CREATE TABLE IF NOT EXISTS `assessment_answer` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `record_id` BIGINT NOT NULL COMMENT '测评记录ID',
    `question_id` BIGINT NOT NULL COMMENT '题目ID',
    `selected_option` VARCHAR(10) DEFAULT NULL COMMENT '选择的选项',
    `score` INT DEFAULT 0 COMMENT '该题得分',
    PRIMARY KEY (`id`),
    KEY `idx_record_id` (`record_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='测评作答明细表';

-- 删除旧题目（只保留完整题库）
DELETE FROM assessment_question WHERE scale_id IN (1, 2, 3);

-- ============ SAS 焦虑自评量表 20题 ============
-- 每题选项: A=1分(没有或很少) B=2分(少部分) C=3分(相当多) D=4分(绝大部分)
INSERT INTO assessment_question (scale_id, question_text, question_type, sort_order, options) VALUES
(1, '我觉得比平常容易紧张和着急', 'SINGLE', 1, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我无缘无故地感到害怕', 'SINGLE', 2, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我容易心里烦乱或觉得惊恐', 'SINGLE', 3, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我觉得我可能将要发疯', 'SINGLE', 4, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我觉得一切都很好，也不会发生什么不幸', 'SINGLE', 5, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]'),
(1, '我手脚发抖打颤', 'SINGLE', 6, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我因为头痛、颈痛和背痛而苦恼', 'SINGLE', 7, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我感觉容易衰弱和疲乏', 'SINGLE', 8, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我觉得心平气和，并且容易安静坐着', 'SINGLE', 9, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]'),
(1, '我觉得心跳得很快', 'SINGLE', 10, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我因为一阵阵头晕而苦恼', 'SINGLE', 11, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我有晕倒发作，或觉得要晕倒似的', 'SINGLE', 12, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我吸气呼气都感到很容易', 'SINGLE', 13, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]'),
(1, '我的手脚麻木和刺痛', 'SINGLE', 14, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我因为胃痛和消化不良而苦恼', 'SINGLE', 15, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我常常要小便', 'SINGLE', 16, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我的手脚常常是干燥温暖的', 'SINGLE', 17, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]'),
(1, '我脸红发热', 'SINGLE', 18, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(1, '我容易入睡并且一夜睡得很好', 'SINGLE', 19, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]'),
(1, '我做噩梦', 'SINGLE', 20, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]');

-- ============ SDS 抑郁自评量表 20题 ============
INSERT INTO assessment_question (scale_id, question_text, question_type, sort_order, options) VALUES
(2, '我觉得闷闷不乐，情绪低沉', 'SINGLE', 1, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(2, '我觉得一天之中早晨最好', 'SINGLE', 2, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]'),
(2, '我一阵阵哭出来或觉得想哭', 'SINGLE', 3, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(2, '我晚上睡眠不好', 'SINGLE', 4, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(2, '我吃得跟平常一样多', 'SINGLE', 5, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]'),
(2, '我与异性密切接触时和以往一样感到愉快', 'SINGLE', 6, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]'),
(2, '我发觉我的体重在下降', 'SINGLE', 7, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(2, '我有便秘的苦恼', 'SINGLE', 8, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(2, '我心跳比平时快', 'SINGLE', 9, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(2, '我无缘无故地感到疲乏', 'SINGLE', 10, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(2, '我的头脑跟平常一样清楚', 'SINGLE', 11, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]'),
(2, '我觉得经常做的事情并没有困难', 'SINGLE', 12, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]'),
(2, '我觉得不安而平静不下来', 'SINGLE', 13, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(2, '我对将来抱有希望', 'SINGLE', 14, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]'),
(2, '我比平常容易生气激动', 'SINGLE', 15, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(2, '我觉得作出决定是容易的', 'SINGLE', 16, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]'),
(2, '我觉得自己是个有用的人，有人需要我', 'SINGLE', 17, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]'),
(2, '我的生活过得很有意思', 'SINGLE', 18, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]'),
(2, '我认为如果我死了别人会生活得好些', 'SINGLE', 19, '[{"label":"A","text":"没有或很少时间","score":1},{"label":"B","text":"少部分时间","score":2},{"label":"C","text":"相当多时间","score":3},{"label":"D","text":"绝大部分或全部时间","score":4}]'),
(2, '平常感兴趣的事我仍然照样感兴趣', 'SINGLE', 20, '[{"label":"A","text":"绝大部分或全部时间","score":4},{"label":"B","text":"相当多时间","score":3},{"label":"C","text":"少部分时间","score":2},{"label":"D","text":"没有或很少时间","score":1}]');

-- ============ 大学生压力问卷 20题 ============
INSERT INTO assessment_question (scale_id, question_text, question_type, sort_order, options) VALUES
(3, '因为学业任务繁重而感到压力', 'SINGLE', 1, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '担心考试成绩不理想', 'SINGLE', 2, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '对未来就业方向感到迷茫', 'SINGLE', 3, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '经济方面存在困难', 'SINGLE', 4, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '与室友或同学关系紧张', 'SINGLE', 5, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '与家人沟通存在障碍', 'SINGLE', 6, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '恋爱或情感问题让您困扰', 'SINGLE', 7, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '对自己的外貌或身材不满意', 'SINGLE', 8, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '经常感到时间不够用', 'SINGLE', 9, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '难以平衡学习和社团活动', 'SINGLE', 10, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '对自己所选专业感到不满', 'SINGLE', 11, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '感到孤独，缺乏知心朋友', 'SINGLE', 12, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '在众人面前发言感到紧张', 'SINGLE', 13, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '对自己的能力缺乏信心', 'SINGLE', 14, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '面临重要选择时难以做出决定', 'SINGLE', 15, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '睡眠质量差，经常失眠或多梦', 'SINGLE', 16, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '经常出现头痛、胃痛等身体不适', 'SINGLE', 17, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '对社交活动缺乏兴趣或刻意回避', 'SINGLE', 18, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '过度使用手机或网络来逃避现实', 'SINGLE', 19, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]'),
(3, '觉得大学生活没有意义或缺乏动力', 'SINGLE', 20, '[{"label":"A","text":"完全不符合","score":1},{"label":"B","text":"不太符合","score":2},{"label":"C","text":"比较符合","score":3},{"label":"D","text":"完全符合","score":4}]');
