package com.demo.ecclient.utils;

import static com.demo.ecclient.utils.Constants.CHAIN_ID;
import static com.demo.ecclient.utils.Constants.Q_URL;

import android.os.AsyncTask;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;
import org.web3j.quorum.Quorum;
import org.web3j.tx.RawTransactionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class QuorumConnectionAsyncTask extends AsyncTask<File, Void, QuorumConnection> {

    public QuorumConnectionRes res;

    private Exception exception;

    public QuorumConnectionAsyncTask(QuorumConnectionRes res) {
        this.res = res;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        res.processStart();
    }

    @Override
    protected QuorumConnection doInBackground(File... files) {
        File path = files[0];
        try {
            File outputFile = new File(path, "wallet");

            try {
                FileOutputStream fos = new FileOutputStream(outputFile);
                fos.write("{\"address\":\"ca843569e3427144cead5e4d5999a3d0ccf92b8e\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"01d409941ce57b83a18597058033657182ffb10ae15d7d0906b8a8c04c8d1e3a\",\"cipherparams\":{\"iv\":\"0bfb6eadbe0ab7ffaac7e1be285fb4e5\"},\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":262144,\"p\":1,\"r\":8,\"salt\":\"7b90f455a95942c7c682e0ef080afc2b494ef71e749ba5b384700ecbe6f4a1bf\"},\"mac\":\"4cc851f9349972f851d03d75a96383a37557f7c0055763c673e922de55e9e307\"},\"id\":\"354e3b35-1fed-407d-a358-889a29111211\",\"version\":3}".getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            Quorum quorum = Quorum.build(new HttpService(Q_URL));
            Web3ClientVersion web3ClientVersion = quorum.web3ClientVersion().send();
            String clientVersion = web3ClientVersion.getWeb3ClientVersion();
            System.out.println("This is ClientVersion" + clientVersion);
            Credentials credentials = WalletUtils.loadCredentials("", outputFile);
            String address = credentials.getAddress();
            System.out.println("This is address: " + address);

            RawTransactionManager transactionManager = new RawTransactionManager(
                    quorum,
                    credentials,
                    CHAIN_ID // replace with your chainId
            );

            return new QuorumConnection(quorum, transactionManager, "");

        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(QuorumConnection quorumConnection) {
        if (quorumConnection != null) {
            res.processFinish(quorumConnection);
        } else if (this.exception != null) {
            // Handle exception
        }
    }
}