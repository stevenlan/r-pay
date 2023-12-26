package com.rpay.model.cms;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author steven
 */
@Data
@TableName("site_news")
@EqualsAndHashCode(callSuper = true)
public class SiteNews extends Model<SiteNews> {
    /**
     * 自增id
     */
    @TableId
    private Long id ;

    @ApiParam(name = "lang", value = "新闻语种", required = true)
    @NotBlank(message = "{news.lang.empty}")
    private String lang ;

    @ApiParam(name = "title", value = "新闻标题", required = true)
    @NotBlank(message = "{news.title.empty}")
    private String title ;

    @ApiParam(name = "cover", value = "新闻简介图片，上传图片后保存对应的图片地址，图片格式大小按照首页面板图片的格式提示，上船后等比放缩", required = true)
    @NotBlank(message = "{news.cover.empty}")
    private String cover ;

    @ApiParam(name = "mainPoint", value = "新闻摘要简介，用于在首页新闻陈列时显示的简介内容", required = true)
    @NotBlank(message = "{news.mainPoint.empty}")
    private String mainPoint ;

    @ApiParam(name = "content", value = "新闻主题内容，可支持html的富文本格式", required = true)
    @NotBlank(message = "{news.content.empty}")
    private String content ;

    @ApiParam(name = "newStatus", value = "新闻状态，不需要填写", required = false)
    private Integer newStatus ;

    @ApiParam(name = "recommend", value = "是否推荐, 必选，穿boolean值", required = true)
    @NotNull(message = "{news.recommend.empty}")
    private Boolean recommend ;

    @ApiParam(name = "count", value = "点击次数", required = false)
    private Integer count ;

    @ApiParam(name = "author", value = "新闻做着", required = false)
    private String author ;

    @ApiParam(name = "userId", value = "新闻创建人", required = false)
    private Long userId ;

    /**
     * 注册时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifiedTime;
}
