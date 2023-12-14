package com.rpay.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.model.bill.BalanceDetail;
import com.rpay.model.bill.BillDetail;
import com.rpay.model.bill.ChangeDetail;
import com.rpay.service.query.BillDetailQuery;
import com.rpay.service.query.ChangeDetailQuery;

/**
 * 账单以及余额相关服务
 * @author steven
 */
public interface BillService {
    /**
     * 记录入账账单
     * @param bill 账单详情
     * @return
     */
    boolean recordDepBill(BillDetail bill) ;

    /**
     * 记录兑换金额
     * @param change 记录兑换金额信息
     * @return
     */
    boolean recordChange(ChangeDetail change) ;

    /**
     * 查询账本变更信息列表
     * @param query 查询信息集合
     * @return
     */
    Page<BillDetail> balDetails(BillDetailQuery query) ;

    /**
     * 查询换汇详细信息列表
     * @param query 查询信息集合
     * @return
     */
    Page<ChangeDetail> changeDetails(ChangeDetailQuery query) ;

    /**
     * 创建兑换申请，兑换申请后状态为未确认，但是账户余额会先扣除，取消申请或者管理员驳回会解开余额
     * @param req 申请信息
     * @return
     */
    boolean creChange(ChangeDetail req) ;

    /**
     * 取消申请
     * @param id
     * @return
     */
    boolean cancelChange(Long id) ;

    /**
     * 管理员审批换汇申请
     * @param id 换汇申请单id
     * @param pass 是否完成
     * @param mime 拒绝原因
     * @return
     */
    boolean perChange(Long id, boolean pass, String mime) ;

    /**
     * 获取单个兑换单详情
     * @param id 兑换单id
     * @return
     */
    ChangeDetail getChangeDetail(Long id) ;

    /**
     * 回滚
     * @param userId 回滚的账本id
     * @param coin 回滚的货币
     * @param billType 回滚前的账单类型
     * @param outId 关联的业务单据id
     * @param backType 回滚后的账单类型
     */
    void rollbackBal(Long userId, String coin, Integer billType, Long outId, Integer backType) ;
}
