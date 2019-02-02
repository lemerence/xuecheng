package com.xuecheng.filesystem.controller;

import com.xuecheng.api.filesystem.FileSystemControllerApi;
import com.xuecheng.filesystem.service.FileSystemService;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 19:47 2019/1/18
 */
@RequestMapping("/fileSystem")
@RestController
public class FileSystemController implements FileSystemControllerApi {

    @Autowired
    FileSystemService fileSystemService;

    @Override
    @PostMapping("upload")
    public UploadFileResult upload(MultipartFile multipartFile, String businesskey, String filetag, String metadata) {
        return fileSystemService.upload(multipartFile,businesskey,filetag,metadata);
    }
}
