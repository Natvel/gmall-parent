package com.hz.gmall.pms.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hz.gmall.constant.EsConstant;
import com.hz.gmall.pms.entity.*;
import com.hz.gmall.pms.mapper.*;
import com.hz.gmall.pms.service.ProductService;
import com.hz.gmall.to.es.EsProduct;
import com.hz.gmall.to.es.EsProductAttributeValue;
import com.hz.gmall.to.es.EsSkuProductInfo;
import com.hz.gmall.vo.PageInfoVo;
import com.hz.gmall.vo.PmsProductParam;
import com.hz.gmall.vo.PmsProductQueryParam;
import io.searchbox.client.JestClient;
import io.searchbox.core.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 商品信息 服务实现类
 * </p>
 *
 * @author Lfy
 * @since 2020-03-27
 */
@Slf4j
@Service
@Component
public class ProductServiceImpl extends ServiceImpl<ProductMapper,Product> implements ProductService{

	@Resource
	ProductMapper productMapper;

	@Resource
	private ProductAttributeValueMapper productAttributeValueMapper;

	@Resource
	private ProductFullReductionMapper productFullReductionMapper;

	@Resource
	private ProductLadderMapper productLadderMapper;

	@Resource
	private SkuStockMapper skuStockMapper;

	@Resource
	private JdkService jdkService;

	@Resource
	private JestClient jestClient;
	/**
	 * 当前线程共享同样的数据
	 */
	ThreadLocal<Long> threadLocal = new ThreadLocal<>();


	//ThreadLocal的原理
	private Map<Thread,Long> map = new HashMap<>();

	@Override
	public PageInfoVo productPageInfo(PmsProductQueryParam param,Long pageNum,Long pageSize){
		QueryWrapper<Product> wrapper = new QueryWrapper<>();
		if(param.getBrandId() != null){
			//前端传了
			wrapper.eq("brand_id",param.getBrandId());
		}

		if(! StringUtils.isEmpty(param.getKeyword())){
			wrapper.like("name",param.getKeyword());
		}

		if(param.getProductCategoryId() != null){
			wrapper.eq("product_category_id",param.getProductCategoryId());
		}

		if(! StringUtils.isEmpty(param.getProductSn())){
			wrapper.like("product_sn",param.getProductSn());
		}
		if(param.getPublishStatus() != null){
			wrapper.eq("publish_status",param.getPublishStatus());
		}

		if(param.getVerifyStatus() != null){
			wrapper.eq("verify_status",param.getVerifyStatus());
		}

		IPage<Product> page = productMapper.selectPage(new Page<Product>(pageNum,pageSize),wrapper);
		PageInfoVo pageInfoVo = new PageInfoVo(page.getTotal(),page.getPages(),pageSize,page.getRecords(),page.getCurrent());

		return pageInfoVo;
	}

