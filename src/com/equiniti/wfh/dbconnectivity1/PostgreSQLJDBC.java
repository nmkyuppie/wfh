/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh.dbconnectivity1;

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

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private Statement statement = null;
    private String startTime;
    private static PostgreSQLJDBC postgreSQLJDBC = null;
    
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    private PostgreSQLJDBC() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/wfh", "postgres", "Ramya1994");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
    }
    
    public static PostgreSQLJDBC getInstance(){
        if(postgreSQLJDBC!=null)
            return postgreSQLJDBC;
        else{
            postgreSQLJDBC = new PostgreSQLJDBC();
            return postgreSQLJDBC;
        }
    }

    public List<ReportData> getReportDataList() {
        List<ReportData> reportDataList = new ArrayList();
        try {
            String query = new StringBuilder().append("select e.starttime, e.endtime, concat('Effective') as type, concat(DATE_PART('hour', e.endtime - e.starttime ) * 60, ':', DATE_PART('minute', e.endtime - e.starttime ), ':', cast(DATE_PART('second', e.endtime - e.starttime ) as int)) as total from effective e where e.endtime is not null")
                    .append(" union ")
                    .append("select b.starttime, b.endtime, concat('Break')  as type, concat(DATE_PART('hour', b.endtime - b.starttime ) * 60, ':', DATE_PART('minute', b.endtime - b.starttime ), ':', cast(DATE_PART('second', b.endtime - b.starttime ) as int)) as total from break b where b.endtime is not null")
                    .append(" union ")
                    .append("select i.starttime, i.endtime, concat('Idle')  as type, concat(DATE_PART('hour', i.endtime - i.starttime ) * 60, ':', DATE_PART('minute', i.endtime - i.starttime ), ':', cast(DATE_PART('second', i.endtime - i.starttime ) as int)) as total from idle i where i.endtime is not null")
                    .toString();
            
            try (Statement statementObject = connection.createStatement();
                    ResultSet resultSet = statementObject.executeQuery(query)) {
                while (resultSet.next()) {
                    ReportData reportData = new ReportData();
                    reportData.setStartTime(resultSet.getTimestamp("starttime"));
                    reportData.setEndTime(resultSet.getTimestamp("endtime"));
                    reportData.setType(resultSet.getString("type"));
                    reportData.setTotalTime(resultSet.getString("total"));
                    reportDataList.add(reportData);
                }
            }

        } catch (SQLException se) {
            System.err.println("Threw a SQLException creating the list of blogs.");
            System.err.println(se.getMessage());
        }

        return reportDataList;
    }

    public int insertUpdateTimeTracker(Date startDate, boolean isNewId, int empid) throws SQLException {
        int timetrackerId = 0;
        Timestamp timestamp = new Timestamp(startDate.getTime());
        String query = "";
        ResultSet rs = null;
        if (isNewId) {
            query = "INSERT INTO timetracker"
                    + "(empid, starttime) VALUES"
                    + "(?,?)";
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, empid);
            preparedStatement.setTimestamp(2, timestamp);
            preparedStatement.executeUpdate();
            rs = preparedStatement.getGeneratedKeys();
            if (rs.next()) {
                timetrackerId = rs.getInt(1);
            }
        } else {
            query = "SELECT MAX(id) as id from timetracker";
            Statement st = connection.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                timetrackerId = rs.getInt("id");
            }

            query = "update timetracker set endtime=? where id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setTimestamp(1, null);
            preparedStatement.setInt(2, timetrackerId);
            preparedStatement.executeUpdate();
        }
        return timetrackerId;
    }

    public void insert(int id, Date date, String tableName) throws SQLException {
        Timestamp timestamp = new Timestamp(date.getTime());
        String query = new StringBuilder()
                .append("INSERT INTO ")
                .append(tableName)
                .append("(timetrackerid, starttime) VALUES")
                .append("(?,?)").toString();

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, id);
            preparedStatement.setTimestamp(2, timestamp);

            // execute insert SQL stetement
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(PostgreSQLJDBC.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void update(int timeTrackerId, Date date, String tableName) {
        Timestamp timestamp = new Timestamp(date.getTime());
        String query;
        if (tableName.equals("timetracker")) {
            query = new StringBuilder().append("UPDATE ").append(tableName).append(" set endtime= ? WHERE id= ? and endtime is null").toString();
        } else {
            query = new StringBuilder().append("UPDATE ").append(tableName).append(" set endtime= ? WHERE timetrackerid= ? and endtime is null").toString();
        }

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setTimestamp(1, timestamp);
            preparedStatement.setInt(2, timeTrackerId);
            // execute insert SQL stetement
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(PostgreSQLJDBC.class.getName()).log(Level.SEVERE, null, e);

        }
    }

    public int getLastSessionEffectiveHours(int empid) {
        String query = new StringBuilder().append("select max(id) as id from timetracker where empid=").append(empid).toString();
        int seconds = 0;

        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if(resultSet.next()) {
                int timeTrackerId = resultSet.getInt("id");
                if (timeTrackerId > 0) {
                    query = new StringBuilder().append("select EXTRACT(EPOCH FROM (endtime-starttime )) as seconds from timetracker  where id=").append(timeTrackerId).toString();
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery(query);
                    while (resultSet.next()) {
                        seconds += resultSet.getInt("seconds");
                    }
                    query = new StringBuilder().append("select to_char(starttime,'DD/MM/YYYY HH12:MI:SS AM') as starttime from timetracker where id=").append(timeTrackerId).toString();
                    resultSet = statement.executeQuery(query);
                    if(resultSet.next()) {
                        startTime = resultSet.getString("starttime");
                    }
                } else {
                    return -1;
                }
            }

            return seconds;
        } catch (SQLException e) {
            Logger.getLogger(PostgreSQLJDBC.class.getName()).log(Level.SEVERE, null, e);
        }
        return seconds;
    }

    public String getTotalHours(int timeTrackerId, String tableName) {
        try {
            String query = new StringBuilder().append("select concat(lpad(DATE_PART('hour', sum(endtime -starttime) )::text,2,'0'), ':', lpad(DATE_PART('minute', sum(endtime - starttime) )::text,2,'0'),':', lpad(cast(DATE_PART('second', sum(endtime - starttime)) as int)::text,2,'0')) as total from ").append(tableName).append(" where timetrackerid=?").toString();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, timeTrackerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                return (rs.getString("total").equalsIgnoreCase("::"))?"00 : 00 : 00":rs.getString("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSQLJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "00:00:00";
    }
}
