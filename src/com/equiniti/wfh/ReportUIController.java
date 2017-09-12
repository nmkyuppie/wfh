/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh;

import com.equiniti.wfh.DBConnectivity.PostgreSQLJDBC;
import com.equiniti.wfh.bean.ReportData;
import java.net.URL;
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
        assert itemTbl != null : "fx:id=\"itemTbl\" was not injected: check your FXML file 'ReportUI.fxml'.";
        System.out.println("itemTbl "+itemTbl); 
     startTimeCol.setCellValueFactory(
        new PropertyValueFactory<>("startTime"));        
    endTimeCol.setCellValueFactory(                
        new PropertyValueFactory<>("endTime"));
    typeCol.setCellValueFactory(
        new PropertyValueFactory<>("type"));        
    totalTimeCol.setCellValueFactory(
        new PropertyValueFactory<>("totalTime"));
     ObservableList<ReportData> data = FXCollections.observableArrayList();
            itemTbl.setItems(data);
            List<ReportData> reportDataList = new PostgreSQLJDBC().populateListOfTopics();
            data.addAll(reportDataList);
//            System.out.println("table.getItems().size() "+table.getItems().size());
    }
    
}
