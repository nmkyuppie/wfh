/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh;

import com.equiniti.wfh.bean.DatabaseData;
import com.equiniti.wfh.dbconnectivity.PostgreSQLJDBC;
import com.equiniti.wfh.filemanipulation.FileManipulation;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nachimm
 */
public class TimeTrackerDAO {

    private PostgreSQLJDBC postgreSQLJDBC;
    private FileManipulation fileManipulation;
    private static TimeTrackerDAO timeTrackerDAO;
    private final String EFFECTIVE_TABLE = "effective", IDLE_TABLE = "idle", BREAK_TABLE = "break", TIMETRACKER_TABLE = "timetracker";
    private final String EMP_NAME = System.getProperty("user.name");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    int timeTrackerId;
    private String startTime;
    private List<DatabaseData> effectiveDataList;
    private List<DatabaseData> idleDataList;
    private List<DatabaseData> breakDataList;
    private final String filePath = "C:\\";
    private final String extension = ".txt";
    private TimeTrackerDAO() {
        postgreSQLJDBC = PostgreSQLJDBC.getInstance();
        fileManipulation = new FileManipulation();
        effectiveDataList = new ArrayList<>();
        breakDataList = new ArrayList<>();
        idleDataList = new ArrayList<>();
    }

    public static TimeTrackerDAO getInstance() {
        if (timeTrackerDAO != null) {
            return timeTrackerDAO;
        } else {
            timeTrackerDAO = new TimeTrackerDAO();
            return timeTrackerDAO;
        }
    }
    public void insertTimeTracker() throws SQLException, ParseException{
        java.util.Date dateTime = new java.util.Date();
        java.util.Date date = dateFormat.parse(dateFormat.format(dateTime));
        timeTrackerId = postgreSQLJDBC.insertTimeTracker(new java.sql.Date(date.getTime()), EMP_NAME);
    }
    void startTimeTracker(Date startDate) {
        try {
            //timeTrackerId = postgreSQLJDBC.insertTimeTracker(startDate, EMP_NAME);
            //startEffectiveHour(startDate);
            fileManipulation.insertEffective(timeTrackerId, startDate);

        } catch (IOException e) {
            Logger.getLogger(TimeTrackerDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    void stopTimeTracker(Date stopDate) {
        try {
            fileManipulation.updateEffective(timeTrackerId, stopDate);
            //postgreSQLJDBC.update(timeTrackerId, stopDate, TIMETRACKER_TABLE);
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
//        int seconds = postgreSQLJDBC.getLastSessionEffectiveHours(EMP_NAME);
//        setStartTime(postgreSQLJDBC.getStartTime());
        return -1;
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
    public void setTimeTrackerId() throws SQLException{
        java.util.Date date = new java.util.Date();
        timeTrackerId = postgreSQLJDBC.getTimeTrackerId(EMP_NAME, new java.sql.Date(date.getTime()));
    }
    public int getTimeTrackerId(){
        return timeTrackerId;
    }
    
     public void insertEffective(Timestamp startTime, Timestamp stopTime) throws SQLException{
         //postgreSQLJDBC.insert(timeTrackerId, startTime, stopTime, EFFECTIVE_TABLE);
         DatabaseData databaseData = new DatabaseData(timeTrackerId, startTime, stopTime);
         effectiveDataList.add(databaseData);
     }
     public void insertBreak(Timestamp startTime, Timestamp stopTime) throws SQLException{
         //postgreSQLJDBC.insert(timeTrackerId, startTime, stopTime, BREAK_TABLE);
         DatabaseData databaseData = new DatabaseData(timeTrackerId, startTime, stopTime);
         breakDataList.add(databaseData);
     }
     public void insertIdle(Timestamp startTime, Timestamp stopTime) throws SQLException{
        // postgreSQLJDBC.insert(timeTrackerId, startTime, stopTime, IDLE_TABLE);
        DatabaseData databaseData = new DatabaseData(timeTrackerId, startTime, stopTime);
        idleDataList.add(databaseData);
     }
     public void updateFileContentsToDataBase(String fileName) throws IOException, SQLException, ParseException {
        File file = new File(filePath + fileName+ extension);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String record;
        String[] recordArray;
        if ("1".equals(fileName.substring(fileName.length() - 1, fileName.length()))) {
            timeTrackerDAO.insertTimeTracker();
        } else if (timeTrackerDAO.getTimeTrackerId() == 0) {
            timeTrackerDAO.setTimeTrackerId();
        }
        while ((record = br.readLine()) != null) {
            recordArray = record.split(",");
            if(recordArray[2]!=null)
                createDataList(recordArray[0], convertStringToTimeStamp(recordArray[1]), convertStringToTimeStamp(recordArray[2]));
        }
        postgreSQLJDBC.insertBatch(effectiveDataList, idleDataList, breakDataList);
    }

    Timestamp convertStringToTimeStamp(String dateTimeString) throws ParseException {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        java.util.Date parsedDate = dateTimeFormat.parse(dateTimeString);
        return new Timestamp(parsedDate.getTime());
    }

    private void createDataList(String idType, Timestamp startTime, Timestamp stopTime) throws SQLException {
        switch (idType) {
            case "effective":
                timeTrackerDAO.insertEffective(startTime, stopTime);
                break;
            case "break":
                timeTrackerDAO.insertBreak(startTime, stopTime);
                break;
            case "idle":
                timeTrackerDAO.insertIdle(startTime, stopTime);
                break;
        }
    }
}
