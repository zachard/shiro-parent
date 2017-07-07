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

import org.apache.shiro.codec.Base64;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base64及16进制字符串编码/解码测试
 * <p></p>
 *
 * @author zachard
 * @version 1.0.0
 */
public class EncryptionTest {
	
	private static final Logger logger = LoggerFactory.getLogger(EncryptionTest.class);

	/**
	 * 测试采用base64对字符串进行编码解码
	 */
	@Test
	public void testBase64() {
		String str = "iamacode";
		logger.info("Base64编码前的字符串为: " + str);
		
		String encodeStr = Base64.encodeToString(str.getBytes());
		logger.info("Base64编码后的字符串为: " + encodeStr);
		
		String decodeStr = Base64.decodeToString(encodeStr);
		logger.info("Base64解码后的字符串为: " + decodeStr);
		
		Assert.assertEquals(str, decodeStr);
	}
	
	/**
	 * 测试通过16进制编码/解码字符串
	 */
	@Test
	public void testHex() {
		String password = "iampassword";
		logger.info("Hex编码前的字符串为: " + password);
		
		String encodeStr = Hex.encodeToString(password.getBytes());
		logger.info("Hex编码后的字符串为: " + encodeStr);
		
		String decodeStr = new String(Hex.decode(encodeStr));
		logger.info("Hex解码后的字符串为: " + decodeStr);
		
		Assert.assertEquals(password, decodeStr);
	}
	
	/**
	 * 测试Md5进行加密
	 * <p>为了使密码不易于破解，在加密时加入salt<br/>
	 * 经过MD5解密网站的测试，经salt加密之后不便于破解</p>
	 * 
	 */
	@Test
	public void testMd5() {
		String password = "iampassword";
		String salt = "salt";
		String encodeStr = new Md5Hash(password, salt).toString();
		logger.info("Md5加密后的字符串为: " + encodeStr);
	}
	
	/**
	 * 测试Sha256进行加密
	 */
	@Test
	public void testSha256() {
		String password = "iampassword";
		String salt = "salt";
		String encodeStr = new Sha256Hash(password, salt).toString();
		logger.info("Sha256加密后的字符串为: " + encodeStr);
	}

}
