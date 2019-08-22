package com.coezal.wallet.biz;

import com.coezal.wallet.api.bean.Test;
import com.coezal.wallet.dal.dao.TestMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TestServiceImpl implements TestService{
    @Resource
    TestMapper testMapper;

    @Override
    public List<Test> queryList(Test test) {
        return testMapper.select(test);
    }

    @Override
    public Boolean addTest(Test test) {
        testMapper.insert(test);
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateTest(Test test) {
        testMapper.update(test);
        return Boolean.TRUE;
    }
}
