package com.coezal.wallet.api.bean;

/**
 * 充值成功通知请求对象
 */

public class RechargeRequest {
    private String usersign;

    private String checkcode;

    private String id;

    private String  tokenname;

    private String wallet;

    private String money;

    private String time;

    private String md5chk;



    public String getUsersign() {
        return usersign;
    }

    public void setUsersign(String usersign) {
        this.usersign = usersign;
    }

    public String getCheckcode() {
        return checkcode;
    }

    public void setCheckcode(String checkcode) {
        this.checkcode = checkcode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTokenname() {
        return tokenname;
    }

    public void setTokenname(String tokenname) {
        this.tokenname = tokenname;
    }

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
}
