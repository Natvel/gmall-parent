package com.hz.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hz.gmall.pms.entity.Product;
import com.hz.gmall.pms.entity.SkuStock;
import com.hz.gmall.to.es.EsProduct;
import com.hz.gmall.vo.PageInfoVo;
import com.hz.gmall.vo.PmsProductParam;
import com.hz.gmall.vo.PmsProductQueryParam;

import java.util.List;

/**
 * <p>
 * 商品信息 服务类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
public interface ProductService extends IService<Product> {

	PageInfoVo productPageInfo(PmsProductQueryParam productQueryParam,Long pageNum,Long pageSize);

	/**
	 * 保存商品数据
	 * @param productParam
	 */
	void saveProduct(PmsProductParam productParam);

	/**
	 * 批量上下架
	 * @param ids
	 * @param recommendStatus
	 */
	void updateStatusByIds(List<Long> ids,Integer recommendStatus);

	Product productInfo(Long id);


	EsProduct productAllInfo(Long id);

	EsProduct productSkuInfo(Long id);

	SkuStock skuInfoById(Long skuId);
}
