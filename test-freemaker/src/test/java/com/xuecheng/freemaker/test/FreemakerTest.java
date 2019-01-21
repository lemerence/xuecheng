package com.xuecheng.freemaker.test;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 16:42 2019/1/2
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FreemakerTest {

    @Autowired
    RestTemplate restTemplate;

    /**
     * 测试生成静态页面（读取配置文件中的模型）
     * @throws Exception
     */
    @Test
    public void testGeneratelHtml() throws Exception {
        //定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //得到classpath路径
        String classPath = this.getClass().getResource("/").getPath();
        //获取模板路径
        configuration.setDirectoryForTemplateLoading(new File(classPath+"/templates/"));
        //定义模板
        Template template = configuration.getTemplate("test.ftl");
        //获取数据
        Map map = new HashMap<>();
        map.put("name","yyyy");
        //静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        InputStream fis = IOUtils.toInputStream(content);
        FileOutputStream out = new FileOutputStream(new File("G:/xczx/test.html"));
        IOUtils.copy(fis,out);
        fis.close();
        out.close();
    }

    /**
     * 测试生成静态页面通过字符串
     * @throws Exception
     */
    @Test
    public void testGeneratelHtmlByString() throws Exception {
        //定义配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
/*        //得到classpath路径
        String classPath = this.getClass().getResource("/").getPath();
        //获取模板路径
        configuration.setDirectoryForTemplateLoading(new File(classPath+"/templates/"));
        //定义模板
        Template template = configuration.getTemplate("test.ftl");*/
        //定义模型，通过字符串
        String templateString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"utf-8\">\n" +
                "    <title>Hello World!</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "hello${name}\n" +
                "</body>\n" +
                "</html>";
        StringTemplateLoader loader = new StringTemplateLoader();
        loader.putTemplate("template",templateString);
        configuration.setTemplateLoader(loader);
        Template template = configuration.getTemplate("template", "UTF-8");

        //获取数据
        Map map = new HashMap<>();
        map.put("name","yyyy");
        //静态化
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        InputStream fis = IOUtils.toInputStream(content);
        FileOutputStream out = new FileOutputStream(new File("G:/xczx/test1.html"));
        IOUtils.copy(fis,out);
        fis.close();
        out.close();
    }

    @Test
    public void testRestTemplate(){
        String dataUrl = "http://localhost:31001/cms/config/getModel/5a791725dd573c3574ee333f";
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = entity.getBody();
        System.out.println(body);
    }
}
