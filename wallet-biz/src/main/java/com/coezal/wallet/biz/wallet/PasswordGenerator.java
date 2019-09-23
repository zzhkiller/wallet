package com.coezal.wallet.biz.wallet;

import com.coezal.wallet.common.util.AESUtils;
import com.coezal.wallet.common.util.RSACoder;
import org.apache.commons.codec.binary.Base64;

import java.security.SecureRandom;

/**
 * Version 1.0
 * Created by lll on 2019-08-29.
 * Description
 * <pre>
 *   密码生成器
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class PasswordGenerator {

  /**
   * ras 加密公钥
   */
  public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCK84fezECjBJgDcg1/l9TRuw07MxYDGzuipX4p3LInx/tvYV0TmTkUCBUAlFaYW54OgHcw4fik2HYKYSl2zVqEq2ELqSx/mXHERA50SP8fFRp3ggRk6LXMi9NklicaV6VLA//3jTloIuPeZjZ5qKGdSdDWNzZO3NHGdLElz0ekWwIDAQAB";

  /**
   * ras 加密私钥
   */
  public static final String PRIVATE_KEY_RSA = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIrzh97MQKMEmANyDX+X1NG7DTszFgMbO6KlfincsifH+29hXROZORQIFQCUVphbng6AdzDh+KTYdgphKXbNWoSrYQupLH+ZccREDnRI/x8VGneCBGTotcyL02SWJxpXpUsD//eNOWgi495mNnmooZ1J0NY3Nk7c0cZ0sSXPR6RbAgMBAAECgYALDgD7SsjBr3XgoExOoGfAH9+XnCLeMGZ4NC5rajGKVLC+VcKv8nrGCzaQizywdmmGwdW5v+CmTMpnXP+Nghz3Xx6vbfKLA93jxPEYayg7LxqvZMpz9MZjf8h8zWugkAQD6/ElFj0KiLJoysdiqN70nCnRojod1ka0ZEiZRIUGwQJBAMC1XYiMfan29qVHV/K3fIUyzI/PMzyQJWTm/SNFd3jvEZopagrvYkxRC7koLoeCvKslcQ2O4gzgpdY+iqPVEBECQQC4llgsghu9v1zZW+kp9gQz5pAp/cYvOS5DTZctVjJzpgZaXmg5a6qgOWPzD8tDm1GW/TQP1yVOMmVO9SHXYFmrAkBWPZQjNMRUGOqeYsQwIf8+2NIFFbQXSWcCtgDZFRB3dX3KIPiM9j5mauq1LQ9No6ttaC8k4ym0m6B7tbdzxDkRAkBh+SKp1REmYIjGsbsLU5IdfhYsw47Kh94fSPKh1KuIqKmck5lcSOJSksOTQmHP64Od0Z0tfzNE0wjkpMWmjHRrAkAUyDj0xyN99WfU099LzNmed4KJyKCspgfwZXE+afgplXrw3TBnM1ZngfZJuvEPOAZ2sMAai3CjESFra5Z1cvCx";

  /**
   * aes 加密的key
   */
  public static final String GENERAL_KEY = "H68iG4v4Us3ZUF9xg5eOAS3ktVPVY5uYEEAQjt6/bWvbKLKdhTAHDHOyEdv89LiAZleyw07Zf63sWsRoXL8lUt+bcAbVXKjXlVY/Y/2R1jH/5eiKMCt6Ie9ZPvnyfs2N+woPCL3MXpB4vc5d50/i007Uv/MqhviTK1RcOG91RZs=";

  /**
   * 生成钱包密码
   *
   * @return
   */
  public static String generatorPassword() {
    byte bytes[] = new byte[256];
    SecureRandom random = new SecureRandom();
    random.nextBytes(bytes);
    return new String(bytes);
  }

  /**
   * 获取解密AWS秘钥
   *
   * @return
   */
  public static String getPwd() {
    try {
      String src = new String(RSACoder.decryptByPrivateKey(Base64.decodeBase64(GENERAL_KEY), Base64.decodeBase64(PRIVATE_KEY_RSA)));
      return src;
    } catch (Exception e) {
      e.printStackTrace();
      return "wallet.coezal.com.io";
    }
  }


  public static void main(String[] args) {
    String aesPwd = getPwd();
    String encryptMnemonic = AESUtils.encrypt("0xd9795Ac75229d4bE73cF817D0d65eC03E378849B", aesPwd);
    System.out.println("aesPwd===" + aesPwd);
    System.out.println("encryptMnemonic===" + encryptMnemonic);

    String decrypt = AESUtils.decrypt(encryptMnemonic, aesPwd);
    System.out.println("decrypt===" + decrypt);
  }
}
