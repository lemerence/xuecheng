package com.xuecheng.filesystem.service;

import com.xuecheng.filesystem.dao.FileSystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 19:45 2019/1/18
 */
@Service
public class FileSystemService {

    @Value("${xuecheng.fastdfs.tracker_servers}")
    String tracker_servers;
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    int connect_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    int network_timeout_in_seconds;
    @Value("${xuecheng.fastdfs.charset}")
    String charset;

    @Autowired
    FileSystemRepository fileSystemRepository;

    /**
     * 上传文件
     * @param multipartFile
     * @param businesskey
     * @param filetag
     * @param metadata
     * @return
     */
    public UploadFileResult upload(MultipartFile multipartFile, String businesskey, String filetag, String metadata){

        if (multipartFile == null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_FILEISNULL);
        }

        //上传文件到fastDFS中，的人道一个文件id
        String fileId = fdfs_upload(multipartFile);
        if (fileId == null){
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }

        //将文件信息存储到mongoDB中
        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setFiletag(filetag);
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFileType(multipartFile.getContentType());
        fileSystemRepository.save(fileSystem);

        return new UploadFileResult(FileSystemCode.FS_UPLOADFILE_SUCCESS,fileSystem);
    }

    /**
     * 上传文件到fastDFS
     * @param multipartFile 文件
     * @return 文件id
     */
    private String fdfs_upload(MultipartFile multipartFile) {
        //初始化fastDFS的环境
        try {
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_charset(charset);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_INITFAIL);
        }
        //创建trackerClient
        TrackerClient trackerClient = new TrackerClient();
        try {
            //TrackerServer
            TrackerServer trackerServer = trackerClient.getConnection();
            //StorageServer
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            //StorageClient1
            StorageClient1 storageClient1 = new StorageClient1(trackerServer,storageServer);
            //使用storageClient1上传文件
            byte[] bytes = multipartFile.getBytes();
            String filename = multipartFile.getOriginalFilename();
            String extName = filename.substring(filename.lastIndexOf(".")+1);
            String fileId = storageClient1.upload_file1(bytes, extName, null);
            return fileId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
