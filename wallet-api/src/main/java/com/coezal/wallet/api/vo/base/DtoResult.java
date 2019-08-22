package com.coezal.wallet.api.vo.base;



import com.coezal.wallet.api.enums.ResultCode;

import java.io.Serializable;


public class DtoResult implements Serializable {

    private static final long serialVersionUID = 7441966401293650465L;

    private Integer code;
    private String msg;
    private Object data;

    public DtoResult() {
        super();
    }

    public DtoResult(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static DtoResult success() {
        return new DtoResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getDesc());
    }

    public static DtoResult fail(ResultCode resultCode) {
        return new DtoResult(resultCode.getCode(), resultCode.getDesc());
    }

    public static DtoResult fail(Integer code, String msg) {
        return new DtoResult(code, msg);
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }

}
