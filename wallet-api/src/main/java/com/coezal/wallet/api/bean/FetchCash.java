package com.coezal.wallet.api.bean;

import java.util.Date;

/**
 * 请求体现的doman
 *
 *
 */
public class FetchCash {
    private Long id;

    /**api 体现请求的id 唯一 不为空*/
    private String code;

    /**测试还是正式环境区别*/
    private String server;

    /**用户电话，不为空*/
    private String userSign;

    /**用户标记，不为空*/
    private String checkCode;

    /**提币名称，不为空*/
    private String tokenName;

    /**提现钱包地址*/
    private String wallet;

    /**提现数量，不为空*/
    private String money;

    /**提现时间*/
    private String time;

    /**md5校验，不为空*/
    private String md5chk;

    /**用户钱包地址*/
    private String userWalletAddress;

    /**提现hash，可为空，但是唯一*/
    private String transactionHash;

    /**转账是否成功：0不成功，1成功*/
    private byte transactionSuccess;

    /**通知API体现成功是否成功 0不成功，1成功*/
    private byte noticeApiSuccess;

    /**是否曾经有过转币，0有过，1没有*/
    private byte checkHadTransaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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

    public byte getNoticeApiSuccess() {
        return noticeApiSuccess;
    }

    public void setNoticeApiSuccess(byte noticeApiSuccess) {
        this.noticeApiSuccess = noticeApiSuccess;
    }

    public String getUserWalletAddress() {
        return userWalletAddress;
    }

    public void setUserWalletAddress(String userWalletAddress) {
        this.userWalletAddress = userWalletAddress;
    }

    public byte getCheckHadTransaction() {
        return checkHadTransaction;
    }

    public void setCheckHadTransaction(byte checkHadTransaction) {
        this.checkHadTransaction = checkHadTransaction;
    }

    @Override
    public String toString() {
        return "FetchCash{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", server='" + server + '\'' +
                ", userSign='" + userSign + '\'' +
                ", checkCode='" + checkCode + '\'' +
                ", tokenName='" + tokenName + '\'' +
                ", wallet='" + wallet + '\'' +
                ", money='" + money + '\'' +
                ", time='" + time + '\'' +
                ", md5chk='" + md5chk + '\'' +
                ", userWalletAddress='" + userWalletAddress + '\'' +
                ", transactionHash='" + transactionHash + '\'' +
                ", transactionSuccess=" + transactionSuccess +
                ", noticeApiSuccess=" + noticeApiSuccess +
                ", checkHadTransaction=" + checkHadTransaction +
                '}';
    }
}
