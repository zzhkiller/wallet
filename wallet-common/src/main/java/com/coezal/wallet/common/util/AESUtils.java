package com.coezal.wallet.common.util;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Version 1.0
 * Created by lll on 2019-09-04.
 * Description
 * <pre>
 *   aes 加解密工具类
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class AESUtils {

  private static final String KEY_ALGORITHM = "AES";
  private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法

  /**
   * AES 加密操作
   *
   * @param content  待加密内容
   * @param password 加密密码
   * @return 返回Base64转码后的加密数据
   */
  public static String encrypt(String content, String password) {
    try {
      Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器
      byte[] byteContent = content.getBytes("utf-8");
      cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器
      byte[] result = cipher.doFinal(byteContent);// 加密
      return Base64.encodeBase64String(result);//通过Base64转码返回
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  /**
   * AES 解密操作
   *
   * @param content
   * @param password
   * @return
   */
  public static String decrypt(String content, String password) {
    try {
      //实例化
      Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
      //使用密钥初始化，设置为解密模式
      cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
      //执行操作
      byte[] result = cipher.doFinal(Base64.decodeBase64(content));
      return new String(result, "utf-8");
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  /**
   * 生成加密秘钥
   *
   * @return
   */
  private static SecretKeySpec getSecretKey(String password) {
    //返回生成指定算法密钥生成器的 KeyGenerator 对象
    KeyGenerator kg = null;
    try {
      kg = KeyGenerator.getInstance(KEY_ALGORITHM);
      //AES 要求密钥长度为 128
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      random.setSeed(password.getBytes());
      kg.init(128, random);
      //生成一个密钥
      SecretKey secretKey = kg.generateKey();
      return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
    } catch (NoSuchAlgorithmException ex) {
      ex.printStackTrace();
    }
    return null;
  }

//  public static void main(String[] args){
//    System.out.println("原始字符串为:  abdndfj");
//    String  encryStr = encrypt("abdndfj", "123abc");
//    System.out.println("加密后字符串为:  "+encryStr);
//
//    String  decryptStr = decrypt(encryStr, "123abc");
//
//    System.out.println("解密后字符串为:  " + decryptStr);
//
//  }
}


