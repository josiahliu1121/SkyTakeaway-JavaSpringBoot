package com.skytakeaway.server.controller.admin;

import com.skytakeaway.common.result.Result;
import com.skytakeaway.common.utils.OssUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Tag(name = "common interface")
@RestController
@RequestMapping("/admin/common")
public class CommonController {
    @Autowired
    private OssUtil ossUtil;

    //.jpg file upload
    @Operation(summary = "upload file")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file){
        System.out.println("image upload requested");
        try {
            String originalFileName = file.getOriginalFilename();
            // extension = .jpg
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String objectName = UUID.randomUUID().toString() + extension;

            String filePath = ossUtil.upload(file.getBytes(), objectName);

            System.out.println("image upload success");
            return Result.success(filePath);
        } catch (IOException e) {
            System.out.println("file upload fail");
            throw new RuntimeException(e);
        }
    }
}
