package com.coezal.wallet.api.bean;

/**
 * 提现请求对象
 */
public class FetchCashRequest {

    private String server;
    private String usersign;
    private String checkcode;
    private Long id; //提现编号
    private String tokenname;
    private String wallet;// 钱包地址
    private Float money;// 金额
    private Long time;//提现时间
    private String md5chk;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getMd5chk() {
        return md5chk;
    }

    public void setMd5chk(String md5chk) {
        this.md5chk = md5chk;
    }
}
