package com.coezal.wallet.dal.dao;


import com.coezal.wallet.api.bean.RsaKey;

import java.util.List;

public interface RsaKeyMapper {
    /**
     * 保存数据
     */
    int insert(RsaKey record);

    /**
     * 更新主键ID对应的非NULL字段数据
     */
    int update(RsaKey record);


    /**
     * 通过条件查询数据信息
     */
    List<RsaKey> select(RsaKey record);

    /**
     * 通过条件查询唯一数据信息
     */
    RsaKey selectOne(RsaKey record);
}