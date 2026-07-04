package com.mentalhealth.service.impl;

import com.mentalhealth.entity.FileUpload;
import com.mentalhealth.mapper.FileUploadMapper;
import com.mentalhealth.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${file.upload.path:./uploads/}")
    private String uploadPath;

    @Autowired
    private FileUploadMapper fileUploadMapper;

    @Override
    public Map<String, String> upload(MultipartFile file, String businessType, Long userId) {
        if (file.isEmpty()) throw new RuntimeException("文件为空");
        try {
            File dir = new File(uploadPath);
            if (!dir.exists()) dir.mkdirs();

            String originalName = file.getOriginalFilename();
            String suffix = originalName != null && originalName.contains(".") ?
                    originalName.substring(originalName.lastIndexOf(".")) : "";
            String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix;
            String filePath = uploadPath + newFileName;

            file.transferTo(new File(filePath));

            FileUpload fileUpload = new FileUpload();
            fileUpload.setFileName(originalName);
            fileUpload.setFilePath(filePath);
            fileUpload.setFileSize(file.getSize());
            fileUpload.setFileType(file.getContentType());
            fileUpload.setUploaderId(userId);
            fileUpload.setBusinessType(businessType);
            fileUploadMapper.insert(fileUpload);

            Map<String, String> result = new HashMap<>();
            result.put("url", "/uploads/" + newFileName);
            result.put("fileName", originalName);
            result.put("fileId", fileUpload.getId().toString());
            return result;
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败: " + e.getMessage());
        }
    }
}
