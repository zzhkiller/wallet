package com.coezal.wallet.api.excetion;


import com.coezal.wallet.api.enums.ResultCode;

public class BaseException extends Exception {

	private static final long serialVersionUID = 1L;

	private ResultCode code;
	
	private String message;

	public BaseException(ResultCode code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public BaseException(String message) {
		super();
		this.message = message;
	}

	public ResultCode getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public void setCode(ResultCode code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
}
