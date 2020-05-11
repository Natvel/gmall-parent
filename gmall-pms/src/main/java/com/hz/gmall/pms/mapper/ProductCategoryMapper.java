package com.hz.gmall.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hz.gmall.pms.entity.ProductCategory;
import com.hz.gmall.vo.PmsProductCategoryWithChildrenItem;

import java.util.List;

/**
 * <p>
 * 产品分类 Mapper 接口
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
public interface ProductCategoryMapper extends BaseMapper<ProductCategory> {

	/**
	 * 查询子菜单
	 * @param categoryId
	 * @return
	 */
	List<PmsProductCategoryWithChildrenItem> listCategoryWithChildren(Integer categoryId);
}
