package com.hz.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hz.gmall.pms.entity.Brand;
import com.hz.gmall.vo.PageInfoVo;

/**
 * <p>
 * 品牌表 服务类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
public interface BrandService extends IService<Brand> {

	PageInfoVo brandPageInfo(String keyword,Integer pageNum,Integer pageSize);
}
