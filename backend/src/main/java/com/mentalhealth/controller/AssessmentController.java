package com.mentalhealth.controller;

import com.mentalhealth.dto.Result;
import com.mentalhealth.service.AssessmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assessment")
public class AssessmentController {

    @Autowired
    private AssessmentService assessmentService;

    @GetMapping("/scales")
    public Result<?> getScales() {
        return Result.success(assessmentService.getActiveScales());
    }

    @GetMapping("/questions/{scaleId}")
    public Result<?> getQuestions(@PathVariable Long scaleId) {
        return Result.success(assessmentService.getQuestionsByScaleId(scaleId));
    }

    @PostMapping("/submit")
    public Result<?> submit(HttpServletRequest request, @RequestBody Map<String, Object> params) {
        Long userId = (Long) request.getAttribute("userId");
        Long scaleId = Long.valueOf(params.get("scaleId").toString());
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> answers = (List<Map<String, Object>>) params.get("answers");
        return Result.success(assessmentService.submitAssessment(userId, scaleId, answers));
    }

    @GetMapping("/records")
    public Result<?> getRecords(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(assessmentService.getUserRecords(userId));
    }
}

