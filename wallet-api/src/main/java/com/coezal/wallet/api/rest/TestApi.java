package com.coezal.wallet.api.rest;

import com.coezal.wallet.api.bean.Test;
import com.coezal.wallet.api.vo.base.BaseResponse;
import com.coezal.wallet.api.vo.base.DtoResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Api(value = "Test API", description = "Test API", protocols = "http")
public interface TestApi {
    @ApiOperation(value="ces", notes="ces", protocols="http")
    @RequestMapping(value = "/queryTest", method = RequestMethod.GET)
     BaseResponse<List<Test>> queryTest(Test test);


    @RequestMapping(value = "/addTest", method = RequestMethod.POST)
    BaseResponse<Boolean> addTest(Test test);
}
