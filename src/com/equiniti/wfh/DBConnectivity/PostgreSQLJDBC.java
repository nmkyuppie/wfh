/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh.DBConnectivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
/**
 *
 * @author rajaser
 */
public class PostgreSQLJDBC {
    Connection c = null;
    private ObservableList<ObservableList> data;
    private TableView tableview;
    public PostgreSQLJDBC(){
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
    public ObservableList<ObservableList> getReportData(){
        data = FXCollections.observableArrayList();
        try {
            //SQL FOR SELECTING ALL OF CUSTOMER
            String SQL = "SELECT * from wfhschema.time_tracker";
            //ResultSet
            ResultSet rs = c.createStatement().executeQuery(SQL);
            System.out.println("com.equiniti.wfh.DBConnectivity.PostgreSQLJDBC.getReportData()" + rs.getRow());
            
            for(int i=0 ; i<rs.getMetaData().getColumnCount(); i++){
                //We are using non property style for making dynamic table
                final int j = i;                
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i+1));
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList,String>,ObservableValue<String>>(){                    
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {                                                                                              
                        return new SimpleStringProperty(param.getValue().get(j).toString());                        
                    }                    
                });

                tableview.getColumns().addAll(col); 
                System.out.println("Column ["+i+"] ");
            }

            /********************************
             * Data added to ObservableList *
             ********************************/
            while(rs.next()){
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for(int i=1 ; i<=rs.getMetaData().getColumnCount(); i++){
                    //Iterate Column
                    row.add(rs.getString(i));
                    System.out.println(rs.getString(1));
                }
                System.out.println("Row [1] added "+row );
                data.add(row);

            }
        } catch (SQLException ex) {
            Logger.getLogger(PostgreSQLJDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }
    
}
