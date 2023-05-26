package com.demo.ecclient.model;

import java.math.BigInteger;

public class TaskModel {
    private BigInteger taskId;
    private String contractAddress;

    public TaskModel(BigInteger taskId, String contractAddress) {
        this.taskId = taskId;
        this.contractAddress = contractAddress;
    }

    public TaskModel() {
    }

    public BigInteger getTaskId() {
        return taskId;
    }

    public void setTaskId(BigInteger taskId) {
        this.taskId = taskId;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }
}
