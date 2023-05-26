package com.demo.ecclient.model;

import security.paillier.PaillierPrivateKey;

public class DecryptModel {
    private PictureBase pictureBase;
    private PaillierPrivateKey sk;

    public DecryptModel(PictureBase pictureBase, PaillierPrivateKey sk) {
        this.pictureBase = pictureBase;
        this.sk = sk;
    }

    public PictureBase getPictureBase() {
        return pictureBase;
    }

    public void setPictureBase(PictureBase pictureBase) {
        this.pictureBase = pictureBase;
    }

    public PaillierPrivateKey getSk() {
        return sk;
    }

    public void setSk(PaillierPrivateKey sk) {
        this.sk = sk;
    }
}
