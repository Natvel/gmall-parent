package com.hz.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hz.gmall.pms.mapper.SkuStockMapper;
import com.hz.gmall.pms.entity.SkuStock;
import com.hz.gmall.pms.service.SkuStockService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * <p>
 * sku的库存 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
@Component
@Service
public class SkuStockServiceImpl extends ServiceImpl<SkuStockMapper, SkuStock> implements SkuStockService {

	@Resource
	SkuStockMapper skuStockMapper;
	@Override
	public BigDecimal getSkuPriceBySkuId(Long skuId){
		SkuStock skuStock = skuStockMapper.selectById(skuId);
		return skuStock.getPrice();
	}
}
