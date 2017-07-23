/*
 *  Copyright 2015-2017 zachard, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.zachard.shiro.realm.util;

import java.util.ResourceBundle;

import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * JdbcTemplate工厂类
 * <p>提供创建JdbcTemplate对象的工厂方法</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class JdbcTemplateFactory {
	
	/**
	 * JdbcTemplate对象
	 */
	private static JdbcTemplate jdbcTemplate;
	
	/**
	 * 加载数据库属性对象
	 */
	private static ResourceBundle bundle = ResourceBundle.getBundle("jdbc");
	
	/**
	 * 创建JdbcTemplate的工厂方法
	 * <p>采用了非线程安全的单例模式</p>
	 * 
	 * @return  JdbcTemplate对象
	 */
	public static JdbcTemplate getJdbcTemplateInstance() {
		
		if (jdbcTemplate == null) {
			jdbcTemplate = newJdbcTemplate();
		}
		
		return jdbcTemplate;
	}
	
	/**
	 * 实例化JdbcTemplate对象的方法
	 * 
	 * @return    JdbcTemplate实例
	 */
	private static JdbcTemplate newJdbcTemplate() {
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(bundle.getString("driverClassName"));
		dataSource.setUrl(bundle.getString("url"));
		dataSource.setUsername(bundle.getString("username"));
		dataSource.setPassword(bundle.getString("password"));
		
		return new JdbcTemplate(dataSource);
	}

}
