package com.demo.ecclient.model;

import java.math.BigDecimal;

public class EdgeInfo {

    private Long edgeId;

    private String location;

    private String ipAddress;

    private BigDecimal charge;

    public EdgeInfo(Long edgeId, String location, String ipAddress, BigDecimal charge) {
        this.edgeId = edgeId;
        this.location = location;
        this.ipAddress = ipAddress;
        this.charge = charge;
    }

    public Long getEdgeId() {
        return edgeId;
    }

    public void setEdgeId(Long edgeId) {
        this.edgeId = edgeId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public BigDecimal getCharge() {
        return charge;
    }

    public void setCharge(BigDecimal charge) {
        this.charge = charge;
    }
}
