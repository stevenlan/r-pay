package com.rpay.service.impl.cms;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.rpay.mapper.cms.SiteNewsMapper;
import com.rpay.model.cms.SiteNews;
import com.rpay.service.cms.CmsService;
import com.rpay.service.query.NewQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author steven
 */
@Service
@RequiredArgsConstructor
public class CmsServiceImpl implements CmsService {
    private final SiteNewsMapper newsMapper ;
    @Override
    public List<SiteNews> queryRecNews(String lang) {
        LambdaQueryWrapper<SiteNews> wrapper = new LambdaQueryWrapper<>() ;
        wrapper.eq(SiteNews::getRecommend, 1) ;
        wrapper.eq(SiteNews::getNewStatus,0) ;
        wrapper.eq(SiteNews::getLang,lang) ;

        wrapper.orderByDesc(SiteNews::getModifiedTime) ;

        Page<SiteNews> page = new Page<>() ;
        page.setCurrent(1) ;
        page.setSize(3) ;
        page = newsMapper.selectPage(page,wrapper) ;
        if ( null == page.getRecords() || page.getRecords().isEmpty() ) {
            return null ;
        }
        return page.getRecords() ;
    }

    @Override
    public Page<SiteNews> querySiteNews(NewQuery query) {
        /*
         需要处理一下，转换成按时间排序的样子
         */
        LambdaQueryWrapper<SiteNews> wrapper = new LambdaQueryWrapper<>() ;
        wrapper.eq(SiteNews::getNewStatus,0) ;
        wrapper.eq(SiteNews::getLang,query.getLang()) ;
        wrapper.orderByDesc(SiteNews::getRecommend) ;
        wrapper.orderByDesc(SiteNews::getModifiedTime) ;
        return newsMapper.selectPage(query,wrapper);
    }

    @Override
    public boolean saveSiteNews(SiteNews news) {
        if ( null != news.getId() && news.getId() > 0 ) {
            return SqlHelper.retBool(newsMapper.updateById(news)) ;
        } else {
            return SqlHelper.retBool(newsMapper.insert(news)) ;
        }
    }

    @Override
    public boolean recSiteNews(Long newId, Boolean act) {
        LambdaUpdateChainWrapper<SiteNews> wrapper = new LambdaUpdateChainWrapper<>(newsMapper) ;
        wrapper.set(SiteNews::getRecommend,act) ;
        wrapper.eq(SiteNews::getId,newId) ;

        return wrapper.update() ;
    }

    @Override
    public boolean invalidSiteNews(Long newId) {
        LambdaUpdateChainWrapper<SiteNews> wrapper = new LambdaUpdateChainWrapper<>(newsMapper) ;
        wrapper.set(SiteNews::getNewStatus,1) ;
        wrapper.eq(SiteNews::getId,newId) ;

        return wrapper.update() ;
    }

    @Override
    public SiteNews getSiteNew(Long newId) {
        LambdaQueryWrapper<SiteNews> wrapper = new LambdaQueryWrapper<>() ;
        wrapper.eq(SiteNews::getId,newId) ;
        wrapper.eq(SiteNews::getNewStatus,0) ;
        return newsMapper.selectOne(wrapper) ;
    }
}
