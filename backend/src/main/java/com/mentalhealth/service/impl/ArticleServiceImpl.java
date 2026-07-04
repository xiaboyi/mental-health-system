package com.mentalhealth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mentalhealth.entity.Article;
import com.mentalhealth.entity.ArticleComment;
import com.mentalhealth.mapper.ArticleMapper;
import com.mentalhealth.mapper.ArticleCommentMapper;
import com.mentalhealth.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleCommentMapper commentMapper;

    @Override
    public List<Article> getPublishedArticles(String category) {
        if (category != null && !category.isEmpty()) {
            return baseMapper.selectList(new LambdaQueryWrapper<Article>()
                    .eq(Article::getStatus, "PUBLISHED")
                    .eq(Article::getCategory, category)
                    .orderByDesc(Article::getCreateTime));
        }
        return baseMapper.selectPublishedWithAuthor();
    }

    @Override
    @Transactional
    public Article getArticleDetail(Long id) {
        incrementViewCount(id);
        return baseMapper.selectById(id);
    }

    @Override
    public ArticleComment addComment(ArticleComment comment) {
        commentMapper.insert(comment);
        return comment;
    }

    @Override
    public List<ArticleComment> getComments(Long articleId) {
        return commentMapper.selectByArticleId(articleId);
    }

    @Override
    public void incrementViewCount(Long id) {
        baseMapper.update(null, new LambdaUpdateWrapper<Article>()
                .eq(Article::getId, id)
                .setSql("view_count = view_count + 1"));
    }

    @Override
    public void likeArticle(Long id) {
        baseMapper.update(null, new LambdaUpdateWrapper<Article>()
                .eq(Article::getId, id)
                .setSql("like_count = like_count + 1"));
    }
}
