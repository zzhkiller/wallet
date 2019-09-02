package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.RsaKey;
import com.coezal.wallet.common.util.RSACoder;
import com.coezal.wallet.dal.dao.RsaKeyMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Version 1.0
 * Created by lll on 2019-08-27.
 * Description
 * <pre>
 *   获取public key
 *
 * </pre>
 * copyright generalray4239@gmail.com
 */

@Service
public class RsaServiceImpl implements RsaService {

  @Resource
  RsaKeyMapper rsaMapper;

  @Override
  public String getPublicKey() {
    List<RsaKey> keyList = rsaMapper.select(null);
    if (keyList != null && keyList.size() > 0) {
      return keyList.get(0).getPublicKey();
    } else {
      try {
        return insertKey(true);
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("获取key 失败");
      }
    }
  }

  @Override
  public String getPrivateKey() {
    List<RsaKey> keyList = rsaMapper.select(null);
    if (keyList != null && keyList.size() > 0) {
      return keyList.get(0).getPublicKey();
    } else {
      try {
        return insertKey(false);
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("获取privateKeyStr 失败");
      }
    }
  }

  /**
   * 生成Rsa秘钥对，并存储到数据库里面
   * @param publicKey
   * @return
   * @throws Exception
   */
  private String insertKey(boolean publicKey) throws Exception {
    RsaKey key = new RsaKey();
    Map<String, Object> keyMap = RSACoder.initKey();
    String publicKeyStr = Base64.encodeBase64String(RSACoder.getPublicKey(keyMap));
    String privateKeyStr = Base64.encodeBase64String(RSACoder.getPrivateKey(keyMap));
    key.setPublicKey(publicKeyStr);
    key.setPrivateKey(privateKeyStr);
    rsaMapper.insert(key);
    return publicKey ? publicKeyStr : privateKeyStr;
  }
}
