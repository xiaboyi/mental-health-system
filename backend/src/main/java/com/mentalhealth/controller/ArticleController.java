package com.mentalhealth.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mentalhealth.dto.PageResult;
import com.mentalhealth.dto.Result;
import com.mentalhealth.entity.Article;
import com.mentalhealth.entity.ArticleComment;
import com.mentalhealth.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "10") Integer size,
                          @RequestParam(required = false) String category) {
        Page<Article> p = articleService.page(new Page<>(page, size),
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Article>()
                        .eq(Article::getStatus, "PUBLISHED")
                        .eq(category != null && !category.isEmpty(), Article::getCategory, category)
                        .orderByDesc(Article::getCreateTime));
        return Result.success(PageResult.of(p.getTotal(), p.getPages(), p.getCurrent(), p.getSize(), p.getRecords()));
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        return Result.success(articleService.getArticleDetail(id));
    }

    @PostMapping
    public Result<?> create(HttpServletRequest request, @RequestBody Article article) {
        Long userId = (Long) request.getAttribute("userId");
        article.setAuthorId(userId);
        articleService.save(article);
        return Result.success(article);
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Article article) {
        article.setId(id);
        articleService.updateById(article);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        articleService.removeById(id);
        return Result.success("删除成功", null);
    }

    @PostMapping("/{id}/like")
    public Result<?> like(@PathVariable Long id) {
        articleService.likeArticle(id);
        return Result.success("点赞成功", null);
    }

    @GetMapping("/{id}/comments")
    public Result<?> comments(@PathVariable Long id) {
        return Result.success(articleService.getComments(id));
    }

    @PostMapping("/comment")
    public Result<?> addComment(HttpServletRequest request, @RequestBody ArticleComment comment) {
        Long userId = (Long) request.getAttribute("userId");
        comment.setUserId(userId);
        return Result.success(articleService.addComment(comment));
    }

    @GetMapping("/manage")
    public Result<?> manageList(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size,
                                HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("role");
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Article> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        if (!"ADMIN".equals(role)) {
            wrapper.eq(Article::getAuthorId, userId);
        }
        wrapper.orderByDesc(Article::getCreateTime);
        Page<Article> p = articleService.page(new Page<>(page, size), wrapper);
        return Result.success(PageResult.of(p.getTotal(), p.getPages(), p.getCurrent(), p.getSize(), p.getRecords()));
    }
}

