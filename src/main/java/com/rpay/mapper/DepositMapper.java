package com.rpay.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rpay.model.Deposit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author steven
 */
@Mapper
public interface DepositMapper extends BaseMapper<Deposit> {

    /**
     * 查询
     * @return
     */
    @Select("select d.id,d.coin_code,d.bank_id,d.deposit_status," +
            "b.id as `bank.id`, b.account_name as `bank.account_name`," +
            "b.country as `bank.country`, b.account_add as `bank.account_add`," +
            "b.bank_name as `bank.bank_name`, b.swift_code as `bank.swift_code`," +
            "b.bank_code as `bank.bank_code`, b.bank_account as `bank.bank_account`," +
            "b.bank_country as `bank.bank_country`, b.bank_add as `bank.bank_add`," +
            "b.account_cer as `bank.account_cer`, b.user_id as `bank.user_id`," +
            "b.bank_status as `bank.bank_status`, b.reason as `bank.reason`," +
            "b.create_time as `bank.create_time`, b.modified_time as `bank.modified_time`" +
            " from sys_deposit d left join bank_detail b on d.`bank_id` = b.`id`where d.deposit_status = 1" +
            " order by d.modified_time desc")
    List<Deposit> queryList() ;

    /**
     * 查询总数
     * @return
     */
    @Select("select count(0) from sys_deposit d left join bank_detail b on d.`bank_id` = b.`id`where d.deposit_status = 1")
    Integer countQueryList() ;

    /**
     * 分页查询
     * @param start 起始页
     * @param size 分页大小
     * @return
     */
    @Select("select d.id,d.coin_code,d.bank_id,d.deposit_status," +
            "b.id as `bank.id`, b.account_name as `bank.account_name`," +
            "b.country as `bank.country`, b.account_add as `bank.account_add`," +
            "b.bank_name as `bank.bank_name`, b.swift_code as `bank.swift_code`," +
            "b.bank_code as `bank.bank_code`, b.bank_account as `bank.bank_account`," +
            "b.bank_country as `bank.bank_country`, b.bank_add as `bank.bank_add`," +
            "b.account_cer as `bank.account_cer`, b.user_id as `bank.user_id`," +
            "b.bank_status as `bank.bank_status`, b.reason as `bank.reason`," +
            "b.create_time as `bank.create_time`, b.modified_time as `bank.modified_time`" +
            " from sys_deposit d left join bank_detail b on d.`bank_id` = b.`id`where d.deposit_status = 1" +
            " order by d.modified_time desc limit #{start},#{size}")
    List<Deposit> queryList(Long start, Long size) ;
}
