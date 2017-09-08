/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh;

import com.equiniti.wfh.DBConnectivity.PostgreSQLJDBC;
import com.equiniti.wfh.util.WindowMonitorThread;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 *
 * @author nachimm
 */
public class TimeTracker extends Application {

    TimeTrackerController controller;

    PostgreSQLJDBC postgreSQLJDBC = null;
    
    @Override
    public void start(Stage stage) throws Exception {
        postgreSQLJDBC = new PostgreSQLJDBC();
        WindowMonitorThread wmt=new WindowMonitorThread();
        wmt.start();
            FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("TimeTrackerDocument.fxml"));

        Scene scene = new Scene(root);

        scene.getStylesheets().add(TimeTracker.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
        controller = loader.getController();
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        Date stopDate = new Date();
        System.out.println("Stop Date : " + dateFormat.format(stopDate));
        //controller. - Stop Event, If there is no previous stop
    }


}
