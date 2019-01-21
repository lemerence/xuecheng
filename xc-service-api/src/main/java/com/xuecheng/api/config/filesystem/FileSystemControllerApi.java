package com.xuecheng.api.config.filesystem;

import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 19:03 2019/1/18
 */
@Api(value = "文件管理接口",description = "文件管理接口，提供文件的增、删、改、查")
public interface FileSystemControllerApi {

    /**
     * 上传文件
     * @param multipartFile 文件
     * @param businesskey 文件标签
     * @param filetag 业务key
     * @param metadata 元信息，json格式
     * @return
     */
    @ApiOperation("上传文件接口")
    public UploadFileResult upload(MultipartFile multipartFile, String businesskey, String filetag, String metadata);
}
