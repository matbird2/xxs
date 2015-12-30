package com.xxs.leon.xxs.rest.bean.request;

/**
 * Created by maliang on 15/12/30.
 */
public class CollectInstallationParams {
    private String deviceType;
    private String installationId;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getInstallationId() {
        return installationId;
    }

    public void setInstallationId(String installationId) {
        this.installationId = installationId;
    }
}
