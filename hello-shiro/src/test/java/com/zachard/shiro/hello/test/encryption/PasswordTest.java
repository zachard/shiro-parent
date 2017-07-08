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

package com.zachard.shiro.hello.test.encryption;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zachard.shiro.hello.test.AbstractTestCase;

/**
 * 用于测试通过自定的Realm及JdbcRealm实现密码加密及解密
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class PasswordTest extends AbstractTestCase {
	
	private static final Logger logger = LoggerFactory.getLogger(PasswordTest.class);

	/**
	 * 根据自定义Realm实现对密码进行加密与解密
	 * <p>注意shiro.ini文件中realm的配置</p>
	 */
	@Test
	public void testEncryptionWithCustomerRealm() {
		login("wang", "wang");
		Subject subject = SecurityUtils.getSubject();
		logger.info("用户身份标识为: " + subject.getPrincipal());
	}
	
	/**
	 * 根据JdbcRealm实现对密码进行加密与解密
	 * <p>注意shiro.ini文件中realm的配置</p>
	 */
	@Test
	public void testEncryptionWithJdbcRealm() {
//		login("wang", "wang");
//		login("zachard", "123456");
		login("zachard", "admin");
		Subject subject = SecurityUtils.getSubject();
		logger.info("用户身份标识为: " + subject.getPrincipal());
	}
	
	/**
	 * 通过MD5算法实现用户的加密解密
	 * <p>需要将生成的密码及salt2存入数据库<br/>
	 * 加密算法为: MD5(MD5(password + username + salt2))</p>
	 */
	@Test
	public void testMD5GeneratePassword() {
		String algorithmName = "md5";
        String username = "zachard";
        String password = "admin";
        String salt1 = username;
        String salt2 = new SecureRandomNumberGenerator().nextBytes().toHex();
        int hashIterations = 2;
        SimpleHash hash = new SimpleHash(algorithmName, password, salt1 + salt2, hashIterations);
        String encodedPassword = hash.toHex();
        
        logger.info("随机生成的盐2为: " + salt2);
        logger.info("加密后的密码为: " + encodedPassword);
	}

}
