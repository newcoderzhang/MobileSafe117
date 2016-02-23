package com.huilong.zhang.mobilesafe117.bean;

/**
 * Created by Mario on 2/23/16.
 */
public class BlackNumberInfo {
    private String phonenumber;
    private String mode;
    /**
     * 黑名单拦截模式
     * 1 全部拦截 电话拦截 + 短信拦截
     * 2 电话拦截
     * 3 短信拦截
     */
    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
