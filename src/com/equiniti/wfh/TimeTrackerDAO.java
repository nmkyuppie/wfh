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

    private PostgreSQLJDBC postgreSQLJDBC;
    private static TimeTrackerDAO timeTrackerDAO;
    private final String EFFECTIVE_TABLE = "effective", IDLE_TABLE = "idle", BREAK_TABLE = "break", TIMETRACKER_TABLE = "timetracker";
    private final int EMP_ID=1920;
    int timeTrackerId;
    private String startTime;

    private TimeTrackerDAO() {
        postgreSQLJDBC = PostgreSQLJDBC.getInstance();
    }

    public static TimeTrackerDAO getInstance() {
        if (timeTrackerDAO != null) {
            return timeTrackerDAO;
        } else {
            timeTrackerDAO = new TimeTrackerDAO();
            return timeTrackerDAO;
        }
    }

    void startTimeTracker(Date startDate, boolean isNewId) {
        try {
            timeTrackerId = postgreSQLJDBC.insertUpdateTimeTracker(startDate, isNewId, EMP_ID);
            startEffectiveHour(startDate);
        } catch (SQLException e) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, e);
        }
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

    void startIdleHour(Date idleStartDate) {
        try {
            stopEffectiveHour(idleStartDate);
            postgreSQLJDBC.insert(timeTrackerId, idleStartDate, IDLE_TABLE);
        } catch (SQLException ex) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void stopIdleHour(Date idleStopDate) {
        postgreSQLJDBC.update(timeTrackerId, idleStopDate, IDLE_TABLE);
        startEffectiveHour(idleStopDate);
    }

    int getLastSessionInSeconds() {
        int seconds = postgreSQLJDBC.getLastSessionEffectiveHours(EMP_ID);
        setStartTime(postgreSQLJDBC.getStartTime());
        return seconds;
    }

    String getTotalIdle() {
        return postgreSQLJDBC.getTotalHours(timeTrackerId, IDLE_TABLE);
    }

    String getTotalBreak() {
        return postgreSQLJDBC.getTotalHours(timeTrackerId, BREAK_TABLE);
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

}
