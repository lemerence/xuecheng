package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 19:11 2019/1/18
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
