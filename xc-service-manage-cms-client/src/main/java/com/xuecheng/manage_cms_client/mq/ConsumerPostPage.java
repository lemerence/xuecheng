package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms_client.service.PageService;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * 监听消息
 * @Author lemerence
 * @Version 1.0
 * @Date 23:31 2019/1/11
 */
@Component
public class ConsumerPostPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    PageService pageService;
    @Autowired
    CmsPageRepository cmsPageRepository;

    @RabbitListener(queues = "${xuecheng.mq.queue}")
    public void postPage(String msg){
        //解析消息
        Map map = JSON.parseObject(msg, Map.class);
        LOGGER.info("receive cms post page:{}",msg.toString());
        //得到消息中的页面id
        String pageId = (String) map.get("pageId");
        //查询页面信息，判断页面是否为空
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (!optional.isPresent()){
            LOGGER.info("receive cms post page, cmsPage is null:{}",msg.toString());
            return;
        }
        //调用pageService中方法下载页面文件并储存
        pageService.savePageToServerPath(pageId);
    }

}
