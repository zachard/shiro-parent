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

package com.zachard.shiro.hello.strategy;

import java.util.Collection;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.pam.AbstractAuthenticationStrategy;
import org.apache.shiro.realm.Realm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实现只有一个Realm验证成功时才成功的策略
 * <p>主要通过继承AbstractAuthenticationStrategy并覆写其方法实现<br/>
 * 一定要注意是在哪个方法里面实现怎样的逻辑</p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class OnlyOneSuccessfulStrategy extends AbstractAuthenticationStrategy {
	
	private static final Logger logger = LoggerFactory.getLogger(OnlyOneSuccessfulStrategy.class);
	
	/**
	 * 在所有Realm验证之前调用
	 * 
	 * @param realms  用于验证token的realm集合
	 * @param token   登录token信息
	 * @return        认证信息的空对象
	 */
	@Override
	public AuthenticationInfo beforeAllAttempts(Collection<? extends Realm> realms, 
			AuthenticationToken token) throws AuthenticationException {
		//返回一个空的AuthenticationInfo用于填充Realms的认证信息
		return new SimpleAuthenticationInfo();
	}
	
	/**
	 * 在每个Realm验证之前调用
	 * 
	 * @param realm   用于验证token的realm
	 * @param token   登录token信息
	 * @param aggregate  用于多个Realm之间认证的聚合认证信息对象
	 * @return        在认证过程中用于传入下一个Realm处理的aggregate参数
	 */
	@Override
	public AuthenticationInfo beforeAttempt(Realm realm, AuthenticationToken token, 
			AuthenticationInfo aggregate) throws AuthenticationException {
		//aggregate 会在返回之前合并认证信息
		return aggregate;
	}
	
	/**
	 * 在每个Realm验证之前调用
	 * 
	 * @param realm   用于验证token的realm
	 * @param token   登录token信息
	 * @param singleRealmInfo  单个Realm验证后的返回认证信息
	 * @param aggregateInfo    多个Realm验证时，聚合了所有realm认证信息的对象
	 * @param t                当方法正常返回时，返回null，否则为验证过程中Realm抛出的异常
	 * @return                 返回的认证信息(应该是聚合后，然后传入下一个realm的aggregateInfo)
	 */
	@Override
	public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo, 
			AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
		AuthenticationInfo authenticationInfo;
		
		if (singleRealmInfo == null) {
			authenticationInfo = aggregateInfo;
		} else {
			if (aggregateInfo == null) {
				authenticationInfo = singleRealmInfo;
			} else {
				// 当单个realm认证信息及聚合的认证信息都不为null时，将它们合并
				authenticationInfo = merge(singleRealmInfo, aggregateInfo);
				
				/*
				 * 之前想过将if结构移到position 1处来验证，来确保在没有进入到这个else结构时验证authenticationInfo
				 * <p>但这里其实是没有必要的，因为每个realm都会经过这个方法，如果有问题，在之前的realm调用就会出现异常<br/>
				 * 甚至这种写法还会出现空指针异常(当第一个realm验证失败)</p>
				 */
				if (authenticationInfo.getPrincipals().getRealmNames().size() != 1) {
					logger.info(authenticationInfo.getPrincipals().getRealmNames().toString());
					throw new AuthenticationException("类型为[" + token.getClass() + "]" + ", 不能被配置的Realm认证通过" + 
					              "请确认只有一个realm能够认证此token");
				}
			}
		}
		
		//position 1
//		if (authenticationInfo.getPrincipals().getRealmNames().size() != 1) {
//			logger.info(authenticationInfo.getPrincipals().getRealmNames().toString());
//			throw new AuthenticationException("类型为[" + token.getClass() + "]" + ", 不能被配置的Realm认证通过" + 
//			              "请确认只有一个realm能够认证此token");
//		}
		
		return authenticationInfo;
	}
	
	/**
	 * 在所有Realm验证之后调用
	 * 
	 * @param toekn     登录token信息
	 * @param aggregate 验证过程中所有realm返回的认证信息的聚合
	 * @return          最终的认证信息，返回给Authenticator的authenticate()方法
	 */
	@Override
	public AuthenticationInfo afterAllAttempts(AuthenticationToken token, 
			AuthenticationInfo aggregate) throws AuthenticationException {
		return aggregate;
	}

}
