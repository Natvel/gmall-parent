package com.hz.gmall.pms;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * 配置整合duobbo
 * 配置mybatis-plus
 * 1 导jar包
 * 2 创建日志文件
 * 3 elasticsearch创建索引
 * 设计模式 模板设置
 * JDBCTemplate RestTemplate RedisTemplate
 * RedisTemplate 注入这个操作的组件XXXAutoConfiguration
 * 整合Redis两大步
 *  引入starter-data-redis
 *  配置文件配置相关信息
 * 3.解决事务最终方案
 *  3.1导入aop包 开启代理对象的相关功能
 *  3.2 获取当前类真正的代理对象 去掉方法即可
 *
 */
@EnableAspectJAutoProxy(exposeProxy = true)
@EnableDubbo
@MapperScan(basePackages = "com.hz.gmall.pms.mapper")
@SpringBootApplication
public class GmallPmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallPmsApplication.class, args);
	}

}