	/**
	 * 考虑事务
	 * 哪些事务是需要回滚的 哪些是出错了不用回滚的
	 * 商品的核心信息 基本数据 sku 保存的时候不要受到无关信息的影响
	 * 核心信息
	 * @param productParam
	 * 2事务的的传播行为
	 * 当前方法的事务是否和别人公用一个事务
	 * Propagation propagation() default Propagation.REQUIRED;
	 * REQUIRED(必须的)
	 * 以前有事务 就和之前的事共用一个事务 如果没有事务就兴建一个事务
	 *
	 * REQUIRES_NEW
	 * 创建一个新的事务 如果以前有事务 暂停事务创建一个新的事务
	 * SUPPORTS
	 * 之前有事务就以事务方法运行 没事务也可以
	 *
	 * NOT_SUPPORTED
	 *  不支持在事务 如果有事务就挂起
	 * MANDATORY
	 *  必须要有事务 没事务就报错
	 * NEVER
	 *  重不开启事务 如果有事务就报错
	 * NESTED
	 * 开启一个子事务 需要支持还原点功能的数据库才行
	 *
	 *
	 *        Required：坐老王车
	 *            Requires_new：一定得开车，开新的
	 *
	 *            SUPPORTS：用车，有车就用，没车走路；
	 *            MANDATORY：用车，没车就骂街。。。
	 *
	 *            NOT_SUPPORTED：不支持用车。有车放那不用
	 *            NEVER：从不用车，有车抛异常
	 *  传播行为过程中，只要Requires_new被执行过就一定成功，不管后面出不出问题。异常机制还是一样的，出现异常代码以后不执行。
	 *       Required只要感觉到异常就一定回滚。和外事务是什么传播行为无关。
	 *
	 *       传播行为总是来定义，当一个事务存在的时候，他内部的事务该怎么执行。
	 *  事务在spring中是怎么做的
	 *  动态代理
	 *
	 * 事务的问题 service自己调用自己的方法 无法加上真自己内部类的事务
	 *  从容器中把我们的容器拿到
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveProduct(PmsProductParam productParam){
		ProductServiceImpl proxy = (ProductServiceImpl) AopContext.currentProxy();
		//pms_product 商品基本信息
		Product product = proxy.saveBaseInfo(productParam);
		log.info("刚才商品的id{}",product.getId());
		//pms_product_attribute_value保存商品对应所有属性的值
		proxy.saveProductAttributeValue(productParam,product);
		//保存商品的满减信息
		proxy.saveFullReduction(productParam,product);
		//满减表
		proxy.saveProductLadder(productParam,product);
		//sku库存表
		proxy.saveSkuStack(productParam,product);

	}


	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveSkuStack(PmsProductParam productParam,Product product){
		List<SkuStock> skuStockList = productParam.getSkuStockList();
		skuStockList.forEach((skuStock) -> {
			skuStock.setProductId(product.getId());
			if(skuStock.getSkuCode() == null){
				//skuCode是空的
				skuStock.setSkuCode(String.valueOf(new Random().nextInt(100) + 1
				));
			}
			skuStockMapper.insert(skuStock);
		});
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = FileNotFoundException.class,
	  noRollbackFor = NullPointerException.class)
	public void saveProductLadder(PmsProductParam productParam,Product product){
		List<ProductLadder> productLadderList = productParam.getProductLadderList();
		productLadderList.forEach((productLadder) -> {
			productLadder.setProductId(product.getId());
			productLadderMapper.insert(productLadder);
		});
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveFullReduction(PmsProductParam productParam,Product product){

		List<ProductFullReduction> productFullReductionList = productParam.getProductFullReductionList();
		productFullReductionList.forEach((reduction) -> {
			reduction.setProductId(product.getId());
			productFullReductionMapper.insert(reduction);
		});
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void saveProductAttributeValue(PmsProductParam productParam,Product product){
		List<ProductAttributeValue> productAttributeValueList = productParam.getProductAttributeValueList();
		productAttributeValueList.forEach((item) -> {
			item.setProductId(product.getId());
			productAttributeValueMapper.insert(item);
		});
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Product saveBaseInfo(PmsProductParam productParam){
		Product product = new Product();
		BeanUtils.copyProperties(productParam,product);
		//pms_product 商品基本信息
		productMapper.insert(product);
		//mybatis能自动获取商品id的值
		return product;
	}


	/**
	 * 第87讲
	 * PmsProductParam 是vo层无法获取这个商品的sku值
	 * 所以要将这个商品的sku统一进行封装到es中
	 * 表开始设计时只是将基本属性进行封装 并没有全部进行整合
	 * 该做法是为了统一数据保存到es中
	 *
	 * @param ids
	 * @param recommendStatus
	 */


	@Override
	public void updateStatusByIds(List<Long> ids,Integer recommendStatus){

		//下架做删除操作
		if(recommendStatus == 0){
			ids.forEach((id) -> {
				setProductPublicStatus(recommendStatus,id);
				deleteProductFromEs(id);
			});

		}else{
			//对于数据库是修改商品状态位
			//上架做数据插入操作
			ids.forEach((id) -> {
				setProductPublicStatus(recommendStatus,id);
				saveProductToEs(recommendStatus,id);
			});
		}

	}

	private void deleteProductFromEs(Long id){
		Delete build = new Delete.Builder(id.toString()).index(EsConstant.PRODUCT_INDEX)
		  .type(EsConstant.PRODUCT_INFO_TYPE).build();
		try{
			DocumentResult execute = jestClient.execute(build);
			boolean succeeded = execute.isSucceeded();
			/*if(!succeeded){
				log.info("下架失败");
				deleteProductFromEs(id);
			}*/
		}catch(IOException e){
			//deleteProductFromEs(id);
			e.printStackTrace();
		}

	}

