package com.coezal.wallet.api.bean;

/**
 * Version 1.0
 * Created by lll on 2019-08-28.
 * Description
 * copyright generalray4239@gmail.com
 */
public class RsaKey {

  private Long id;
  private String privateKey;
  private String publicKey;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  public String getPublicKey() {
    return publicKey;
  }

  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }
}
