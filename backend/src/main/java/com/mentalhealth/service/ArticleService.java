package com.mentalhealth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mentalhealth.entity.Article;
import com.mentalhealth.entity.ArticleComment;
import java.util.List;

public interface ArticleService extends IService<Article> {
    List<Article> getPublishedArticles(String category);
    Article getArticleDetail(Long id);
    ArticleComment addComment(ArticleComment comment);
    List<ArticleComment> getComments(Long articleId);
    void incrementViewCount(Long id);
    void likeArticle(Long id);
}
