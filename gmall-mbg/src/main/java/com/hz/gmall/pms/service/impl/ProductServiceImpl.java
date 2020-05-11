package com.hz.gmall.pms.service.impl;

import com.hz.gmall.pms.entity.Product;
import com.hz.gmall.pms.mapper.ProductMapper;
import com.hz.gmall.pms.service.ProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author Enzo
 * @since 2020-03-30
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

}
