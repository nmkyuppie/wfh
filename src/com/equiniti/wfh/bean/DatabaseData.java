/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh.bean;

import java.sql.Timestamp;

/**
 *
 * @author rajaser
 */
public class DatabaseData {
    private int timeTrackerId;
    private Timestamp startTime;
    private Timestamp endTime;
    
    public DatabaseData(int timeTrackerId, Timestamp startTime, Timestamp endTime) {
        this.timeTrackerId = timeTrackerId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getTimeTrackerId() {
        return timeTrackerId;
    }

    public void setTimeTrackerId(int timeTrackerId) {
        this.timeTrackerId = timeTrackerId;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

}
