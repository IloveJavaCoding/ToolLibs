package com.nepalese.toollibs.Activity.Interface;

public interface NetStateChangeObserver {
    void onNetDisconnected();
    void onNetConnected(NetworkType networkType);
}
