package com.coezal.wallet.api.bean;

/**
 * 充值请求校验
 */
public class PayCheckRequest {
    private String server;
    private String usersign;

    private String checkcode;


    private String  id;

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

    @Override
    public String toString() {
        return "PayCheckRequest{" +
                "server='" + server + '\'' +
                ", usersign='" + usersign + '\'' +
                ", checkcode='" + checkcode + '\'' +
                ", id='" + id + '\'' +
                ", md5chk='" + md5chk + '\'' +
                '}';
    }
}
