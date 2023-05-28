package com.demo.ecclient.utils;

import static com.demo.ecclient.utils.Constants.ENCRYPT;
import static com.demo.ecclient.utils.Constants.PAY;

import android.os.AsyncTask;
import android.util.Log;

import com.demo.ecclient.model.PayModel;
import org.web3j.model.EdgeComputing;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;


public class PayAsyncTask extends AsyncTask<PayModel, Void, Void> {

    private PayAsyncRes res;

    public PayAsyncTask(PayAsyncRes res) {
        this.res = res;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        res.payStart();
    }

    @Override
    protected Void doInBackground(PayModel... payModels) {
        Instant start = Instant.now();
        Log.d(PAY, "Start to pay...");
        PayModel input = payModels[0];
        EdgeComputing edgeComputing = EdgeComputing.load(input.getQuorumConnection().getContractAddress(), input.getQuorumConnection().getQuorum(), input.getQuorumConnection().getTransactionManager(),
                new StaticGasProvider(BigInteger.ZERO, DefaultGasProvider.GAS_LIMIT));

        TransactionReceipt receipt = null;
        try {
            receipt = edgeComputing.payTask(input.getTaskId(), Convert.toWei(input.getPrice().toString(), Convert.Unit.ETHER).toBigInteger()).send();
            System.out.println("Pay task: " + receipt);

            Instant finish = Instant.now();
            long time = Duration.between(start, finish).toMillis();
            Log.d(PAY, "Payment finished: " + time + "ms");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        res.payFinished();
    }
}
