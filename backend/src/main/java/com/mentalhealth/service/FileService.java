package com.mentalhealth.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface FileService {
    Map<String, String> upload(MultipartFile file, String businessType, Long userId);
}
