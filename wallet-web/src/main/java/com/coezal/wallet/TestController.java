package com.coezal.wallet;

import com.coezal.wallet.api.bean.Test;
import com.coezal.wallet.api.rest.TestApi;
import com.coezal.wallet.api.vo.base.BaseResponse;
import com.coezal.wallet.api.vo.base.DtoResult;

import com.coezal.wallet.biz.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController extends AbstractController implements TestApi {

    @Resource
    TestService testService;

    @Override
    public BaseResponse<List<Test>> queryTest(Test test) {
        return buildJson(testService.queryList(test));
    }


    @Override
    public BaseResponse<Boolean> addTest(Test test) {
        return buildJson(testService.addTest(test));
    }
}
