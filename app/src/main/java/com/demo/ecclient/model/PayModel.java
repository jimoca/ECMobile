package com.demo.ecclient.model;

import com.demo.ecclient.utils.QuorumConnection;

import java.math.BigInteger;

public class PayModel {

    private QuorumConnection quorumConnection;

    private BigInteger taskId;

    private BigInteger price;

    public PayModel(QuorumConnection quorumConnection, BigInteger taskId, BigInteger price) {
        this.quorumConnection = quorumConnection;
        this.taskId = taskId;
        this.price = price;
    }

    public PayModel() {
    }

    public QuorumConnection getQuorumConnection() {
        return quorumConnection;
    }

    public void setQuorumConnection(QuorumConnection quorumConnection) {
        this.quorumConnection = quorumConnection;
    }

    public BigInteger getTaskId() {
        return taskId;
    }

    public void setTaskId(BigInteger taskId) {
        this.taskId = taskId;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }
}
