package com.rpay.controller.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author steven
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserQuery<T> extends Page<T> {
    private String search ;
    private Integer options ;
    private Integer sub ;
}
