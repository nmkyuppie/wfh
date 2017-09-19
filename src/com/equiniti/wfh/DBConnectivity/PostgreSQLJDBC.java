/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh.DBConnectivity;

import com.equiniti.wfh.bean.ReportData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.*;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rajaser
 */
public class PostgreSQLJDBC {

    Connection c = null;
    PreparedStatement preparedStatement = null;
    private String starttime; 

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }
    
    public PostgreSQLJDBC() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/wfh", "postgres", "Ramya1994");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public List<ReportData> populateListOfTopics() {
        List<ReportData> reportDataList = new ArrayList();
        try {
            try (Statement st = c.createStatement(); ResultSet rs = st.executeQuery("select e.starttime, e.endtime, concat('Effective') as type, concat(DATE_PART('hour', e.endtime - e.starttime ) * 60, ':', DATE_PART('minute', e.endtime - e.starttime ), ':', cast(DATE_PART('second', e.endtime - e.starttime ) as int)) as total from effective e where e.endtime is not null"
                    + " union "
                    + "select b.starttime, b.endtime, concat('Break')  as type, concat(DATE_PART('hour', b.endtime - b.starttime ) * 60, ':', DATE_PART('minute', b.endtime - b.starttime ), ':', cast(DATE_PART('second', b.endtime - b.starttime ) as int)) as total from break b where b.endtime is not null "
                    + " union "
                    + "select i.starttime, i.endtime, concat('Idle')  as type, concat(DATE_PART('hour', i.endtime - i.starttime ) * 60, ':', DATE_PART('minute', i.endtime - i.starttime ), ':', cast(DATE_PART('second', i.endtime - i.starttime ) as int)) as total from idle i where i.endtime is not null"
            )) {
                while (rs.next()) {
                    ReportData rd = new ReportData();
                    rd.startTime.setValue(rs.getTimestamp("starttime"));
                    rd.endTime.setValue(rs.getTimestamp("endtime"));
                    rd.type.setValue(rs.getString("type"));
                    rd.totalTime.setValue(rs.getString("total"));
                    reportDataList.add(rd);
                }
            }

        } catch (SQLException se) {
            System.err.println("Threw a SQLException creating the list of blogs.");
            System.err.println(se.getMessage());
        }

        return reportDataList;
    }

    public int insertUpdateTimeTracker(Date startDate, boolean isNewId, int empid) throws SQLException {
        int newId = 0;
        Timestamp timestamp = new Timestamp(startDate.getTime());
        System.out.println("timestamp " + timestamp);
        PreparedStatement preparedStatement = null;
        String query = "";
        ResultSet rs = null;
        if (isNewId) {
            query = "INSERT INTO timetracker"
                    + "(empid, starttime) VALUES"
                    + "(?,?)";
            preparedStatement = c.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, empid);
            preparedStatement.setTimestamp(2, timestamp);
            preparedStatement.executeUpdate();
            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("p key " + newId);
                newId = rs.getInt(1);
            }
            System.out.println("newId " + newId);
            System.out.println("Record is inserted into timetracker table!");
        } else {
            query = "SELECT MAX(id) as id from timetracker";
            Statement st = c.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                System.out.println("in rs true");
                newId = rs.getInt("id");
            }
            System.out.println("tt ID " + newId);
            
            query = "update timetracker set endtime=? where id=?";
            preparedStatement = c.prepareStatement(query);
            preparedStatement.setTimestamp(1, null);
            preparedStatement.setInt(2, newId);
            preparedStatement.executeUpdate();
            //Select latest timetracker record based on emp id
            //Update endtime to null
            //insert a new effective hour entry
            //TODO
            System.out.println("Record is updated in timetracker table!");
        }
        return newId;
    }

    public void insert(int id, Date date, String tableName) throws SQLException {
        Timestamp timestamp = new Timestamp(date.getTime());
        String insertTableSQL = "INSERT INTO " + tableName
                + "(timetrackerid, starttime) VALUES"
                + "(?,?)";

        try {
            preparedStatement = c.prepareStatement(insertTableSQL);

            preparedStatement.setInt(1, id);
            preparedStatement.setTimestamp(2, timestamp);

            // execute insert SQL stetement
            preparedStatement.executeUpdate();

            System.out.println("Record is inserted into " + tableName + " table!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }

    public void update(int timeTrackerId, Date date, String tableName) {
        Timestamp timestamp = new Timestamp(date.getTime());
        String insertTableSQL = "";
        if (tableName.equals("timetracker")) {
            insertTableSQL = "UPDATE " + tableName + " set endtime= ? WHERE id= ? and endtime is null";
        } else {
            insertTableSQL = "UPDATE " + tableName + " set endtime= ? WHERE timetrackerid= ? and endtime is null";
        }

        try {
            System.out.println("heellldsopfjdsoijf " + timeTrackerId + " dsfdsfdsf " + tableName + " dsfdsf " + date);
            System.out.println(insertTableSQL);
            preparedStatement = c.prepareStatement(insertTableSQL);

            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.setInt(2, timeTrackerId);
            // execute insert SQL stetement
            preparedStatement.executeUpdate();

            System.out.println("Record is updated in " + tableName + " table!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        }
    }

    public int getLastSessionEffectiveHours() {
        String query = "select max(id) as id from timetracker";
        int seconds = 0;

        try {
            System.out.println(query);
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                ReportData rd = new ReportData();
                int timeTrackerId = rs.getInt("id");
                System.out.println("inside while "+timeTrackerId);
                if (timeTrackerId > 0) {
                    query = "select DATE_PART('seconds', age(endtime,starttime ))  as hour from effective  where timetrackerid=" + timeTrackerId;
                    st = c.createStatement();
                    rs = st.executeQuery(query);
                    while (rs.next()) {
                        seconds += rs.getInt("hour");
                    }
                    query = "select to_char(starttime,'DD/MM/YYYY HH12:MI:SS AM') as starttime from timetracker where id="+timeTrackerId;
                    rs=st.executeQuery(query);
                    while(rs.next()){
                        starttime = rs.getString("starttime");
                        System.out.println("last starttime"+starttime);
                    }
                } else {
                    return -1;
                }
            }

            return seconds;
        } catch (SQLException e) {

            System.out.println(e);

        }
        return seconds;
    }

    public String getTotalIdle(int timeTrackerId) {
        try {
            String query = "select concat(DATE_PART('hour', sum(endtime -starttime) ), ' : ', DATE_PART('minute', sum(endtime - starttime) ),' : ', cast(DATE_PART('second', sum(endtime - starttime)) as int)) as total from idle where timetrackerid=?";
            PreparedStatement ps = c.prepareStatement(query);
            ps.setInt(1, timeTrackerId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println("rs.idle" +rs.getString("total"));
                return rs.getString("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSQLJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
         return "00:00:00";
    }

    public String getTotalBreak(int timeTrackerId) {
        try {
            String query = "select concat(DATE_PART('hour', sum(endtime -starttime) ), ' : ', DATE_PART('minute', sum(endtime - starttime) ),' : ', cast(DATE_PART('second', sum(endtime - starttime)) as int)) as total from break where timetrackerid=?";
            PreparedStatement ps = c.prepareStatement(query);
            ps.setInt(1, timeTrackerId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println("rs.break" +rs.getString("total"));
                return rs.getString("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSQLJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
         return "00:00:00";
    }
}
