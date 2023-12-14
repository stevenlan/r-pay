package com.rpay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rpay.model.Order;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author steven
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
