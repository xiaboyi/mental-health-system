package com.mentalhealth.controller;

import com.mentalhealth.dto.Result;
import com.mentalhealth.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file,
                            @RequestParam(defaultValue = "OTHER") String businessType,
                            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        try {
            return Result.success(fileService.upload(file, businessType, userId));
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
}