	private void saveProductToEs(Integer recommendStatus,Long id){
		Product info = productInfo(id);
		info.setPublishStatus(recommendStatus);
		EsProduct esProduct = new EsProduct();
		BeanUtils.copyProperties(info,esProduct);
		//把商品所有信息查出来
		//对于es要保存商品信息 还要保存这个商品的sku
		List<SkuStock> skuStocks = skuStockMapper.selectList(new QueryWrapper<SkuStock>().eq("product_id",id));
		List<EsSkuProductInfo> list = new ArrayList<>(skuStocks.size());
		//查出当前商品的sku属性
		List<ProductAttribute> attributeValueList =
		  productAttributeValueMapper.selectProductBaseAttrAndName(id);
		skuStocks.forEach((skuStock) -> {
			EsSkuProductInfo productInfo = new EsSkuProductInfo();
			BeanUtils.copyProperties(skuStock,productInfo);
			//查出这个sku所有销售属性的值
			String subTitle = esProduct.getName();
			if(skuStock.getSp1() != null){
				subTitle += " " + skuStock.getSp1();
			}
			if(skuStock.getSp2() != null){
				subTitle += " " + skuStock.getSp2();
			}
			if(skuStock.getSp3() != null){
				subTitle += " " + skuStock.getSp3();
			}
			productInfo.setSkuTitle(subTitle);
			ArrayList<EsProductAttributeValue> arrayList = new ArrayList<>();

			/**
			 * sp3属性未添加
			 * stock 中未添加颜色和尺寸 所以将信息查询进行封装
			 * attributeValue中
			 */
			for(int i = 0;i < attributeValueList.size();i++){
				EsProductAttributeValue attributeValue = new EsProductAttributeValue();
				attributeValue.setName(attributeValueList.get(i).getName());
				attributeValue.setProductId(id);
				attributeValue.setProductAttributeId(attributeValueList.get(i).getId());
				attributeValue.setType(attributeValueList.get(i).getType());
				if(i == 0){
					attributeValue.setValue(skuStock.getSp1());
				}else if(i == 2){
					attributeValue.setValue(skuStock.getSp2());
				}else{
					attributeValue.setValue(skuStock.getSp3());
				}
				//attributeValue.setValue();
				arrayList.add(attributeValue);
			}

			//sku有多个属性 颜色尺码
			productInfo.setAttributeValues(arrayList);
			list.add(productInfo);
			//查出销售属性的名
		});
		List<EsProductAttributeValue> valueList = productAttributeValueMapper.selectProductBaseAttrAndValue(id);
		esProduct.setAttrValueList(valueList);
		esProduct.setSkuProductInfos(list);
		try{
			Index build = new Index.Builder(esProduct)
			  .index(EsConstant.PRODUCT_INDEX).type(EsConstant.PRODUCT_INFO_TYPE)
			  .id(id.toString()).build();
			DocumentResult execute = jestClient.execute(build);

			boolean succeeded = execute.isSucceeded();
			log.info("Es中 id{}商品上架成功",id);
			if(! succeeded){
				log.info("商品未保存成功 开始重试");
			//	saveProductToEs(recommendStatus,id);
			}
		}catch(Exception e){
			log.error("Es商品保存异常{},id为{}",e.getMessage(),id);
		//	saveProductToEs(recommendStatus,id);
		}
	}

	private void setProductPublicStatus(Integer recommendStatus,Long id){
		//javaBean应该都用包装类型
		Product product = new Product();
		product.setId(id);
		product.setPublishStatus(recommendStatus);
		productMapper.updateById(product);
	}

	@Override
	public Product productInfo(Long id){
		return productMapper.selectById(id);
	}

	@Override
	public EsProduct productAllInfo(Long id){
		 EsProduct esProduct = null;
		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(QueryBuilders.termQuery("id",id));
		Search build = new Search.Builder(builder.toString()).addIndex(EsConstant.PRODUCT_INDEX)
		  .addType(EsConstant.PRODUCT_INFO_TYPE).build();
		try{
			SearchResult execute = jestClient.execute(build);
			List<SearchResult.Hit<EsProduct,Void>> hits = execute.getHits(EsProduct.class);
			esProduct = hits.get(0).source;

		}catch(IOException e){
			e.printStackTrace();
		}
		return esProduct;
	}

	@Override
	public EsProduct productSkuInfo(Long id){
		EsProduct esProduct = null;
		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(QueryBuilders.nestedQuery("skuProductInfos",
		  QueryBuilders.termQuery("skuProductInfos.id",id),ScoreMode.None
		));
		Search build = new Search.Builder(builder.toString()).addIndex(EsConstant.PRODUCT_INDEX)
		  .addType(EsConstant.PRODUCT_INFO_TYPE).build();
		try{
			SearchResult execute = jestClient.execute(build);
			List<SearchResult.Hit<EsProduct,Void>> hits = execute.getHits(EsProduct.class);
			esProduct = hits.get(0).source;

		}catch(IOException e){
			e.printStackTrace();
		}
		return esProduct;
	}

	@Override
	public SkuStock skuInfoById(Long skuId){
		return skuStockMapper.selectById(skuId);
	}

}
