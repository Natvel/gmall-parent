package com.hz.gmall.pms.service;

import com.hz.gmall.pms.entity.ProductAttribute;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hz.gmall.vo.PageInfoVo;

/**
 * <p>
 * 商品属性参数表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
public interface ProductAttributeService extends IService<ProductAttribute> {

	PageInfoVo getCategoryAttribute(Long cid,Integer type,Integer pageNum,Integer pageSize);
}
