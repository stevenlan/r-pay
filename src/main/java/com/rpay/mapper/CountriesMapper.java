package com.rpay.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rpay.model.Countries;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author steven
 */
@Mapper
public interface CountriesMapper extends BaseMapper<Countries> {

    /**
     * 查询
     * @return
     */
    @Select(value = "select * from sys_exchange e left join sys_countries c on e.`from_country` = c.`code`")
    List<Countries> selectExSource() ;

    /**
     * 查询
     * @param fromCountry 来源
     * @return
     */
    @Select(value = "select * from sys_exchange e left join sys_countries c on e.`target_country` = c.`code` where e.`from_country` = #{fromCountry} ;")
    List<Countries> selectExTarget(String fromCountry) ;
}
