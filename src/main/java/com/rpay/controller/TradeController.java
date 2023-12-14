package com.rpay.controller;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.rpay.common.utils.R;
import com.rpay.controller.vo.UserQuery;
import com.rpay.model.Order;
import com.rpay.model.User;
import com.rpay.service.OrderService;
import com.rpay.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单新增修改删除功能
 * @author steven
 */
@Slf4j
//@Controller
//@RequestMapping("api/trade")
@RequiredArgsConstructor
public class TradeController extends BaseController {
    private final UserService userService ;
    private final OrderService orderService ;

    @GetMapping({"","index"})
    public String trade(Model model) {
        User user = getLoginUser() ;
        model.addAttribute("user",user) ;
        return "trade" ;
    }

    @GetMapping("add")
    public String add(Model model) {
        User user = getLoginUser() ;
        model.addAttribute("user",user) ;
        return "addOrder" ;
    }

    @PostMapping("orders")
    @ResponseBody
    public R orders(UserQuery<Order> query) {
        User user = getLoginUser() ;
        LambdaQueryChainWrapper<Order> wra = orderService.lambdaQuery() ;
        if ( null == query.getOptions() ) {
            query.setOptions(1);
        }
        if ( query.getOptions() != -1 ) {
            long diff = System.currentTimeMillis() - 1000*60*60*24*query.getOptions() ;
            Date min = new Date(diff) ;
            wra.ge(Order::getModifiedTime,min) ;
        }
        if ( !StringUtils.equals(user.getProviderId(),user.getInviteCode()) ) {
            if ( null != query.getSub() ) {
                List<User> sub = userService.lambdaQuery().eq(User::getInviteCode, user.getProviderId()).list() ;
                if ( null != sub && !sub.isEmpty() ) {
                    List<String> subPro = new ArrayList<>() ;
                    sub.forEach(u -> {
                        subPro.add(u.getProviderId()) ;
                    });
                    wra.in(Order::getProviderId,subPro) ;
                } else {
                    query.setRecords(new ArrayList<>()) ;
                    return R.succeed(query) ;
                }
            } else {
                wra.eq(!StringUtils.equals(user.getProviderId(),user.getInviteCode())
                        , Order::getProviderId, user.getProviderId()) ;
            }
        }

        wra.orderByDesc(Order::getModifiedTime) ;
        query = wra.page(query) ;
        return R.succeed(query) ;
    }

    @PostMapping("add")
    @ResponseBody
    public R addOrder(Order order) {
        User user = userService.getById(order.getUserId()) ;
        //以后可以考虑只使用providerId
        order.setProviderId(user.getProviderId()) ;
        //以后传递有效kyc的name
        order.setName(user.getNickName()) ;

        if (orderService.save(order)) {
            return R.succeed("新增订单成功") ;
        } else {
            return R.failed("新增失败") ;
        }
    }
}
