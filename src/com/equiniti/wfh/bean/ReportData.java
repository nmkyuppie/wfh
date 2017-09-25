/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh.bean;

import java.sql.Timestamp;
import java.util.Comparator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author rajaser
 */
public class ReportData {

    private ObjectProperty<Timestamp> startTime = new SimpleObjectProperty<>();
    private ObjectProperty<Timestamp> endTime = new SimpleObjectProperty<>();
    private SimpleStringProperty type = new SimpleStringProperty();
    private SimpleStringProperty totalTime = new SimpleStringProperty();

    public Timestamp getStartTime() {
        return startTime.get();
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime.set(startTime);
    }

    public Timestamp getEndTime() {
        return endTime.get();
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime.set(endTime);
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getTotalTime() {
        return totalTime.get();
    }

    public void setTotalTime(String totalTime) {
        this.totalTime.set(totalTime);
    }

    public static Comparator<ReportData> ReportDataComparator
            = (ReportData reportData1, ReportData reportData2) -> {
                Timestamp startTime1 = reportData1.getStartTime();
                Timestamp startTime2 = reportData2.getStartTime();
                
                return startTime1.compareTo(startTime2);
            };
}
