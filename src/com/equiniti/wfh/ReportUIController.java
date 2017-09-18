/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh;

import com.equiniti.wfh.DBConnectivity.PostgreSQLJDBC;
import com.equiniti.wfh.bean.ReportData;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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

    @FXML
    private void backButtonAction(ActionEvent event) {
        Parent root = null;
        FXMLLoader loader = new FXMLLoader();
        try {
            root = loader.load(getClass().getResource("TimeTrackerDocument.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(ReportUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Scene scene = new Scene(root);

        scene.getStylesheets().add(TimeTracker.class.getResource("style.css").toExternalForm());
        Stage stage = TimeTracker.parentWindow;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.show();

    }
}
