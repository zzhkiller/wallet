package com.coezal.wallet.api.bean.request;

import java.io.Serializable;

/**
 * 查询提现请求是否真实
 */
public class CheckFetchCashRequest implements Serializable {

    private String wallet;

    private String money;

    private Integer status;

    private String md5chk;

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMd5chk() {
        return md5chk;
    }

    public void setMd5chk(String md5chk) {
        this.md5chk = md5chk;
    }
}
