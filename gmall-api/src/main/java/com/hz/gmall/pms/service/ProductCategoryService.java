package com.hz.gmall.pms.service;

import com.hz.gmall.pms.entity.ProductCategory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hz.gmall.vo.PmsProductCategoryWithChildrenItem;

import java.util.List;

/**
 * <p>
 * 产品分类 服务类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
public interface ProductCategoryService extends IService<ProductCategory> {

	/**
	 * 查询菜单及其子菜单
	 * @return
	 */
	List<PmsProductCategoryWithChildrenItem> listCategoryWithChilder(Integer categoryId);
}
