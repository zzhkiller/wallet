package com.coezal.wallet.api.bean;

import java.util.List;

/**
 * Version 1.0
 * Created by lll on 2019-08-25.
 * Description
 * <pre>
 *   钱包实体
 * </pre>
 * copyright generalray4239@gmail.com
 */
public class WalletBean {

  private Long id;// 主键id
  private String password; //生成钱包的秘钥
  private String address; //钱包地址;
  private String privateKey;// 钱包私钥;
  private String mnemonicList;//助记词，HD钱包才有， 根据生成方式的不同，有可能没有助记词， 助记词之间用,分割
  private String keyStoreJson;//生成的keystore json文件，如果采用助记词就没有
  private String ownerInfo; // 钱包拥有者信息，电话或者邮箱的MD5|checkcode
  private String signKey; // 解密私钥


  public WalletBean() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  public String getMnemonicList() {
    return mnemonicList;
  }

  public void setMnemonicList(String mnemonicList) {
    this.mnemonicList = mnemonicList;
  }

  public String getKeyStoreJson() {
    return keyStoreJson;
  }

  public void setKeyStoreJson(String keyStoreJson) {
    this.keyStoreJson = keyStoreJson;
  }

  public String getOwnerInfo() {
    return ownerInfo;
  }

  public void setOwnerInfo(String ownerInfo) {
    this.ownerInfo = ownerInfo;
  }

  public String getSignKey() {
    return signKey;
  }

  public void setSignKey(String signKey) {
    this.signKey = signKey;
  }


  @Override
  public String toString() {
    return "WalletBean{" +
            "id=" + id +
            ", password='" + password + '\'' +
            ", address='" + address + '\'' +
            ", privateKey='" + privateKey + '\'' +
            ", mnemonicList='" + mnemonicList + '\'' +
            ", keyStoreJson='" + keyStoreJson + '\'' +
            ", ownerInfo='" + ownerInfo + '\'' +
            ", signKey='" + signKey + '\'' +
            '}';
  }
}
