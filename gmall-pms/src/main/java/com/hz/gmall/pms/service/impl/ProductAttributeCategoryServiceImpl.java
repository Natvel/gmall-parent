package com.hz.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hz.gmall.pms.entity.ProductAttribute;
import com.hz.gmall.pms.entity.ProductAttributeCategory;
import com.hz.gmall.pms.mapper.ProductAttributeCategoryMapper;
import com.hz.gmall.pms.mapper.ProductAttributeMapper;
import com.hz.gmall.pms.service.ProductAttributeCategoryService;
import com.hz.gmall.vo.PageInfoVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * 产品属性分类表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
@Service
@Component
public class ProductAttributeCategoryServiceImpl extends ServiceImpl<ProductAttributeCategoryMapper, ProductAttributeCategory> implements ProductAttributeCategoryService {

	@Resource
	ProductAttributeMapper productAttributeMapper;
	/**
	 * 分页查询所有分类
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfoVo roductAttributeCategoryPageInfo(Integer pageNum,Integer pageSize){
		IPage<ProductAttribute> page = productAttributeMapper.selectPage(new Page<>(pageNum,pageSize),null);
		return PageInfoVo.getVo(page,pageNum,pageSize);
	}
}
