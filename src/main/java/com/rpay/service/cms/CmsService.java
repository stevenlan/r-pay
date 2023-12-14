package com.rpay.service.cms;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.model.cms.SiteNews;
import com.rpay.service.query.NewQuery;

import java.util.List;

/**
 * @author steven
 */
public interface CmsService {
    /**
     * 查询首页默认三个新闻
     * @return
     */
    List<SiteNews> queryRecNews(String lang) ;

    /**
     * 后台管理新闻查询分页
     * @param query
     * @return
     */
    Page<SiteNews> querySiteNews(NewQuery query) ;

    /**
     * 保存新闻
     * @param news
     * @return
     */
    boolean saveSiteNews(SiteNews news) ;

    /**
     * 推荐新闻
     * @param newId
     * @param act
     * @return
     */
    boolean recSiteNews(Long newId, Boolean act) ;

    /**
     * 失效新闻
     * @param newId
     * @return
     */
    boolean invalidSiteNews(Long newId) ;

    /**
     * 获取单个新闻
     * @param newId
     * @return
     */
    SiteNews getSiteNew(Long newId) ;
}
