package com.example;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
public class AppConfig {
	@Autowired
	DataSourceProperties properties;
	DataSource dataSource;

	@ConfigurationProperties(prefix = DataSourceAutoConfiguration.CONFIGURATION_PREFIX)
	@Bean(destroyMethod = "close")
	DataSource realDataSource() {
		DataSourceBuilder factory = DataSourceBuilder
				.create(this.properties.getClassLoader())
				.url(this.properties.getUrl())
				.username(this.properties.getUsername())
				.password(this.properties.getPassword());
		this.dataSource = factory.build();
		return this.dataSource;
	}

	@Bean
	DataSource dataSource() {
		return new Log4jdbcProxyDataSource(this.dataSource);
	}

	/*
	 * 文字コード変換を行うフィルタ。
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter filter = new CharacterEncodingFilter();
		filter.setEncoding("UTF-8");
		return filter;
	}

	/*
	 * Cross-Origin Resource Sharing(CORS) を行うフィルタ。
	 */
	@Bean
	Filter corsFilter() {
		return new Filter() {

			@Override
			public void init(FilterConfig filterConfig) throws ServletException {
			}

			@Override
			public void doFilter(ServletRequest request,
					ServletResponse response, FilterChain chain)
					throws IOException, ServletException {
				HttpServletRequest req = (HttpServletRequest) request;
				HttpServletResponse res = (HttpServletResponse) response;
				String method = req.getMethod();
				res.setHeader("Access-Control-Allow-Origin", "*");
				res.setHeader("Access-Control-Allow-Methods",
						"POST,GET,OPTIONS,DELETE");
				res.setHeader("Access-Control-Max-Age", Long.toString(60 * 60));
				res.setHeader("Access-Control-Allow-Credentials", "true");
				res.setHeader(
						"Access-Control-Allow-Headers",
						"Origin,Accept,X-Requested-With,"
								+ "Content-Type,Access-Control-Request-Method,"
								+ "Access-Control-Request-Headers,Authorization");
				if ("OPTIONS".equals(method)) {
					res.setStatus(HttpStatus.OK.value());
				} else {
					chain.doFilter(req, res);
				}
			}

			@Override
			public void destroy() {
			}
		};
	}
}
