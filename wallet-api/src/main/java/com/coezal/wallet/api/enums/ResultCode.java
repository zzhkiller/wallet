package com.coezal.wallet.api.enums;

import org.apache.commons.lang.StringUtils;

/**
 * @author yj
 */

public enum ResultCode {

    SUCCESS("成功", 0),
    FAIL("服务端异常", 10002),
    PARAM_ERROR("参数错误", 10003),
    DATA_ERROR("数据异常", 10004);






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
