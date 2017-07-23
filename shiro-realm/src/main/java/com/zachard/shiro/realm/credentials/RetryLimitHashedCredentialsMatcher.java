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

package com.zachard.shiro.realm.credentials;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

/**
 * 用于将限制密码重试次数的类
 * <p>{@link HashedCredentialsMatcher} 用于在Token与用户信息比较之前,<br />
 * 对Token进行哈希计算</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {
	
	/**
	 * 密码重试缓存
	 */
	private Ehcache passwordRetryCache;
	
	/**
	 * 构造密码重试对象
	 */
	public RetryLimitHashedCredentialsMatcher() {
		CacheManager cacheManager = CacheManager.newInstance(CacheManager.class.getResource("/ehcache.xml"));
		passwordRetryCache = cacheManager.getEhcache("passwordRetryCache");
	}
	
	/**
	 * 如果info对象是一个{@link SaltedAuthenticationInfo},则先对token凭证进行哈希<br />
	 * 之后再与已经进行哈希过的info对象进行比较
	 * 
	 * @param  token   登录token信息
	 * @param  info    认证信息
	 * @return 当对token与info值匹配时，返回true，否则返回false
	 */
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		String username = (String) token.getPrincipal();
		Element element = passwordRetryCache.get(username);
		
        if(element == null) {
            element = new Element(username, new AtomicInteger(0));
            passwordRetryCache.put(element);
        }
        
        AtomicInteger retryCount = (AtomicInteger) element.getObjectValue();
        
        //似乎没有看到5次失败后，锁定1小时的代码
        if(retryCount.incrementAndGet() > 5) {
            throw new ExcessiveAttemptsException();
        }

        boolean matches = super.doCredentialsMatch(token, info);
        
        if(matches) {
            passwordRetryCache.remove(username);
        }
        
        return matches;
	}
}
