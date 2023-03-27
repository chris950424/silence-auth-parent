package com.silence.auth.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 关于安全的配置
 *
 * @author liuyongtao
 * @create 2019-05-22 14:25
 */

public class SecurityProperties {
    private List<String> noCheckList = new ArrayList<>();

    private String baseUrl;

    public List<String> getNoCheckList() {
        return noCheckList;
    }

    public void setNoCheckList(List<String> noCheckList) {
        this.noCheckList = noCheckList;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
