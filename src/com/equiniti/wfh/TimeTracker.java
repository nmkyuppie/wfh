/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh;

import java.util.Date;
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

    TimeTrackerController controller;
    Parent root;
    public static Stage parentWindow;

    @Override
    public void start(Stage stage) throws Exception {
        parentWindow = stage;
        FXMLLoader loader = new FXMLLoader();
        root = loader.load(getClass().getResource("TimeTrackerDocument.fxml"));

        Scene scene = new Scene(root);

        scene.getStylesheets().add(TimeTracker.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
        controller = new TimeTrackerController();
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
    public void stop() {
        controller.timeTrackerDAO.stopTimeTracker(new Date());
    }

}
