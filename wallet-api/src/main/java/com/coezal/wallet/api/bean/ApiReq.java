package com.coezal.wallet.api.bean;

import java.io.Serializable;

public class ApiReq implements Serializable {
    private String datastr;

    public String getDatastr() {
        return datastr;
    }

    public void setDatastr(String datastr) {
        this.datastr = datastr;
    }
}
