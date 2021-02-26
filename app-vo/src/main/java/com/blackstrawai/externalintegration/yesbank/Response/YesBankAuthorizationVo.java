package com.blackstrawai.externalintegration.yesbank.Response;

public class YesBankAuthorizationVo {
  private String userName;
  private String authKey;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getAuthKey() {
    return authKey;
  }

  public void setAuthKey(String authKey) {
    this.authKey = authKey;
  }
}
