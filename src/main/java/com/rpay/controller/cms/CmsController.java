package com.rpay.controller.cms;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.common.utils.R;
import com.rpay.controller.BaseController;
import com.rpay.model.User;
import com.rpay.model.cms.SiteNews;
import com.rpay.service.cms.CmsService;
import com.rpay.service.query.NewQuery;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @author steven
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class CmsController extends BaseController {
    private final CmsService cmsService ;

    @ApiOperation(value = "首页后去固定3个最新的推荐新闻")
    @GetMapping("/api/cmdList")
    @ResponseBody
    public R<List<SiteNews>> cmsList(String lang){
        if (StringUtils.isBlank(lang)) {
            lang = "zh" ;
        }
        return R.succeed(cmsService.queryRecNews(lang)) ;
    }

    @ApiOperation(value = "后台管理新闻分页查询")
    @PostMapping("/api/cmsPage")
    @ResponseBody
    public R<Page<SiteNews>> cmsPage(@RequestBody NewQuery query){
        return R.succeed(cmsService.querySiteNews(query)) ;
    }

    @ApiOperation(value = "设置推荐")
    @GetMapping("/api/cmsRecommend")
    @ResponseBody
    public R recommend(Boolean act, Long id){
        if (cmsService.recSiteNews(id,act)) {
            return R.succeed("操作成功") ;
        }
        return R.failed("推荐失败") ;
    }

    @ApiOperation(value = "获取单个新闻详情")
    @GetMapping("/api/cmsGet")
    @ResponseBody
    public R<SiteNews> get(Long id){
        return R.succeed(cmsService.getSiteNew(id)) ;
    }

    @ApiOperation(value = "删除新闻")
    @GetMapping("/api/cmsDelete")
    @ResponseBody
    public R delete(Long id){
        if (cmsService.invalidSiteNews(id)) {
            return R.succeed("操作成功") ;
        }
        return R.failed("推荐失败") ;
    }

    @ApiOperation(value = "保存新闻，新增或者删除都调用该接口")
    @PostMapping("/api/cmsSave")
    @ResponseBody
    public R save(@Valid @RequestBody SiteNews news){
        User user = getLoginUser() ;
        news.setAuthor(user.getUsername()) ;
        news.setUserId(user.getId()) ;
        if (cmsService.saveSiteNews(news)) {
            return R.succeed("保存成功") ;
        }
        return R.failed("保存失败") ;
    }
}
