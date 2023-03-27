package com.silence.auth.client.properties;


import java.util.ArrayList;
import java.util.List;

/**
 * 关于安全的配置
 *
 * @author liuyongtao
 * @create 2019-05-22 14:25
 */

public class SecurityProperties {
    private List<String> whiteList = new ArrayList<>();

    private List<String> needCheck = new ArrayList<>();


    public List<String> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(List<String> whiteList) {
        this.whiteList = whiteList;
    }

    public List<String> getNeedCheck() {
        return needCheck;
    }

    public void setNeedCheck(List<String> needCheck) {
        this.needCheck = needCheck;
    }
}
