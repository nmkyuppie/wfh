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

/**
 *
 * @author rajaser
 */
public class PostgreSQLJDBC {

    Connection c = null;
    PreparedStatement preparedStatement = null;

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

    public int insertTimeTracker(Date startDate) throws SQLException {
        int newId = 0;
        PreparedStatement preparedStatement = null;
        String insertTableSQL = "INSERT INTO timetracker"
                + "(empid, starttime) VALUES"
                + "(?,?)";

        try {
            Timestamp timestamp = new Timestamp(startDate.getTime());
            preparedStatement = c.prepareStatement(insertTableSQL, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, 1920);
            preparedStatement.setTimestamp(2, timestamp);

            // execute insert SQL stetement
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                System.out.println("p key " + newId);
                newId = rs.getInt(1);
            }
            System.out.println("newId " + newId);
            System.out.println("Record is inserted into timetracker table!");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {

            if (preparedStatement != null) {
                preparedStatement.close();
            }
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
        String insertTableSQL = "UPDATE " + tableName + " set endtime= ? WHERE id= ? and endtime is null";

        try {
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
}
