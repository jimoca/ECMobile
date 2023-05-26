package com.demo.ecclient.utils;


public interface QuorumConnectionRes {
    void processStart();
    void processFinish(QuorumConnection quorumConnection);
}
