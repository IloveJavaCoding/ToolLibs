package com.example.toollibs.Activity.Events;

public class SentTotalLineEvent {
    private int totalLines;

    public SentTotalLineEvent(int totalLines) {
        this.totalLines = totalLines;
    }

    public int getTotalLines() {
        return totalLines;
    }
}
