package com.hz.gmall.oms.service.impl;

import com.hz.gmall.oms.entity.OrderItem;
import com.hz.gmall.oms.mapper.OrderItemMapper;
import com.hz.gmall.oms.service.OrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单中所包含的商品 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {

}
