package com.demo.ecclient.utils;

import static com.demo.ecclient.utils.Constants.Q_URL;

import android.content.Context;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.quorum.Quorum;
import org.web3j.tx.RawTransactionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class QuorumConnection {

    private Quorum quorum;

    private RawTransactionManager transactionManager;

    private String contractAddress;

    public QuorumConnection(Quorum quorum, RawTransactionManager transactionManager, String contractAddress) {
        this.quorum = quorum;
        this.transactionManager = transactionManager;
        this.contractAddress = contractAddress;
    }

    public Quorum getQuorum() {
        return quorum;
    }

    public RawTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public String getContractAddress() {
        return contractAddress;
    }


    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }
}
