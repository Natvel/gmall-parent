package com.hz.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hz.gmall.pms.entity.Brand;
import com.hz.gmall.pms.mapper.BrandMapper;
import com.hz.gmall.pms.service.BrandService;
import com.hz.gmall.vo.PageInfoVo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * <p>
 * 品牌表 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
@Service
@Component
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

	@Resource
	BrandMapper brandMapper;
	@Override
	public PageInfoVo brandPageInfo(String keyword,Integer pageNum,Integer pageSize){
		QueryWrapper<Brand> name = null;
		if(!StringUtils.isEmpty(keyword)){
			name = new QueryWrapper<Brand>().like("name",keyword);
		}
		IPage<Brand> brandIPage = brandMapper.selectPage(new Page<Brand>(pageNum.longValue(),pageSize.longValue()),name);
		PageInfoVo pageInfoVo = new PageInfoVo(brandIPage.getTotal(),brandIPage.getPages(),pageSize.longValue(),brandIPage.getRecords(),brandIPage.getCurrent());
		return pageInfoVo;
	}
}
