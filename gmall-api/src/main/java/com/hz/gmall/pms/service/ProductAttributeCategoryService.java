package com.hz.gmall.pms.service;

import com.hz.gmall.pms.entity.ProductAttributeCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hz.gmall.vo.PageInfoVo;

/**
 * <p>
 * 产品属性分类表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
public interface ProductAttributeCategoryService extends IService<ProductAttributeCategory> {

	PageInfoVo roductAttributeCategoryPageInfo(Integer pageNum,Integer pageSize);
}
