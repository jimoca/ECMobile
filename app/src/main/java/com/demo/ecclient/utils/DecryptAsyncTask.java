package com.demo.ecclient.utils;

import android.os.AsyncTask;

import com.demo.ecclient.model.DecryptModel;
import com.demo.ecclient.model.PayModel;

import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;

public class DecryptAsyncTask extends AsyncTask<DecryptModel, Void, BigInteger[]> {

    private DecryptAsync res;

    public DecryptAsyncTask(DecryptAsync res) {
        this.res = res;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        res.processStart();
    }

    @Override
    protected BigInteger[] doInBackground(DecryptModel... models) {
        DecryptModel input = models[0];
        return PaillierPixels.decryptPixels(input.getPictureBase().getPixels(), input.getSk());
    }


    @Override
    protected void onPostExecute(BigInteger[] output) {
        super.onPostExecute(output);
        res.processFinish(output);
    }
}
