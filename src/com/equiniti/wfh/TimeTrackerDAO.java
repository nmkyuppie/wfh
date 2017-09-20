/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh;

import com.equiniti.wfh.DBConnectivity.PostgreSQLJDBC;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nachimm
 */
class TimeTrackerDAO {

    PostgreSQLJDBC postgreSQLJDBC;
    final String EFFECTIVE_TABLE = "effective";
    final String IDLE_TABLE = "idle";
    final String BREAK_TABLE = "break";
    final String TIMETRACKER_TABLE = "timetracker";
    int timeTrackerId;
    private String starttime;

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public TimeTrackerDAO() {
        postgreSQLJDBC = new PostgreSQLJDBC();
    }

    void startTimeTracker(Date startDate, boolean isNewId) {
        try {
            timeTrackerId = postgreSQLJDBC.insertUpdateTimeTracker(startDate, isNewId, 1920);
            startEffectiveHour(startDate);
        } catch (SQLException e) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        //startEffectiveHour(startDate);
    }

    void stopTimeTracker(Date stopDate) {
        stopEffectiveHour(stopDate);
        postgreSQLJDBC.update(timeTrackerId, stopDate, TIMETRACKER_TABLE);
    }

    void startBreak(Date breakStartDate) {
        try {
            stopEffectiveHour(breakStartDate);
            postgreSQLJDBC.insert(timeTrackerId, breakStartDate, BREAK_TABLE);
        } catch (SQLException ex) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void stopBreak(Date breakStopDate) {
        postgreSQLJDBC.update(timeTrackerId, breakStopDate, BREAK_TABLE);
        startEffectiveHour(breakStopDate);
    }

    void startEffectiveHour(Date startDate) {
        try {
            postgreSQLJDBC.insert(timeTrackerId, startDate, EFFECTIVE_TABLE);
        } catch (SQLException ex) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void stopEffectiveHour(Date stopDate) {
        postgreSQLJDBC.update(timeTrackerId, stopDate, EFFECTIVE_TABLE);
    }

    void startIdleHour(Date startDate) {
        try {
            stopEffectiveHour(startDate);
            postgreSQLJDBC.insert(timeTrackerId, startDate, IDLE_TABLE);
        } catch (SQLException ex) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void stopIdleHour(Date stopDate) {
        postgreSQLJDBC.update(timeTrackerId, stopDate, IDLE_TABLE);
        startEffectiveHour(stopDate);
    }

    int getLastSessionInSeconds() {
        int seconds = postgreSQLJDBC.getLastSessionEffectiveHours();
        setStarttime(postgreSQLJDBC.getStarttime());
        return seconds;
    }

    String getTotalIdle() {
        return postgreSQLJDBC.getTotalIdle(timeTrackerId);
    }

    String getTotalBreak() {
        return postgreSQLJDBC.getTotalBreak(timeTrackerId);
    }

}
