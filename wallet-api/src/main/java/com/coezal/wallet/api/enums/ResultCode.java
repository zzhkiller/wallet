package com.coezal.wallet.api.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author yj
 */

public enum ResultCode {

    SUCCESS("成功", 0),
    FAIL("服务端异常", 10002),
    PARAM_ERROR("参数错误", 10003),
    DATA_ERROR("数据异常", 10004),
    DECRYPT_ERROR("解密参数异常", 10005),
    CREATE_WALLET_ERROR("钱包生成异常", 10006),
    WALLET_NOT_EXIT("该用户没有钱包地址", 10007),

    PAY_CHECK_ERROR("充值不存在", 10008),
    MD5_CHECK_ERROR("MD5校验异常", 10009),

    TOKEN_NOT_EXIT("token 不存在", 10010),

    FETCH_CASH_EXIT("提现请求已经存在", 10011),
    FETCH_CASH_ERROR("提现请求校验不通过", 10012);






    private String desc;
    private Integer code;

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    ResultCode(String desc, Integer code) {
        this.desc = desc;
        this.code = code;
    }

    public static ResultCode getResultCodeByName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        ResultCode[] enumsArray = ResultCode.values();
        for (ResultCode resultCode : enumsArray) {
            if (resultCode.name().equals(name)) {
                return resultCode;
            }
        }
        return null;
    }
}
