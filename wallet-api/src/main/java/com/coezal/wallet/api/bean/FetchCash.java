package com.coezal.wallet.api.bean;

import java.util.Date;

/**
 * 请求体现的doman
 *
 *
 */
public class FetchCash {
    private Long id;

    private Long code;

    private String server;

    private String userSign;

    private String checkCode;

    private String tokenName;

    private String wallet;

    private Float money;

    private Date time;

    private String md5chk; //

    private String transactionHash; //转账hash

    private byte transactionSuccess; //转账是否成功：0不成功，1成功

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMd5chk() {
        return md5chk;
    }

    public void setMd5chk(String md5chk) {
        this.md5chk = md5chk;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public byte getTransactionSuccess() {
        return transactionSuccess;
    }

    public void setTransactionSuccess(byte transactionSuccess) {
        this.transactionSuccess = transactionSuccess;
    }
}