package com.xuecheng.framework.domain.cms.request;

import com.xuecheng.framework.model.request.RequestData;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Description: 定义请求模型，作为查询条件类型
 * @Author: lemerence
 * @Date: Create in 11:30 2018/12/25
 */
@Data
public class QueryPageRequest extends RequestData {

    @ApiModelProperty("站点id")
    private String siteId;

    @ApiModelProperty("页面ID")
    private String pageId;

    @ApiModelProperty("页面名称")
    private String pageName;

    @ApiModelProperty("别名")
    private String pageAliase;

    @ApiModelProperty("模版id")
    private String templateId;
}
