package com.hz.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hz.gmall.pms.entity.ProductAttribute;
import com.hz.gmall.pms.mapper.ProductAttributeMapper;
import com.hz.gmall.pms.service.ProductAttributeService;
import com.hz.gmall.vo.PageInfoVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * <p>
 * 商品属性参数表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
@Service
@Component
public class ProductAttributeServiceImpl extends ServiceImpl<ProductAttributeMapper, ProductAttribute> implements ProductAttributeService {

	@Resource
	private ProductAttributeMapper productAttributeMapper;
	/**
	 * 查出这个属性所有分类属性
	 * @param cid
	 * @param type
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfoVo getCategoryAttribute(Long cid,Integer type,Integer pageNum,Integer pageSize){
		IPage<ProductAttribute> page = productAttributeMapper.selectPage(new Page<ProductAttribute>(pageNum,pageSize),new QueryWrapper<ProductAttribute>().eq("product_attribute_category_id",cid).eq("type",type));
		return PageInfoVo.getVo(page,pageNum,pageSize);
	}

}
