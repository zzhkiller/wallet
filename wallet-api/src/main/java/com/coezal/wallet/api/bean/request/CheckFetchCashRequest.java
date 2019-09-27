package com.coezal.wallet.api.bean.request;

import java.io.Serializable;

/**
 * 查询提现请求是否真实
 */
public class CheckFetchCashRequest implements Serializable {
    private String usersign;
    private String checkcode;
    private String id;//体现编号
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

    public String getMd5chk() {
        return md5chk;
    }

    public void setMd5chk(String md5chk) {
        this.md5chk = md5chk;
    }
}
