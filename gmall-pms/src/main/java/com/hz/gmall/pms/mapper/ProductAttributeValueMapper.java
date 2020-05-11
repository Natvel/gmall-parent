package com.hz.gmall.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hz.gmall.pms.entity.ProductAttribute;
import com.hz.gmall.pms.entity.ProductAttributeValue;
import com.hz.gmall.to.es.EsProductAttributeValue;

import java.util.List;

/**
 * <p>
 * 存储产品参数信息的表 Mapper 接口
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
public interface ProductAttributeValueMapper extends BaseMapper<ProductAttributeValue> {

	List<EsProductAttributeValue> selectProductBaseAttrAndValue(Long id);

	List<ProductAttribute> selectProductBaseAttrAndName(Long id);
}
