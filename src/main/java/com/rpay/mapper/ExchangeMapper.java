package com.rpay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rpay.model.Exchange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author steven
 */
@Mapper
public interface ExchangeMapper extends BaseMapper<Exchange> {
    @Select("select distinct e.`ex_target` from sys_exchange e left join sys_countries c on e.`ex_target` = c.`coin_code` where e.`ex_from` = #{coinCode}")
    List<String> withdrawCoinsForType(String coinCode) ;
}
