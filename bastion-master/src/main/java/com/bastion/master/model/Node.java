package com.bastion.master.model;

import java.time.LocalDateTime;

public class Node {

    private String nodeId;
    private String hostName;
    private String status;
    private LocalDateTime lastHeartbeat;
    private String osType;
    private String ipAddress;

    public Node(String nodeId,
                String hostName,
                String status,
                LocalDateTime lastHeartbeat,
                String osType,
                String ipAddress) {

        this.nodeId = nodeId;
        this.hostName = hostName;
        this.status = status;
        this.lastHeartbeat = lastHeartbeat;
        this.osType = osType;
        this.ipAddress = ipAddress;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void setLastHeartbeat(LocalDateTime lastHeartbeat) {
        this.lastHeartbeat = lastHeartbeat;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}