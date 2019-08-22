package com.coezal.wallet.dal.dao;


import com.coezal.wallet.api.bean.Test;

import java.util.List;

public interface TestMapper {
    /**
     * 保存数据
     */
    int insert(Test record);

    /**
     * 更新主键ID对应的非NULL字段数据
     */
    int update(Test record);

    /**
     * 通过条件查询数据信息
     */
    List<Test> select(Test record);

    /**
     * 通过条件查询唯一数据信息
     */
    Test selectOne(Test record);
}