package com.coezal.wallet.common.util;

public class MobileUtil {
    private static final String MOBILE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";

    /**
     * 校验手机号格式是否争取
     * @param phoneNo
     * @return
     */
    public static boolean validate(String phoneNo) {
        Boolean flag = null;
        if(phoneNo==null || phoneNo.length()==0){
            flag= false;
        }else {
            flag= phoneNo.matches(MOBILE_REGEX);
        }
        return flag;
    }
}
