package com.coezal.wallet.biz;

import com.coezal.wallet.api.bean.Test;

import java.util.List;

public interface TestService {

    List<Test> queryList(Test test);

    Boolean addTest(Test test);

    Boolean updateTest(Test test);
}
