package com.rpay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rpay.mapper.OrderMapper;
import com.rpay.model.Order;
import com.rpay.service.OrderService;
import com.rpay.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author steven
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    private final UserService userService ;
}
