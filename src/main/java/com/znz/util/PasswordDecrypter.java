package com.znz.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class PasswordDecrypter {

	private StandardPBEStringEncryptor encrypter;
	private String key;

	public PasswordDecrypter(){

	}
	public PasswordDecrypter(String key) {
		this.key = key;
	}
	public String decrypt(String encryptedString) throws Exception {
		try {
			this.encrypter = new StandardPBEStringEncryptor();
			encrypter.setPassword(this.key);
			String decrypted = this.encrypter.decrypt(encryptedString);
			return decrypted;
		} catch (Exception e) {
            throw new Exception("解密失败");
		}
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public  static  void main(String args[]){
		//8169A6AFED63847062CEC807A9F7CFEA
		Md5PasswordEncoder md5PasswordEncoder = new Md5PasswordEncoder();
		System.out.println(md5PasswordEncoder.encodePassword("ht","znz"));
	}
	
	
}
