package com.demo.ecclient.model;

import java.io.Serializable;

import security.paillier.PaillierPublicKey;

public class DelegateModel implements Serializable {
    private static final long serialVersionUID = -3751255153289772365L;
    private PictureBase base;
    private PictureMask mask;
    private PaillierPublicKey publicKey;

    public DelegateModel(PictureBase base, PictureMask mask, PaillierPublicKey publicKey) {
        this.base = base;
        this.mask = mask;
        this.publicKey = publicKey;
    }

    public PictureBase getBase() {
        return base;
    }

    public void setBase(PictureBase base) {
        this.base = base;
    }

    public PictureMask getMask() {
        return mask;
    }

    public void setMask(PictureMask mask) {
        this.mask = mask;
    }

    public PaillierPublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PaillierPublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
