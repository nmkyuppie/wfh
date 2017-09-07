/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh;

import com.equiniti.wfh.DBConnectivity.PostgreSQLJDBC;
import com.equiniti.wfh.util.Win32IdleTime;
import com.equiniti.wfh.util.WindowMonitorThread;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author nachimm
 */
public class TimeTracker extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        PostgreSQLJDBC postgreSQLJDBC = new PostgreSQLJDBC();
        WindowMonitorThread wmt=new WindowMonitorThread();
        wmt.start();
        Parent root = FXMLLoader.load(getClass().getResource("TimeTrackerDocument.fxml"));

        Scene scene = new Scene(root);

        scene.getStylesheets().add(TimeTracker.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
