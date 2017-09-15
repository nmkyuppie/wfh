/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh.bean;

import java.sql.Timestamp;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author rajaser
 */
public class ReportData {

    public ObjectProperty<Timestamp> startTime = new SimpleObjectProperty<>();
    public ObjectProperty<Timestamp> endTime = new SimpleObjectProperty<>();
    public SimpleStringProperty type = new SimpleStringProperty();
    public SimpleStringProperty totalTime = new SimpleStringProperty();

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

}
