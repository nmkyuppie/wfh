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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rajaser
 */
public class PostgreSQLJDBC {

    Connection c = null;

    public PostgreSQLJDBC() {
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/wfh", "postgres", "Ramya1994");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public List<ReportData> populateListOfTopics() {
        List<ReportData> reportDataList=new ArrayList();
        try {
            try (Statement st = c.createStatement()) {
                ResultSet rs = st.executeQuery("SELECT * FROM effective ORDER BY id");
                while (rs.next()) {
                    ReportData rd=new ReportData();
                    rd.startTime.setValue(rs.getTimestamp("starttime"));
                    rd.endTime.setValue(rs.getTimestamp("endtime"));
                    rd.type.setValue("Effective");
                    rd.totalTime.setValue(rs.getTimestamp("endTime"));
                    reportDataList.add(rd);
                }
                rs.close();
            }
            
        } catch (SQLException se) {
            System.err.println("Threw a SQLException creating the list of blogs.");
            System.err.println(se.getMessage());
        }
        
        return reportDataList;
    }
}