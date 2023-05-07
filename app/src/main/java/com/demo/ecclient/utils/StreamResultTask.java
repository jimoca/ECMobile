package com.demo.ecclient.utils;

import static com.demo.ecclient.utils.Constants.API_TAG;

import android.os.AsyncTask;
import android.util.Log;

import com.demo.ecclient.model.PictureBase;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import okhttp3.ResponseBody;

public class StreamResultTask extends AsyncTask<ResponseBody, Void, PictureBase> {

    public AsyncResponse res;

    public StreamResultTask(AsyncResponse res) {
        this.res = res;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        res.processStart();
    }

    @Override
    protected PictureBase doInBackground(ResponseBody... responseBodies) {
        PictureBase pictureRes = responseBodyToObj(responseBodies[0]);
        Log.d(API_TAG, "Byte[] receiving was success? " + (pictureRes != null));
        return pictureRes;
    }

    @Override
    protected void onPostExecute(PictureBase pictureBase) {
        super.onPostExecute(pictureBase);
        res.processFinish(pictureBase);
    }

    private PictureBase responseBodyToObj(ResponseBody body) {
        try (InputStream inputStream = body.byteStream();
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {

            return (PictureBase) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
