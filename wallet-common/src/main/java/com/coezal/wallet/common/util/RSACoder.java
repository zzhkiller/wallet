package com.coezal.wallet.common.util;



import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.coezal.wallet.api.bean.RechargeRequest;
import com.coezal.wallet.exception.RsaException;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Version 1.0
 * Created by lll on 2019-08-28.
 * Description
 * <pre>
 *  非对称加密算法RSA算法组件
 *  非对称算法一般是用来传送对称加密算法的密钥来使用的，相对于DH算法，RSA算法只需要一方构造密钥，不需要
 *  大费周章的构造各自本地的密钥对了。DH算法只能算法非对称算法的底层实现。而RSA算法算法实现起来较为简单
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class RSACoder {
  //非对称密钥算法
  public static final String KEY_ALGORITHM = "RSA";


  /**
   * 密钥长度，DH算法的默认密钥长度是1024
   * 密钥长度必须是64的倍数，在512到65536位之间
   */
  private static final int KEY_SIZE = 512;
  //公钥
  private static final String PUBLIC_KEY = "RSAPublicKey";

  //私钥
  private static final String PRIVATE_KEY = "RSAPrivateKey";

  private static final String PRIVATE_KEY_STR = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIrzh97MQKMEmANyDX+X1NG7DTszFgMbO6KlfincsifH+29hXROZORQIFQCUVphbng6AdzDh+KTYdgphKXbNWoSrYQupLH+ZccREDnRI/x8VGneCBGTotcyL02SWJxpXpUsD//eNOWgi495mNnmooZ1J0NY3Nk7c0cZ0sSXPR6RbAgMBAAECgYALDgD7SsjBr3XgoExOoGfAH9+XnCLeMGZ4NC5rajGKVLC+VcKv8nrGCzaQizywdmmGwdW5v+CmTMpnXP+Nghz3Xx6vbfKLA93jxPEYayg7LxqvZMpz9MZjf8h8zWugkAQD6/ElFj0KiLJoysdiqN70nCnRojod1ka0ZEiZRIUGwQJBAMC1XYiMfan29qVHV/K3fIUyzI/PMzyQJWTm/SNFd3jvEZopagrvYkxRC7koLoeCvKslcQ2O4gzgpdY+iqPVEBECQQC4llgsghu9v1zZW+kp9gQz5pAp/cYvOS5DTZctVjJzpgZaXmg5a6qgOWPzD8tDm1GW/TQP1yVOMmVO9SHXYFmrAkBWPZQjNMRUGOqeYsQwIf8+2NIFFbQXSWcCtgDZFRB3dX3KIPiM9j5mauq1LQ9No6ttaC8k4ym0m6B7tbdzxDkRAkBh+SKp1REmYIjGsbsLU5IdfhYsw47Kh94fSPKh1KuIqKmck5lcSOJSksOTQmHP64Od0Z0tfzNE0wjkpMWmjHRrAkAUyDj0xyN99WfU099LzNmed4KJyKCspgfwZXE+afgplXrw3TBnM1ZngfZJuvEPOAZ2sMAai3CjESFra5Z1cvCx";


  /**
   * 初始化密钥对
   *
   * @return Map 甲方密钥的Map
   */
  public static Map<String, Object> initKey() throws Exception {
    //实例化密钥生成器
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
    //初始化密钥生成器
    keyPairGenerator.initialize(KEY_SIZE);
    //生成密钥对
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    //甲方公钥
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    //甲方私钥
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    //将密钥存储在map中
    Map<String, Object> keyMap = new HashMap<String, Object>();
    keyMap.put(PUBLIC_KEY, publicKey);
    keyMap.put(PRIVATE_KEY, privateKey);
    return keyMap;

  }


  /**
   * 私钥加密
   *
   * @param data 待加密数据
   * @param key       密钥
   * @return byte[] 加密数据
   */
  public static byte[] encryptByPrivateKey(byte[] data, byte[] key) throws Exception {

    //取得私钥
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    //生成私钥
    PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
    //数据加密
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.ENCRYPT_MODE, privateKey);
    return cipher.doFinal(data);
  }




  /**
   * 公钥加密
   *
   * @param data 待加密数据
   * @param key       密钥
   * @return byte[] 加密数据
   */
  public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {

    //实例化密钥工厂
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    //初始化公钥
    //密钥材料转换
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
    //产生公钥
    PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

    //数据加密
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    return cipher.doFinal(data);
  }

  /**
   * 私钥解密
   *
   * @param data 待解密数据
   * @param key  密钥
   * @return byte[] 解密数据
   */
  public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {
    //取得私钥
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    //生成私钥
    PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
    //数据解密
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    return cipher.doFinal(data);
  }
  /**
   * 私钥分段解密
   *
   * @param data 待解密数据
   * @param key  密钥
   * @return byte[] 解密数据
   */
  public static String decryptByPrivateKey1(String data) throws Exception {
    //秘钥
    byte[] key=Base64.decodeBase64(PRIVATE_KEY_STR);
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    //生成私钥
    PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
    //数据解密
    Cipher ci = Cipher.getInstance(keyFactory.getAlgorithm());
    ci.init(Cipher.DECRYPT_MODE, privateKey);

    byte[] encryptedData = Base64.decodeBase64(data);
    int inputLen = encryptedData.length;
    int offLen = 0;
    int i = 0;
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    while(inputLen - offLen > 0){
      byte[] cache;
      if(inputLen - offLen > 128){
        cache = ci.doFinal(encryptedData,offLen,128);
      }else{
        cache = ci.doFinal(encryptedData,offLen,inputLen - offLen);
      }
      byteArrayOutputStream.write(cache);
      i++;
      offLen = 128 * i;

    }
    byteArrayOutputStream.close();
    byte[] byteArray = byteArrayOutputStream.toByteArray();
    return new String(byteArray);

  }
  /**
   * 私钥分段加密
   *
   * @param data 待加密数据
   * @param key       密钥
   * @return byte[] 加密数据
   */
  public static String encryptByPrivateKey1(String data, byte[] key) throws Exception {

    //取得私钥
    PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    //生成私钥
    PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
    //数据加密
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.ENCRYPT_MODE, privateKey);
    int inputLen = data.getBytes().length;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    int offset = 0;
    byte[] cache;
    int i = 0;
    // 对数据分段加密
    while (inputLen - offset > 0) {
      if (inputLen - offset > 116) {
        cache = cipher.doFinal(data.getBytes(), offset, 116);
      } else {
        cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
      }
      out.write(cache, 0, cache.length);
      i++;
      offset = i * 116;
    }
    byte[] encryptedData = out.toByteArray();
    out.close();
    // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
    // 加密后的字符串
    return new String(Base64.encodeBase64String(encryptedData));
  }
  /**
   * 公钥解密
   *
   * @param data 待解密数据
   * @param key  密钥
   * @return byte[] 解密数据
   */
  public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {

    //实例化密钥工厂
    KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
    //初始化公钥
    //密钥材料转换
    X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
    //产生公钥
    PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
    //数据解密
    Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
    cipher.init(Cipher.DECRYPT_MODE, pubKey);
    return cipher.doFinal(data);
  }

  /**
   * 取得私钥
   *
   * @param keyMap 密钥map
   * @return byte[] 私钥
   */
  public static byte[] getPrivateKey(Map<String, Object> keyMap) {
    Key key = (Key) keyMap.get(PRIVATE_KEY);
    return key.getEncoded();
  }

  /**
   * 取得公钥
   *
   * @param keyMap 密钥map
   * @return byte[] 公钥
   */
  public static byte[] getPublicKey(Map<String, Object> keyMap) throws Exception {
    Key key = (Key) keyMap.get(PUBLIC_KEY);
    return key.getEncoded();
  }

