package com.xuecheng.manage_cms;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @Description:
 * @Author: lemerence
 * @Date: Create in 21:12 2018/12/28
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GridFsTest {

    @Autowired
    GridFsTemplate fsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    /**
     * 通过GridFs存文件到mongodb数据库
     * @throws FileNotFoundException
     */
    @Test
    public void testTemplate() throws FileNotFoundException {
        File file = new File("G:\\xczx/index_banner.ftl");
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectId objectId = fsTemplate.store(fileInputStream, "index_banner.html");
        System.out.println(objectId);
    }

    /**
     * 读取文件（从数据库中把文件下载下来）
     * @throws IOException
     */
    @Test
    public void testBucket() throws IOException {
        String id = "5c2d6f5aa56e3b23bcaaf494";
        //根据id查询文件
        GridFSFile fsFile = fsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
        //打开下载流对象
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(fsFile.getObjectId());
        //创建gridFsResource，获取流对象
        GridFsResource fsResource = new GridFsResource(fsFile, downloadStream);
        //获取流中的数据
        String s = IOUtils.toString(fsResource.getInputStream(), "UTF-8");
        System.out.println(s);
    }

    @Test
    public void testDelFile(){
        String id = "5c2d6f5aa56e3b23bcaaf494";
        fsTemplate.delete(Query.query(Criteria.where("_id").is(id)));
    }

}
