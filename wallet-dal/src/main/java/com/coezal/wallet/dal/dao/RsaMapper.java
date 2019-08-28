package com.coezal.wallet.dal.dao;

import com.coezal.wallet.api.bean.RsaKey;

import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2019-08-28.
 * Description
 * copyright generalray4239@gmail.com
 */
public interface RsaMapper {

  /**
   * 保存数据
   */
  int insert(RsaKey rsaKey);

  /**
   * 获取满足条件的key
   * @return
   */
  List<RsaKey> select(RsaKey rsaKey);
}
