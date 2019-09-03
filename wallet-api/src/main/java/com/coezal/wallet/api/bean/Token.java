package com.coezal.wallet.api.bean;

/**
 * Version 1.0
 * Created by lll on 2019-08-25.
 * Description
 * <pre>
 *   合约实体
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class Token {
  private Long id;
  private String tokenContractAddress;
  private int tokenDecimals;
  private String tokenName;
  private String tokenSymbol;
  private String tokenABI;

  public String getTokenContractAddress() {
    return tokenContractAddress;
  }

  public void setTokenContractAddress(String tokenContractAddress) {
    this.tokenContractAddress = tokenContractAddress;
  }

  public int getTokenDecimals() {
    return tokenDecimals;
  }

  public void setTokenDecimals(int tokenDecimals) {
    this.tokenDecimals = tokenDecimals;
  }

  public String getTokenName() {
    return tokenName;
  }

  public void setTokenName(String tokenName) {
    this.tokenName = tokenName;
  }

  public String getTokenSymbol() {
    return tokenSymbol;
  }

  public void setTokenSymbol(String tokenSymbol) {
    this.tokenSymbol = tokenSymbol;
  }

  public String getTokenABI() {
    return tokenABI;
  }

  public void setTokenABI(String tokenABI) {
    this.tokenABI = tokenABI;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
