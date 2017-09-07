/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author nachimm
 */
public class TimeTrackerController implements Initializable {

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button breakButton;

    @FXML
    private Button reportButton;

    @FXML
    private Label clockInLabel;

    @FXML
    private Label clockOutLabel;

    @FXML
    private Label effectiveHourCounterLabel;

    boolean isTimerActive;

    @FXML
    private void startButtonAction(ActionEvent event) {
        isTimerActive = true;
        resetButtonStyles("START");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        Date startDate = new Date();
        System.out.println("Start Date : " + dateFormat.format(startDate));
        clockInLabel.setText(dateFormat.format(startDate));
        initializeDashboard(startDate);
    }

    @FXML
    private void stopButtonAction(ActionEvent event) {
        isTimerActive = false;
        resetButtonStyles("STOP");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        Date stopDate = new Date();
        System.out.println("Stop Date : " + dateFormat.format(stopDate));
        clockOutLabel.setText(dateFormat.format(stopDate));
//        initializeDashboard(stopDate);
    }

    @FXML
    private void breakButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }

    @FXML
    private void reportButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        try {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("NewWindow.fxml"));
        /* 
         * if "fx:controller" is not set in fxml
         * fxmlLoader.setController(NewWindowController);
         */
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("New Window");
        stage.setScene(scene);
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("Yes");
    }

    private void resetButtonStyles(String button) {
        if (button.equalsIgnoreCase("start")) {
            startButton.setDisable(true);
            stopButton.setDisable(false);
            breakButton.setDisable(false);
            startButton.getStyleClass().add("buttonInActive");
            startButton.getStyleClass().remove("buttonActive");
            stopButton.getStyleClass().add("buttonActive");
            stopButton.getStyleClass().remove("buttonInActive");
            breakButton.getStyleClass().add("buttonActive");
            breakButton.getStyleClass().remove("buttonInActive");
        } else if (button.equalsIgnoreCase("stop")) {
            startButton.setDisable(false);
            stopButton.setDisable(true);
            breakButton.setDisable(true);
            startButton.getStyleClass().add("buttonActive");
            startButton.getStyleClass().remove("buttonInActive");
            stopButton.getStyleClass().add("buttonInActive");
            stopButton.getStyleClass().remove("buttonActive");
            breakButton.getStyleClass().add("buttonInActive");
            breakButton.getStyleClass().remove("buttonActive");
        }
    }

    Timer timer;

    private void initializeDashboard(Date startDate) {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    timer = new Timer();
                                    timer.scheduleAtFixedRate(new TimerTask() {

                                        @Override
                                        public void run() {
                                            if (isTimerActive) {
                                                Platform.runLater(() -> {
                                                    Date currentDate = new Date();

                                                    long diff = currentDate.getTime() - startDate.getTime();
                                                    long diffSeconds = diff / 1000 % 60;
                                                    long diffMinutes = diff / (60 * 1000) % 60;
                                                    long diffHours = diff / (60 * 60 * 1000);
                                                    String hr = (diffHours / 10 == 0) ? "0" + diffHours : "" + diffHours;
                                                    String min = (diffMinutes / 10 == 0) ? "0" + diffMinutes : "" + diffMinutes;
                                                    String sec = (diffSeconds / 10 == 0) ? "0" + diffSeconds : "" + diffSeconds;
                                                    effectiveHourCounterLabel.setText(hr + " : " + min + " : " + sec);
                                                });

                                            } else {
                                                timer.cancel();
                                            }
                                        }
                                    }, 1000, 1000);
                                } finally {
                                    latch.countDown();
                                }
                            }
                        });
                        latch.await();
                        //Keep with the background work
                        return null;
                    }
                };
            }
        };
        service.start();
    }

}
