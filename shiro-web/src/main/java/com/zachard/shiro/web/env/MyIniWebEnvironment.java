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

package com.zachard.shiro.web.env;

import javax.servlet.Filter;

import org.apache.shiro.util.ClassUtils;
import org.apache.shiro.web.env.IniWebEnvironment;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilter;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;

/**
 * 通过ini文件获取shiro web环境配置
 * <p>主要用于演示自定义拦截器处理的实现</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class MyIniWebEnvironment extends IniWebEnvironment {
	
	/**
	 * 创建自定义实现拦截器链处理对象
	 * 
	 * @return   自定义的拦截器链处理对象
	 */
	@Override
    protected FilterChainResolver createFilterChainResolver() {
		
        /* 1、创建FilterChainResolver
         * 用于处理基于url路径匹配的拦截器链的对象
         */
        PathMatchingFilterChainResolver filterChainResolver = new PathMatchingFilterChainResolver();
        
        /* 2、创建FilterChainManager
         * 用户维护过拦截器名称及拦截器对象的管理对象
         */
        DefaultFilterChainManager filterChainManager = new DefaultFilterChainManager();
        
        /* 3、注册Filter
         * 将shiro默认的拦截器注册到拦截器管理器
         * DefaultFilter枚举中包含了shiro提供的默认拦截器
         */
        for(DefaultFilter filter : DefaultFilter.values()) {
            filterChainManager.addFilter(filter.name(), (Filter) ClassUtils.newInstance(filter.getFilterClass()));
        }
        
        /* 4、注册URL-Filter的映射关系
         * 设置拦截器默认属性
         */
        filterChainManager.addToChain("/login.jsp", "authc");
        filterChainManager.addToChain("/unauthorized.jsp", "anon");
        filterChainManager.addToChain("/**", "authc");
        filterChainManager.addToChain("/**", "roles", "admin");

        // 5、设置Filter的属性
        FormAuthenticationFilter authcFilter = (FormAuthenticationFilter) filterChainManager.getFilter("authc");
        authcFilter.setLoginUrl("/login.jsp");
        RolesAuthorizationFilter rolesFilter = (RolesAuthorizationFilter) filterChainManager.getFilter("roles");
        rolesFilter.setUnauthorizedUrl("/unauthorized.jsp");

        filterChainResolver.setFilterChainManager(filterChainManager);
        
        return filterChainResolver;
//        return super.createFilterChainResolver();
    }

}