//  /**
//   * @param args
//   * @throws Exception
//   */
//  public static void main(String[] args) throws Exception {
//    //初始化密钥
//    //生成密钥对
//    Map<String, Object> keyMap = RSACoder.initKey();
//    //公钥
//    byte[] publicKey = RSACoder.getPublicKey(keyMap);
//
//    //私钥
//    byte[] privateKey = RSACoder.getPrivateKey(keyMap);
//
//    String publicKeyStr = Base64.encodeBase64String(publicKey);
//    System.out.println("公钥：/n" + publicKeyStr);
//
//
//    System.out.println("私钥：/n" + Base64.encodeBase64String(privateKey));
//
//    System.out.println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");
//    String str = "RSA密码交换算法";
//    System.out.println("/n===========甲方向乙方发送加密数据==============");
//    System.out.println("原文:" + str);
//    //甲方进行数据的加密
//    byte[] code1 = RSACoder.encryptByPrivateKey(str.getBytes(), privateKey);
//    System.out.println("加密后的数据：" + Base64.encodeBase64String(code1));
//    System.out.println("===========乙方使用甲方提供的公钥对数据进行解密==============");
//    //乙方进行数据的解密
//    byte[] decode1 = RSACoder.decryptByPublicKey(code1, publicKey);
//    System.out.println("乙方解密后的数据：" + new String(decode1) + "/n/n");
//
//    System.out.println("===========反向进行操作，乙方向甲方发送数据==============/n/n");
//
//    str = "乙方向甲方发送数据RSA算法";
//
//    System.out.println("原文:" + str);
//
//    //乙方使用公钥对数据进行加密
//    byte[] code2 = RSACoder.encryptByPublicKey(str.getBytes(), publicKey);
//    System.out.println("===========乙方使用公钥对数据进行加密==============");
//    System.out.println("加密后的数据：" + Base64.encodeBase64String(code2));
//
//    System.out.println("=============乙方将数据传送给甲方======================");
//    System.out.println("===========甲方使用私钥对数据进行解密==============");
//
//    //甲方使用私钥对数据进行解密
//    byte[] decode2 = RSACoder.decryptByPrivateKey(code2, privateKey);
//
//    System.out.println("甲方解密后的数据：" + new String(decode2));
//  }

    public static void main(String[] args) {
      RechargeRequest request=new RechargeRequest();
      request.setCheckcode("6");
      request.setId("6");
      request.setMoney("6.66");
      request.setTime("6");
      request.setTokenname("6");
      request.setUsersign("6");
      request.setWallet("6");
      String str1="666666.666gQ#D63K*QW%U9l@X";
      request.setMd5chk(Md5Util.MD5(str1));
      String jsonObj=JsonUtil.encode(request);
      System.out.println(jsonObj);
      String privateKeyStr="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIrzh97MQKMEmANyDX+X1NG7DTszFgMbO6KlfincsifH+29hXROZORQIFQCUVphbng6AdzDh+KTYdgphKXbNWoSrYQupLH+ZccREDnRI/x8VGneCBGTotcyL02SWJxpXpUsD//eNOWgi495mNnmooZ1J0NY3Nk7c0cZ0sSXPR6RbAgMBAAECgYALDgD7SsjBr3XgoExOoGfAH9+XnCLeMGZ4NC5rajGKVLC+VcKv8nrGCzaQizywdmmGwdW5v+CmTMpnXP+Nghz3Xx6vbfKLA93jxPEYayg7LxqvZMpz9MZjf8h8zWugkAQD6/ElFj0KiLJoysdiqN70nCnRojod1ka0ZEiZRIUGwQJBAMC1XYiMfan29qVHV/K3fIUyzI/PMzyQJWTm/SNFd3jvEZopagrvYkxRC7koLoeCvKslcQ2O4gzgpdY+iqPVEBECQQC4llgsghu9v1zZW+kp9gQz5pAp/cYvOS5DTZctVjJzpgZaXmg5a6qgOWPzD8tDm1GW/TQP1yVOMmVO9SHXYFmrAkBWPZQjNMRUGOqeYsQwIf8+2NIFFbQXSWcCtgDZFRB3dX3KIPiM9j5mauq1LQ9No6ttaC8k4ym0m6B7tbdzxDkRAkBh+SKp1REmYIjGsbsLU5IdfhYsw47Kh94fSPKh1KuIqKmck5lcSOJSksOTQmHP64Od0Z0tfzNE0wjkpMWmjHRrAkAUyDj0xyN99WfU099LzNmed4KJyKCspgfwZXE+afgplXrw3TBnM1ZngfZJuvEPOAZ2sMAai3CjESFra5Z1cvCx";
      String code1 = "";
      try {
        code1 = RSACoder.encryptByPrivateKey1(jsonObj, Base64.decodeBase64(privateKeyStr));
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println("加密后的数据：" +code1);
    }
//public static void main(String[] args) {
//  String privateKeyStr="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIrzh97MQKMEmANyDX+X1NG7DTszFgMbO6KlfincsifH+29hXROZORQIFQCUVphbng6AdzDh+KTYdgphKXbNWoSrYQupLH+ZccREDnRI/x8VGneCBGTotcyL02SWJxpXpUsD//eNOWgi495mNnmooZ1J0NY3Nk7c0cZ0sSXPR6RbAgMBAAECgYALDgD7SsjBr3XgoExOoGfAH9+XnCLeMGZ4NC5rajGKVLC+VcKv8nrGCzaQizywdmmGwdW5v+CmTMpnXP+Nghz3Xx6vbfKLA93jxPEYayg7LxqvZMpz9MZjf8h8zWugkAQD6/ElFj0KiLJoysdiqN70nCnRojod1ka0ZEiZRIUGwQJBAMC1XYiMfan29qVHV/K3fIUyzI/PMzyQJWTm/SNFd3jvEZopagrvYkxRC7koLoeCvKslcQ2O4gzgpdY+iqPVEBECQQC4llgsghu9v1zZW+kp9gQz5pAp/cYvOS5DTZctVjJzpgZaXmg5a6qgOWPzD8tDm1GW/TQP1yVOMmVO9SHXYFmrAkBWPZQjNMRUGOqeYsQwIf8+2NIFFbQXSWcCtgDZFRB3dX3KIPiM9j5mauq1LQ9No6ttaC8k4ym0m6B7tbdzxDkRAkBh+SKp1REmYIjGsbsLU5IdfhYsw47Kh94fSPKh1KuIqKmck5lcSOJSksOTQmHP64Od0Z0tfzNE0wjkpMWmjHRrAkAUyDj0xyN99WfU099LzNmed4KJyKCspgfwZXE+afgplXrw3TBnM1ZngfZJuvEPOAZ2sMAai3CjESFra5Z1cvCx";
//
//  String str="D4Hxd4wKwF5aMjOeCQXm26XkHecA17F41qtj/Zgub/qzebLOpPemuYv7um3RHi9WzQVayqeZRc2B7gIb6hLuREYwoGitRQuaooRXkiCNHVUGuNkn62RtUHS6S5DV8VRQ0xMKRPhWy3n4/uyxKw5WKl+vlqK9sibnnCgsP8vYCwmCla5sx4iMhv9O4eBw9YyyTTaZPOqEpg4tFiqbPHFw4VVMJq6TjRSDATtWofc8v6L4YQ3f8rgQPrNdFYzNrQlIoKfzIZwob6NpaFtxSNjnou/nP78TxWaXcbAjKavFZpUQT+mTUr8UrLY6jGguRIIzS7R+ziBjRb11Y04ZjcQFfA==";
//  try {
//    System.out.println(RSACoder.decryptByPrivateKey1(str,Base64.decodeBase64(privateKeyStr)));
//
//  } catch (Exception e) {
//    e.printStackTrace();
//  }
//}


}
