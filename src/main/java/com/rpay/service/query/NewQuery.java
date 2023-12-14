package com.rpay.service.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.model.cms.SiteNews;
import lombok.Data;

/**
 * @author steven
 */
@Data
public class NewQuery extends Page<SiteNews> {
    private String lang ;
}
