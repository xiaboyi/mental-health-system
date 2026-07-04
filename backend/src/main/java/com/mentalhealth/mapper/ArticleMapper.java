package com.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mentalhealth.entity.Article;
import org.apache.ibatis.annotations.Select;
import java.util.List;
import java.util.Map;

public interface ArticleMapper extends BaseMapper<Article> {
    @Select("SELECT a.*, u.real_name as author_name FROM article a LEFT JOIN user u ON a.author_id = u.id WHERE a.status = 'PUBLISHED' ORDER BY a.create_time DESC")
    List<Article> selectPublishedWithAuthor();

    @Select("SELECT category, COUNT(*) as count FROM article WHERE status = 'PUBLISHED' GROUP BY category")
    List<Map<String, Object>> countByCategory();
}
