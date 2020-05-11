package com.hz.gmall.pms.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Enzo
 * Created 2020/3/27.
 * 数据源配置类
 * SpringBoot引入某个场景 这个场景就会自动配置好
 */
@Configuration
public class PmsDataSourceConfig{
	@Bean
	public DataSource dataSource() throws IOException, SQLException{
		File file = ResourceUtils.getFile("classpath:sharding-jdbc.yml");
		DataSource dataSource = MasterSlaveDataSourceFactory.createDataSource(file);
		return dataSource;
	}


	@Bean
	public PaginationInterceptor paginationInterceptor() {
		PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
		// 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
		// paginationInterceptor.setOverflow(false);
		// 设置最大单页限制数量，默认 500 条，-1 不受限制
		// paginationInterceptor.setLimit(500);
		// 开启 count 的 join 优化,只针对部分 left join
		/*paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
		return paginationInterceptor;*/
		return paginationInterceptor;
	}
}
