package com.demo.ecclient.utils;

import com.demo.ecclient.model.PictureBase;

public interface AsyncResponse {
    void processStart();
    void processFinish(PictureBase output);
}
