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
    public TimeTrackerDAO(){
        postgreSQLJDBC = new PostgreSQLJDBC();
    }
    void startTimeTracker(Date startDate) {
        try{
        timeTrackerId = postgreSQLJDBC.insertTimeTracker(startDate);
        System.out.println("Starting session @ "+startDate);
        startEffectiveHour(startDate);
        }catch(SQLException e){
            System.out.println(e);
        }
        //startEffectiveHour(startDate);
    }

    void stopTimeTracker(Date stopDate) {
        stopEffectiveHour(stopDate);
        postgreSQLJDBC.update(timeTrackerId, stopDate, TIMETRACKER_TABLE);
        System.out.println("Stopping session @ "+stopDate);
    }

    void startBreak(Date breakStartDate) {
        try {
            postgreSQLJDBC.insert(timeTrackerId, breakStartDate, BREAK_TABLE);
            stopEffectiveHour(breakStartDate);
            System.out.println("Starting break @ "+breakStartDate);
        } catch (SQLException ex) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void stopBreak(Date breakStopDate) {
        System.out.println("Stopping break @ "+breakStopDate);
        postgreSQLJDBC.update(timeTrackerId, breakStopDate, BREAK_TABLE);
        startEffectiveHour(breakStopDate);
    }

    void startEffectiveHour(Date startDate) {
        try {
            postgreSQLJDBC.insert(timeTrackerId, startDate, EFFECTIVE_TABLE);
            System.out.println("Starting effective hour @ "+startDate);
        } catch (SQLException ex) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void stopEffectiveHour(Date stopDate) {
        System.out.println("Stopping effective hour @ "+stopDate);
        postgreSQLJDBC.update(timeTrackerId, stopDate, EFFECTIVE_TABLE);
    }

    void startIdleHour(Date startDate) {
        try {
            postgreSQLJDBC.insert(timeTrackerId, startDate, IDLE_TABLE);
            stopEffectiveHour(startDate);
            System.out.println("Starting idle hour @ "+startDate);
        } catch (SQLException ex) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void stopIdleHour(Date stopDate) {
        System.out.println("Stopping idle hour @ "+stopDate);
        postgreSQLJDBC.update(timeTrackerId, stopDate, IDLE_TABLE);
        startEffectiveHour(stopDate);
    }
    
}
