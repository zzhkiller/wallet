package com.coezal.wallet.api.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Version 1.0
 * Created by lll on 2019-09-02.
 * Description
 * <pre>
 *   token 转账记录
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class TokenTransaction {
  /**
   *        "blockNumber": "5893571",
   * 		"timeStamp": "1561903996",
   * 		"hash": "0xd5d1bc54778046300bc65b0342afda7b5e0108e43d1028c44ca425b57a63088d",
   * 		"nonce": "21",
   * 		"blockHash": "0xbf41104f0d4c650ae422a9de1e0ff23053ef0d4b11a900bce7d5559c22a03e0e",
   * 		"from": "0x604f2db361304e9c775d76182b00c3c2dc397dcc",
   * 		"contractAddress": "0xa5efd6d045c444283f1fe5036ba2b2e82ed6d5c6",
   * 		"to": "0xf102121cbaaa2731f2c68c11157c8c56d970c3df",
   * 		"value": "200",
   * 		"tokenName": "lllToken",
   * 		"tokenSymbol": "lll",
   * 		"tokenDecimal": "18",
   * 		"transactionIndex": "15",
   * 		"gas": "53747",
   * 		"gasPrice": "5000000000",
   * 		"gasUsed": "53747",
   * 		"cumulativeGasUsed": "6749373",
   * 		"input": "deprecated",
   * 		"confirmations": "423845"
   */
  private Long id;
  private String blockNumber;
  private String timeStamp;
  private String hash;
  private String blockHash;
  private String fromAddress; //
  private String toAddress;
  private String value;//转账数量
  private String isError; //0 成功
  private String contractAddress;// 合约地址
  private String tokenName;
  private String tokenSymbol;
  private String tokenDecimal;
  private Byte notifySuccessFlag; //0 表示通知失败，1 表示成功

  public String getBlockNumber() {
    return blockNumber;
  }

  public void setBlockNumber(String blockNumber) {
    this.blockNumber = blockNumber;
  }

  public String getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  @JSONField(name = "from")
  public String getFromAddress() {
    return fromAddress;
  }

  @JSONField(name = "from")
  public void setFromAddress(String fromAddress) {
    this.fromAddress = fromAddress;
  }

  @JSONField(name = "to")
  public String getToAddress() {
    return toAddress;
  }

  @JSONField(name = "to")
  public void setToAddress(String toAddress) {
    this.toAddress = toAddress;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getIsError() {
    return isError;
  }

  public void setIsError(String isError) {
    this.isError = isError;
  }

  public String getContractAddress() {
    return contractAddress;
  }

  public void setContractAddress(String contractAddress) {
    this.contractAddress = contractAddress;
  }

  public String getTokenName() {
    return tokenName;
  }

  public void setTokenName(String tokenName) {
    this.tokenName = tokenName;
  }

  public String getTokenDecimal() {
    return tokenDecimal;
  }

  public void setTokenDecimal(String tokenDecimal) {
    this.tokenDecimal = tokenDecimal;
  }

  public String getBlockHash() {
    return blockHash;
  }

  public void setBlockHash(String blockHash) {
    this.blockHash = blockHash;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTokenSymbol() {
    return tokenSymbol;
  }

  public void setTokenSymbol(String tokenSymbol) {
    this.tokenSymbol = tokenSymbol;
  }

  public Byte getNotifySuccessFlag() {
    return notifySuccessFlag;
  }

  public void setNotifySuccessFlag(Byte notifySuccessFlag) {
    this.notifySuccessFlag = notifySuccessFlag;
  }

  @Override
  public String toString() {
    return "TokenTransaction{" +
            "id=" + id +
            ", blockNumber='" + blockNumber + '\'' +
            ", timeStamp='" + timeStamp + '\'' +
            ", hash='" + hash + '\'' +
            ", blockHash='" + blockHash + '\'' +
            ", from='" + fromAddress + '\'' +
            ", to='" + toAddress + '\'' +
            ", value='" + value + '\'' +
            ", isError='" + isError + '\'' +
            ", contractAddress='" + contractAddress + '\'' +
            ", tokenName='" + tokenName + '\'' +
            ", tokenSymbol='" + tokenSymbol + '\'' +
            ", tokenDecimal='" + tokenDecimal + '\'' +
            '}';
  }
}
