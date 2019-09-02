package com.coezal.wallet.api.vo.base;

import com.coezal.wallet.api.enums.ResultCode;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * @author yj
 */
public class ETHScanBaseResponse<T> implements Serializable {

  private static final long serialVersionUID = 3478493193648267325L;


  @ApiModelProperty(required = false)
  private String message;
  @ApiModelProperty(required = false)
  private T result;
  @ApiModelProperty(required = true)
  private Integer status;

  public ETHScanBaseResponse() {
    super();
  }

  private ETHScanBaseResponse(Integer status, String message, T result) {
    this.message = message;
    this.result = result;
    this.status = status;
  }

  private ETHScanBaseResponse(T data) {
    this.status = ResultCode.SUCCESS.getCode();
    this.message = ResultCode.SUCCESS.getDesc();
    this.result = data;
  }

  /**
   * 返回失败的JSON串
   *
   * @param code
   * @param msg
   * @param data
   * @return
   */
  public static ETHScanBaseResponse fail(Integer code, String msg, Object data) {
    return new ETHScanBaseResponse(code, msg, data);
  }

  /**
   * 返回失败的JSON串,数据体为null
   *
   * @param code
   * @param msg
   * @return
   */
  public static ETHScanBaseResponse fail(Integer code, String msg) {
    return ETHScanBaseResponse.fail(code, msg, null);
  }

  /**
   * 返回系统异常的错误信息JSON串
   *
   * @return
   */
  public static ETHScanBaseResponse systemError() {
    return ETHScanBaseResponse.fail(ResultCode.FAIL.getCode(), ResultCode.FAIL.getDesc(), null);
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getResult() {
    return result;
  }

  public void setResult(T result) {
    this.result = result;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
