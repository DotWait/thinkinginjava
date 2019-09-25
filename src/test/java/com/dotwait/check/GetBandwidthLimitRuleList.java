package com.dotwait.check;

import com.dotwait.annotation.Limit;

public class GetBandwidthLimitRuleList {
    @Limit(strValue = "210235A1JMC164000125")
    private String sn;
    @Limit(strValue = "1234")
    private String ssid;
    @Limit(intValue = 435882)
    private Integer storeId;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    @Override
    public String toString() {
        return "GetBandwidthLimitRuleList{" +
                "sn='" + sn + '\'' +
                ", ssid='" + ssid + '\'' +
                ", storeId=" + storeId +
                '}';
    }
}
