package com.rpay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rpay.common.exception.BusinessException;
import com.rpay.common.exception.ParameterException;
import com.rpay.common.utils.SessionUtils;
import com.rpay.mapper.BankDetailMapper;
import com.rpay.mapper.CountriesMapper;
import com.rpay.mapper.ExchangeMapper;
import com.rpay.mapper.KycCertificationMapper;
import com.rpay.model.*;
import com.rpay.service.AccountService;
import com.rpay.service.UserService;
import com.rpay.service.query.BankQuery;
import com.rpay.service.query.ExQuery;
import com.rpay.service.query.KycQuery;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author steven
 */
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService, SessionUtils {
    private final Logger log = LoggerFactory.getLogger(this.getClass()) ;

    @Autowired
    private KycCertificationMapper kycMapper ;
    @Autowired
    private BankDetailMapper bankMapper ;
    @Autowired
    private CountriesMapper countriesMapper ;
    @Autowired
    private ExchangeMapper exMapper ;
    @Autowired
    private UserService userService ;

    @Override
    public boolean updateAccount(BankDetail bank) {
        if ( StringUtils.isNotBlank(bank.getPayPass()) ) {
            //验证支付密码
            boolean flag = userService.checkPayPass(getLoginUserId(), bank.getPayPass()) ;
            if ( !flag ) {
                //输入错误计数
                throw new BusinessException("支付密码错误，请重新输入") ;
            }
        }
        if ( null != bank.getId() ) {
            return bankMapper.updateById(bank) > 0 ;
        } else {
            return bankMapper.insert(bank) > 0 ;
        }
    }

    @Override
    public boolean updateKyc(KycCertification kyc) {
        KycCertification kyc1 = findKyc(kyc.getUserId()) ;
        if ( !userService.isAdmin(getLoginUser()) ) {
            if (StringUtils.isBlank(kyc.getRegCer())) {
                throw new BusinessException("企业证明资料必须上传，请检查是否已经上传商业登记证书或营业执照，以及经营地址证明，银行水电账单等资料，支持PDF或压缩文件等格式") ;
            }
            if (StringUtils.isBlank(kyc.getLegal())) {
                throw new BusinessException("法人资料必须上传，请检查是否上传法人护照及居住地址证明，支持PDF或压缩文件等格式") ;
            }
            if ( null != kyc1 && kyc1.getKycStatus() != 2 ) {
                throw new BusinessException("kyc信息已提交，未被驳回不能修改") ;
            }
        }

        if ( null != kyc1 ) {
            kyc.setId(kyc1.getId()) ;
            kyc.setKycStatus(0) ;
            return kycMapper.updateById(kyc) > 0;
        } else {
            return kycMapper.insert(kyc) > 0 ;
        }
    }

    @Override
    public boolean updateEx(Exchange ex) {
        if (StringUtils.isBlank(ex.getExFrom()) || StringUtils.isBlank(ex.getExTarget())) {
            log.info(ex.toString()) ;
            throw new ParameterException("汇率的兑换对为空") ;
        }
        int count = exMapper.selectCount(new QueryWrapper<Exchange>().lambda()
                .eq(Exchange::getExFrom,ex.getExFrom())
                .eq(Exchange::getExTarget,ex.getExTarget())) ;
        if ( count>0 ) {
            return exMapper.update(ex,new QueryWrapper<Exchange>().lambda()
                    .eq(Exchange::getExFrom,ex.getExFrom())
                    .eq(Exchange::getExTarget,ex.getExTarget())) > 0 ;
        } else {
            return exMapper.insert(ex) > 0 ;
        }
    }

    @Override
    public List<BankDetail> findBanksByUser(Long userId, BankQuery query) {
        query.setUserId(userId) ;
        return findBanks(query);
    }

    @Override
    public List<BankDetail> findBanks(BankQuery query) {
        QueryWrapper<BankDetail> wra = renderBankQuery(query) ;

        return bankMapper.selectList(wra) ;
    }

    @Override
    public Page<BankDetail> pageBanksByUser(BankQuery query) {
        QueryWrapper<BankDetail> wra = renderBankQuery(query) ;

        return bankMapper.selectPage(query,wra) ;
    }

    private QueryWrapper<BankDetail> renderBankQuery(BankQuery query) {
        QueryWrapper<BankDetail> wra = new QueryWrapper<BankDetail>() ;
        if ( null != query.getUserId() ) {
            wra.lambda().eq(BankDetail::getUserId,query.getUserId()) ;
        } else if ( null != query.getEmail() ) {
            User user = userService.lambdaQuery().eq(User::getEmail,query.getEmail()).one() ;
            if ( null == user ) {
                return null ;
            }
            wra.lambda().eq(BankDetail::getUserId,user.getId()) ;
        }

        wra.lambda().eq(null != query.getBankStatus(), BankDetail::getBankStatus, query.getBankStatus())
                .eq( StringUtils.isNotBlank(query.getSwiftCode()), BankDetail::getSwiftCode, query.getSwiftCode())
                .like( StringUtils.isNotBlank(query.getBankAccount()), BankDetail::getBankAccount, query.getBankAccount() )
                .like( StringUtils.isNotBlank(query.getBankName()), BankDetail::getBankName, query.getBankName())
                .orderByDesc(BankDetail::getModifiedTime) ;
        return wra ;
    }

    @Override
    public int bankCount(Long userId, boolean pass) {
        QueryWrapper<BankDetail> wra = new QueryWrapper<BankDetail>() ;
        wra.lambda().eq(BankDetail::getUserId,userId).eq(pass, BankDetail::getBankStatus,1) ;
        return bankMapper.selectCount(wra) ;
    }

    @Override
    public boolean passBank(Long bankId, boolean pass, String reason) {
        LambdaUpdateChainWrapper<BankDetail> update = new LambdaUpdateChainWrapper<>(bankMapper) ;
        update.set(pass,BankDetail::getBankStatus,1) ;
        update.set(!pass,BankDetail::getBankStatus,2) ;
        update.set(!pass,BankDetail::getReason, reason) ;

        update.eq(BankDetail::getId, bankId) ;

        return update.update() ;
    }

    @Override
    public boolean bankDel(BankDetail bank, Long userId) {
        BankDetail bank1 = findBank(bank.getId()) ;
        if ( bank1.getUserId().equals(userId) ) {
            if ( StringUtils.isNotBlank(bank.getPayPass()) ) {
                //验证支付密码
                boolean flag = userService.checkPayPass(getLoginUserId(), bank.getPayPass()) ;
                if ( !flag ) {
                    //输入错误计数
                    throw new BusinessException("支付密码错误，请重新输入") ;
                }
            }
        }
        if ( null==userId || bank1.getUserId().equals(userId) ) {
            return bankMapper.deleteById(bank.getId()) > 0 ;
        }
        return false ;
    }

    @Override
    public BankDetail findBank(Long bankId) {
        return bankMapper.selectById(bankId) ;
    }

    @Override
    public List<KycCertification> findKycList(KycQuery query) {
        return kycMapper.selectList(renderKycQuery(query));
    }

    @Override
    public Page<KycCertification> pageKycList(KycQuery query) {
        return kycMapper.selectPage(query, renderKycQuery(query));
    }

    private QueryWrapper<KycCertification> renderKycQuery(KycQuery query) {
        QueryWrapper<KycCertification> wra = new QueryWrapper<KycCertification>() ;
        if ( null != query.getUserId() ) {
            wra.lambda().eq(KycCertification::getUserId,query.getUserId()) ;
        } else if ( null != query.getEmail() ) {
            User user = userService.lambdaQuery().eq(User::getEmail,query.getEmail()).one() ;
            if ( null == user ) {
                return null ;
            }
            wra.lambda().eq(KycCertification::getUserId,user.getId()) ;
        } else if ( null != query.getPhone() ) {
            User user = userService.lambdaQuery().eq(User::getPhone,query.getPhone()).one() ;
            if ( null == user ) {
                return null ;
            }
            wra.lambda().eq(KycCertification::getUserId,user.getId()) ;
        }

        wra.lambda().eq( null!=query.getKycStatus(), KycCertification::getKycStatus
                        , query.getKycStatus())
                .like( StringUtils.isNotBlank(query.getName()), KycCertification::getCompanyName, query.getName() )
                .orderByDesc(KycCertification::getModifiedTime) ;
        return wra ;
    }

    @Override
    public KycCertification findKyc(Long userId) {
        return kycMapper.selectOne(new QueryWrapper<KycCertification>().lambda()
                .eq(KycCertification::getUserId, userId));
    }

    @Override
    public KycCertification findKycForId(Long kycId) {
        return kycMapper.selectById(kycId) ;
    }

    @Override
    public boolean passKyc(Long kycId, boolean pass, String reason) {
        if ( pass ) {
            KycCertification kyc = kycMapper.selectById(kycId) ;
            userService.activeUser(kyc.getUserId()) ;
        }
        LambdaUpdateChainWrapper<KycCertification> update = new LambdaUpdateChainWrapper<>(kycMapper) ;

        update.set(pass, KycCertification::getKycStatus,1) ;
        update.set(!pass, KycCertification::getKycStatus,2) ;
        update.set(!pass, KycCertification::getReason,reason) ;

        update.eq(KycCertification::getId,kycId) ;
        return update.update() ;
    }

    @Override
    public List<Countries> getCountries() {
        return countriesMapper.selectList(new QueryWrapper<Countries>().lambda().eq(Countries::getCoinType,1)
                .isNotNull(Countries::getCoinCode).isNotNull(Countries::getAreaCode)) ;
    }

    @Override
    public List<Countries> getCrypts() {
        return countriesMapper.selectList(new QueryWrapper<Countries>().lambda().eq(Countries::getCoinType,2)) ;
    }

    @Override
    public Countries getCountry(String code) {
        return countriesMapper.selectOne(new QueryWrapper<Countries>().lambda().eq(Countries::getCode, code));
    }

    @Override
    public List<Exchange> exchanges(ExQuery query) {
        QueryWrapper<Exchange> wra = new QueryWrapper<Exchange>() ;
        wra.lambda().orderByDesc(Exchange::getModifiedTime) ;
        return exMapper.selectList(wra);
    }

    @Override
    public Exchange matchExchange(ExQuery query) {
        return exMapper.selectOne(new QueryWrapper<Exchange>().lambda()
                .eq(Exchange::getExFrom,query.getExFrom())
                .eq(Exchange::getExTarget,query.getExTarget())) ;
    }
}
