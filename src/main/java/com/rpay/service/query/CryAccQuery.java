package com.rpay.service.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.model.CryAccount;
import lombok.Data;

/**
 * @author steven
 */
@Data
public class CryAccQuery extends Page<CryAccount> {
    private String cryCode ;
}
