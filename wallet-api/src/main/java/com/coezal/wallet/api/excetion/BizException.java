package com.coezal.wallet.api.excetion;


import com.coezal.wallet.api.enums.ResultCode;

/**
 * @author yj
 * @ClassName BizException
 * @Description 业务异常定义
 */
public class BizException extends BaseRuntimeException {

    private static final long serialVersionUID = 1L;

    public BizException(ResultCode resultCode) {
        super(resultCode.getCode(), resultCode.getDesc());
    }

    public BizException(Integer code, String message) {
        super(code, message);
    }

    public BizException(String message) {
        super(message);
    }
}
