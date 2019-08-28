package com.coezal.wallet.biz.service;

import com.coezal.wallet.api.bean.RsaKey;
import com.coezal.wallet.dal.dao.RsaMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
  RsaMapper rsaMapper;

  @Override
  public String getPublicKey() {
    List<RsaKey> keyList = rsaMapper.select(null);
    if (keyList != null && keyList.size() > 0) {
      return keyList.get(0).getPublicKey();
    } else {

      return "ddd";
    }
  }

  @Override
  public String getPrivateKey() {
    return "";
  }
}
