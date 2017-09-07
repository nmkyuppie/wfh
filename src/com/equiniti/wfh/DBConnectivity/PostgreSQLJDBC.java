/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh.DBConnectivity;
import java.sql.Connection;
import java.sql.DriverManager;
/**
 *
 * @author rajaser
 */
public class PostgreSQLJDBC {
    public PostgreSQLJDBC(){
      Connection c = null;
      try {
         Class.forName("org.postgresql.Driver");
         c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/wfh","postgres", "Ramya1994");
      } catch (Exception e) {
         e.printStackTrace();
         System.err.println(e.getClass().getName()+": "+e.getMessage());
         System.exit(0);
      }
      System.out.println("Opened database successfully");
    }
    public void getReportData(){
        
    }
}
