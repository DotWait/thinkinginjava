package com.dotwait.check;

import com.dotwait.annotation.Limit;

public class SyncSSIDInfo {
    @Limit(intValue = 435882)
    private Integer storeId;
    @Limit(strValue = "210235A1JMC164000125")
    private String sn;

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @Override
    public String toString() {
        return "SyncSSIDInfo{" +
                "storeId=" + storeId +
                ", sn='" + sn + '\'' +
                '}';
    }
}

