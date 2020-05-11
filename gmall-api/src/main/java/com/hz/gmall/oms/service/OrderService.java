package com.hz.gmall.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hz.gmall.oms.entity.Order;
import com.hz.gmall.vo.order.OrderConfirmVo;
import com.hz.gmall.vo.order.OrderCreateVo;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
public interface OrderService extends IService<Order> {

	OrderConfirmVo orderConfirm(Long id);

	OrderCreateVo createOrder(BigDecimal totalPrice,Long addressId,String note);

	String pay(String orderSn,String accessToken);

	String resolvePayResult(Map<String,String> params);
}
