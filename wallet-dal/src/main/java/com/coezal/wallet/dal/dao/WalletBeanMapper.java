package com.coezal.wallet.dal.dao;


import com.coezal.wallet.api.bean.WalletBean;

import java.util.List;

public interface WalletBeanMapper {
    /**
     * 保存数据
     */
    int insert(WalletBean record);

    /**
     * 更新主键ID对应的非NULL字段数据
     */
    int update(WalletBean record);


    /**
     * 通过主键获取数据
     */
    WalletBean selectById(Long id);

    /**
     * 通过条件查询数据信息
     */
    List<WalletBean> select(WalletBean record);

    /**
     * 通过条件查询唯一数据信息
     */
    WalletBean selectOne(WalletBean record);

    /**
     * 获取所有绑定了用户的钱包地址
     * @return
     */
    List<WalletBean> selectAllUsedAddress();
}