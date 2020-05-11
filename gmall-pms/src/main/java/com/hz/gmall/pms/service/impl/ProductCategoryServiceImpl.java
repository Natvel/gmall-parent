package com.hz.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hz.gmall.constant.SysCacheConstant;
import com.hz.gmall.pms.mapper.ProductCategoryMapper;
import com.hz.gmall.pms.entity.ProductCategory;
import com.hz.gmall.pms.service.ProductCategoryService;
import com.hz.gmall.vo.PmsProductCategoryWithChildrenItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 产品分类 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
@Component
@Service
@Slf4j
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryMapper,ProductCategory> implements ProductCategoryService{

	@Resource
	private RedisTemplate<Object,Object> redisTemplate;
	@Resource
	ProductCategoryMapper productCategoryMapper;

	@Override
	public List<PmsProductCategoryWithChildrenItem> listCategoryWithChilder(Integer categoryId){
		Object cacheMenu = redisTemplate.opsForValue().get(SysCacheConstant.CATEGORY_MENU_CACHE_KEY);
		List<PmsProductCategoryWithChildrenItem> items;
		if(cacheMenu != null){
			log.info("redis存在该缓存----------------------->");
			items = (List<PmsProductCategoryWithChildrenItem>) cacheMenu;
			return items;
		}
		items = productCategoryMapper.listCategoryWithChildren(categoryId);
		redisTemplate.opsForValue().set(SysCacheConstant.CATEGORY_MENU_CACHE_KEY,items);
		log.info("查询条件不为空----------------------->");
		return items;
	}




}
