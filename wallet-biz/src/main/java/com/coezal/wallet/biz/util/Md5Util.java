package com.coezal.wallet.biz.util;


import java.security.MessageDigest;
import java.util.UUID;

public class Md5Util {
    public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z" };

    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }


    public static String MD5(String s) {
        char hexDigits[] = { '0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput =s.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
