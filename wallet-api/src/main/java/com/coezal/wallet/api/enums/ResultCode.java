package com.coezal.wallet.api.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author yj
 */

public enum ResultCode {

    SUCCESS("0", 0), //成功
    FAIL("10002", 90001), //服务端异常
    PARAM_ERROR("10003", 90002), //参数错误
    DATA_ERROR("10004", 90003), //数据异常
    DECRYPT_ERROR("10005", 90004), //解密参数异常
    CREATE_WALLET_ERROR("10006", 90005), //钱包生成异常
    WALLET_NOT_EXIT("10007", 90006), //该用户没有钱包地址

    PAY_CHECK_ERROR("10008", 90007),//充值不存在,用户没有充值
    MD5_CHECK_ERROR("10009", 90008), //MD5校验异常

    TOKEN_NOT_EXIT("10010", 90009),//token 不存在

    FETCH_CASH_EXIT("10011", 90010),//提现请求已经存在
    FETCH_CASH_ERROR("10012", 90011); //提现请求校验不通过

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
