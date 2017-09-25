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

    Parent root;
    public static Stage parentWindow;

    @Override
    public void start(Stage stage) throws Exception {
        parentWindow = stage;
        root = FXMLLoader.load(getClass().getResource("TimeTrackerDocument.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(TimeTracker.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
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
        TimeTrackerDAO.getInstance().stopTimeTracker(new Date());
    }

}
