package com.hz.gmall.pms;

import com.hz.gmall.pms.entity.Brand;
import com.hz.gmall.pms.service.BrandService;
import com.hz.gmall.pms.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallPmsApplicationTests{

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	@Resource
	private RedisTemplate<Object,Object> redisTemplate;

	@Resource
	ProductService productService;

	@Resource
	BrandService brandService;

	@Test
	public void contextLoads(){
		/*Product byId = productService.getById(1);
		System.out.println(byId.getName());*/
		//测试增删改在主库 读取在从库
		/*Brand brand = new Brand();
		brand.setName("测试名字");
		brandService.save(brand);*/
		Brand byId = brandService.getById(53);

		System.out.println(byId.getName() + "成功");
	}


	@Test
	public void redisTemplate(){
		//操作String类型

		/*redisTemplate.opsForValue()
		redisTemplate.opsForHash().
		  redisTemplate.opsForList();*/
		stringRedisTemplate.opsForValue().set("hello","world");
		String hello = stringRedisTemplate.opsForValue().get("hello");
		System.out.println(hello);

	}

	/**
	 * redis默认是序列化的
	 */
	@Test
	public void getRedisTemplateObj(){
	//以后要存对象转换为json字符串去redis中取出来 逆转为对象
		Brand brand = new Brand();
		brand.setName("啊啊啊");
		redisTemplate.opsForValue().set("abc",brand);
		System.out.println(redisTemplate.opsForValue().get("abc"));

	}
}
