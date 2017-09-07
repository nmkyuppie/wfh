/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh;

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
    private static int startButtonClickCount = 0;
    long effectiveHours = 0, effectiveMinutes = 0, effectiveSeconds = 0;
    long currentDiffHours = 0, currentDiffMinutes = 0, currentDiffSeconds = 0;

    @FXML
    private void startButtonAction(ActionEvent event) {
        isTimerActive = true;
        resetButtonStyles("START");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        Date startDate = new Date();
        System.out.println("Start Date : " + dateFormat.format(startDate));
        if (startButtonClickCount < 1) {
            clockInLabel.setText(dateFormat.format(startDate));
            startButtonClickCount++;
        } else {
            clockOutLabel.setText("");
        }
        initializeDashboard(startDate);
    }

    @FXML
    private void stopButtonAction(ActionEvent event) {
        effectiveHours += currentDiffHours;
        effectiveMinutes += currentDiffMinutes;
        effectiveSeconds += currentDiffSeconds;
        effectiveSeconds = effectiveSeconds % 60;
        effectiveMinutes = effectiveMinutes % 60;
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
        if (breakButton.getText().equalsIgnoreCase("Break")) {
            effectiveHours += currentDiffHours;
            effectiveMinutes += currentDiffMinutes;
            effectiveSeconds += currentDiffSeconds;
            effectiveSeconds = effectiveSeconds % 60;
            effectiveMinutes = effectiveMinutes % 60;
            isTimerActive = false;
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            Date startDate = new Date();
            System.out.println("Break Start Date : " + dateFormat.format(startDate));
            resetButtonStyles("BREAK");
        } else {
            isTimerActive = true;
            resetButtonStyles("CONTINUE");
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            Date startDate = new Date(
            );
            System.out.println("Break End Date : " + dateFormat.format(startDate));
            if (startButtonClickCount < 1) {
                clockInLabel.setText(dateFormat.format(startDate));
                startButtonClickCount++;
            } else {
                clockOutLabel.setText("");
            }
            initializeDashboard(startDate);
        }
    }

    @FXML
    private void reportButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("Yes");
    }

    private void resetButtonStyles(String button) {
        if (button.equalsIgnoreCase("START")) {
            startButton.setDisable(true);
            stopButton.setDisable(false);
            breakButton.setDisable(false);
            startButton.getStyleClass().add("buttonInActive");
            startButton.getStyleClass().remove("buttonActive");
            stopButton.getStyleClass().add("buttonActive");
            stopButton.getStyleClass().remove("buttonInActive");
            breakButton.getStyleClass().add("buttonActive");
            breakButton.getStyleClass().remove("buttonInActive");
        } else if (button.equalsIgnoreCase("STOP")) {
            startButton.setDisable(false);
            stopButton.setDisable(true);
            breakButton.setDisable(true);
            startButton.getStyleClass().add("buttonActive");
            startButton.getStyleClass().remove("buttonInActive");
            stopButton.getStyleClass().add("buttonInActive");
            stopButton.getStyleClass().remove("buttonActive");
            breakButton.getStyleClass().add("buttonInActive");
            breakButton.getStyleClass().remove("buttonActive");
        } else if (button.equalsIgnoreCase("BREAK")) {
            startButton.setDisable(true);
            stopButton.setDisable(true);
            breakButton.setDisable(false);
            breakButton.setText("Continue");
//            startButton.getStyleClass().add("buttonInActive");
            startButton.getStyleClass().remove("buttonActive");
            stopButton.getStyleClass().add("buttonActive");
            stopButton.getStyleClass().remove("buttonInActive");
            breakButton.getStyleClass().add("buttonActive");
            breakButton.getStyleClass().remove("buttonInActive");
        } else if (button.equalsIgnoreCase("CONTINUE")) {
            startButton.setDisable(true);
            stopButton.setDisable(false);
            breakButton.setDisable(false);
            breakButton.setText("Break");
            startButton.getStyleClass().remove("buttonActive");
            stopButton.getStyleClass().add("buttonActive");
            stopButton.getStyleClass().remove("buttonInActive");
            breakButton.getStyleClass().add("buttonActive");
            breakButton.getStyleClass().remove("buttonInActive");
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
                                                    currentDiffSeconds = diff / 1000 % 60;
                                                    currentDiffMinutes = diff / (60 * 1000) % 60;
                                                    currentDiffHours = diff / (60 * 60 * 1000);
                                                    long totalHr, totalMin, totalSec;
                                                    totalSec = currentDiffSeconds + effectiveSeconds;
                                                    currentDiffMinutes += (totalSec / 60);
                                                    totalSec = totalSec % 60;
                                                    totalMin = currentDiffMinutes + effectiveMinutes;
                                                    currentDiffHours += (totalMin / 60);
                                                    totalMin = totalMin % 60;
                                                    totalHr = currentDiffHours + effectiveHours;
                                                    String hr = (totalHr / 10 == 0) ? "0" + totalHr : "" + totalHr;
                                                    String min = (totalMin / 10 == 0) ? "0" + totalMin : "" + totalMin;
                                                    String sec = (totalSec / 10 == 0) ? "0" + totalSec : "" + totalSec;
                                                    effectiveHourCounterLabel.setText(hr + " : " + min + " : " + sec);
                                                });

                                            } else {
                                                timer.cancel();
                                            }
                                        }
                                    }, 0, 1000);
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
