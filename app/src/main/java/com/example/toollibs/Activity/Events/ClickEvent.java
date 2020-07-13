package com.example.toollibs.Activity.Events;

public class ClickEvent {
    private boolean isShow;

    public ClickEvent(boolean isShow) {
        this.isShow = isShow;
    }

    public boolean isShow() {
        return isShow;
    }
}
