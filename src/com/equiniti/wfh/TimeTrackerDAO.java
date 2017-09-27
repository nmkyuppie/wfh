/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh;

import com.equiniti.wfh.dbconnectivity.PostgreSQLJDBC;
import com.equiniti.wfh.filemanipulation.FileManipulation;
import java.io.IOException;
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
    private FileManipulation fileManipulation;
    private static TimeTrackerDAO timeTrackerDAO;
    private final String EFFECTIVE_TABLE = "effective", IDLE_TABLE = "idle", BREAK_TABLE = "break", TIMETRACKER_TABLE = "timetracker";
    private final int EMP_ID = 1920;
    int timeTrackerId;
    private String startTime;

    private TimeTrackerDAO() {
        postgreSQLJDBC = PostgreSQLJDBC.getInstance();
        fileManipulation = new FileManipulation();
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
            //startEffectiveHour(startDate);
            fileManipulation.insertEffective(timeTrackerId, startDate);

        } catch (SQLException | IOException e) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    void stopTimeTracker(Date stopDate) {
        try {
            fileManipulation.updateEffective(timeTrackerId, stopDate);
            postgreSQLJDBC.update(timeTrackerId, stopDate, TIMETRACKER_TABLE);
        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void startBreak(Date breakStartDate) {
        try {
            fileManipulation.updateEffective(timeTrackerId, breakStartDate);
            fileManipulation.insertBreak(timeTrackerId, breakStartDate);
        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void stopBreak(Date breakStopDate) {
        try {
            fileManipulation.updateBreak(timeTrackerId, breakStopDate);
            startEffectiveHour(breakStopDate);
        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void startEffectiveHour(Date startDate) {
        try {
            fileManipulation.insertEffective(timeTrackerId, startDate);
        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

/*    private void stopEffectiveHour(Date stopDate) {
        try {
            fileManipulation.updateEffective(timeTrackerId, stopDate);
        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
*/

    void startIdleHour(Date idleStartDate) {
        try {
            fileManipulation.updateEffective(timeTrackerId, idleStartDate);
            fileManipulation.insertIdle(timeTrackerId, idleStartDate);
        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void stopIdleHour(Date idleStopDate) {
        try {
            fileManipulation.updateIdle(timeTrackerId, idleStopDate);
            fileManipulation.insertEffective(timeTrackerId, idleStopDate);
        } catch (IOException ex) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
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
