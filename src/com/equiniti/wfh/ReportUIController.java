/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh;

import com.equiniti.wfh.DBConnectivity.PostgreSQLJDBC;
import com.equiniti.wfh.bean.ReportData;
import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author rajaser
 */
public class ReportUIController implements Initializable {
       @FXML
    TableView<ReportData> itemTbl;
    
    @FXML
    TableColumn startTimeCol;
    
    @FXML
    TableColumn endTimeCol;
    
    @FXML
    TableColumn typeCol;
    
    @FXML
    TableColumn totalTimeCol;

    // The table's observableList
    ObservableList<ReportData> observableList;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("report fxml");
        assert itemTbl != null : "fx:id=\"tableview\" was not injected: check your FXML file 'UserMaster.fxml'.";
        System.out.println("itemTbl "+itemTbl); 
     startTimeCol.setCellValueFactory(
        new PropertyValueFactory<ReportData,Timestamp>("startTime"));        
    endTimeCol.setCellValueFactory(                
        new PropertyValueFactory<ReportData,Timestamp>("endTime"));
    typeCol.setCellValueFactory(
        new PropertyValueFactory<ReportData,String>("type"));        
    totalTimeCol.setCellValueFactory(
        new PropertyValueFactory<ReportData,Timestamp>("totalTime"));
     ObservableList<ReportData> data=null;
            data= FXCollections.observableArrayList();
            itemTbl.setItems(data);
            List<ReportData> reportDataList = new PostgreSQLJDBC().populateListOfTopics();
            data.addAll(reportDataList);
    }
    
}
