package com.rpay.common.exception;

import com.rpay.common.exception.vo.ValidVO;
import com.rpay.common.utils.R;
import com.rpay.service.i18n.LocaleMessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author dinghao
 * @date 2021/3/10
 */
@Slf4j
@ControllerAdvice
public class MyExceptionHandler {

    @Autowired
    private LocaleMessageUtil messageSource;

    @ResponseBody
    @ExceptionHandler(BindException.class)
    public R<List<ValidVO>> bindExceptionHandler(BindException e) {
        log.error("出现了异常! {}", e);

        List<ValidVO> validList = new ArrayList<>() ;
        e.getFieldErrors().forEach(err -> {
            ValidVO v = new ValidVO() ;
            v.setFiled(err.getField());
            v.setErrMsg(coverErr(err.getDefaultMessage())) ;

            validList.add(v) ;
        } );


        R<List<ValidVO>> ret = R.failed(validList.get(0).getErrMsg()) ;
        ret.setData(validList) ;
        return ret ;
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public Map<String, Object> errorHandler(Exception ex, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        // 根据不同错误获取错误信息
        if (ex instanceof IException) {
            map.put("code", ((IException) ex).getCode());
            map.put("msg", coverErr(ex.getMessage()));
        } else if (ex instanceof UnauthorizedException) {
            //sendRedirect("/error/403", request, response);
            map.put("code", 403);
            map.put("msg", "您没有访问此接口的权限");
        } else {
            String err = coverErr(ex.getMessage()) ;
            log.error(err, ex);
            map.put("code", 500);
            map.put("msg", err == null || err.trim().isEmpty() ? "未知错误" : err);

            ex.printStackTrace();
        }
        // 支持跨域
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, x-requested-with, X-Custom-Header, Authorization");
        return map;
    }

    private String coverErr(String err) {
        if (StringUtils.contains(err,"{")
                && StringUtils.contains(err,"}")) {
            err = StringUtils.substringBetween(err,"{","}") ;
            return messageSource.getMessage(err) ;
        }
        return err ;
    }
}
