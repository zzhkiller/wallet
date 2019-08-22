package com.coezal.wallet.api.vo.base;

import com.coezal.wallet.api.enums.ResultCode;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class Result implements Serializable {
	private static final long serialVersionUID = -1223580564248951536L;
	
	private Integer code;
	private String msg;

    public Result() {
        super();
    }

    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Result success() {
        return new Result(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getDesc());
    }

    public static Result fail(ResultCode resultCode) {
        return new Result(resultCode.getCode(), resultCode.getDesc());
    }

    public static Result fail(Integer code, String msg) {
        return new Result(code, msg);
    }

	@JsonIgnore
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	@JsonIgnore
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
