package com.hz.gmall.pms.service;

import com.hz.gmall.pms.entity.SkuStock;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * sku的库存 服务类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
public interface SkuStockService extends IService<SkuStock> {

	BigDecimal getSkuPriceBySkuId(Long skuId);
}
