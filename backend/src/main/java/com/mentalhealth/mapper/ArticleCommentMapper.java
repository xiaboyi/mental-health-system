package com.mentalhealth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mentalhealth.entity.ArticleComment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

public interface ArticleCommentMapper extends BaseMapper<ArticleComment> {
    @Select("SELECT ac.*, u.username, u.real_name, u.avatar FROM article_comment ac LEFT JOIN user u ON ac.user_id = u.id WHERE ac.article_id = #{articleId} ORDER BY ac.create_time DESC")
    List<ArticleComment> selectByArticleId(@Param("articleId") Long articleId);
}
