/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.equiniti.wfh;

import com.equiniti.wfh.DBConnectivity.PostgreSQLJDBC;
import com.equiniti.wfh.bean.ReportData;
import com.equiniti.wfh.util.Win32IdleTime;
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
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.stage.Stage;

/**
 *
 * @author nachimm
 */
public class TimeTrackerController implements Initializable {
    //TABLE VIEW AND DATA
    private TableView tableview;
    
    PostgreSQLJDBC postgreSQLJDBC = null;

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
    
    TimeTrackerDAO timeTrackerDAO=new TimeTrackerDAO();
    Win32IdleTime win32IdleTime=null;
    public TimeTrackerController() {
        System.out.println("com.equiniti.wfh.TimeTrackerController.<init>() constructor");
        postgreSQLJDBC = new PostgreSQLJDBC();
    }
    
    boolean isTimerActive;
    private static int startButtonClickCount = 0;
    long effectiveHours = 0, effectiveMinutes = 0, effectiveSeconds = 0;
    long currentDiffHours = 0, currentDiffMinutes = 0, currentDiffSeconds = 0;
    Date effectiveStartDate;
    Date effectiveStopDate;
    Date breakStartDate;
    Date breakStopDate;
    Timer countDownTimer;
    Timer isSystemAwakeTimer;
    
    @FXML
    private void startButtonAction(ActionEvent event) {
        isTimerActive = true;
        resetButtonStyles("START");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
        effectiveStartDate = new Date();
        System.out.println("Start Date : " + dateFormat.format(effectiveStartDate));
        if (startButtonClickCount < 1) {
            clockInLabel.setText(dateFormat.format(effectiveStartDate));
            startButtonClickCount++;
        } else {
            clockOutLabel.setText("");
        }
        timeTrackerDAO.startTimeTracker(effectiveStartDate);
        effectiveStopDate=null;
        initializeDashboard(effectiveStartDate);
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
        effectiveStopDate = new Date();
        System.out.println("Stop Date : " + dateFormat.format(effectiveStopDate));
        timeTrackerDAO.stopTimeTracker(effectiveStartDate);
        effectiveStartDate=null;
        clockOutLabel.setText(dateFormat.format(effectiveStopDate));
//        initializeDashboard(effectiveStopDate);
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
            breakStartDate = new Date();
            timeTrackerDAO.startBreak(breakStartDate);
            breakStopDate=null;
            resetButtonStyles("BREAK");
            isSystemAwake();
        } else {
            isTimerActive = true;
            resetButtonStyles("CONTINUE");
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
            breakStopDate = new Date();
            timeTrackerDAO.stopBreak(breakStopDate);
            breakStartDate=null;
            if (startButtonClickCount < 1) {
                clockInLabel.setText(dateFormat.format(breakStopDate));
                startButtonClickCount++;
            } else {
                clockOutLabel.setText("");
            }
            initializeDashboard(breakStopDate);
        }
    }
    
    @FXML
    private void reportButtonAction(ActionEvent event) {
        System.out.println("in report button");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("ReportUI.fxml"));
            Parent root = fxmlLoader.load();
            ReportUIController reportUIController = (ReportUIController) fxmlLoader.getController();
            Scene scene = new Scene(root, 600, 400);
            Stage stage = new Stage();
            stage.setTitle("Report View");
            stage.setScene(scene);
            stage.show();
            /*
            * if "fx:controller" is not set in fxml
            * fxmlLoader.setController(NewWindowController);
             */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
//        System.out.println("Yes");
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
    
    
    private void initializeDashboard(Date startDate) {
        System.out.println("yesss");
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
                                    countDownTimer = new Timer();
                                    countDownTimer.scheduleAtFixedRate(new TimerTask() {
                                        
                                        @Override
                                        public void run() {
                                            if (isTimerActive) {
                                                Platform.runLater(() -> {
                                                    Date currentDate = new Date();
//                                                    win32IdleTime=Win32IdleTime.getInstance();
//                                                    if(win32IdleTime.getState()==Win32IdleTime.State.IDLE){
//                                                        System.out.println("idle");
//                                                    }
                                                    
                                                    
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
//                                                    System.out.println(hr + " : " + min + " : " + sec);
                                                    effectiveHourCounterLabel.setText(hr + " : " + min + " : " + sec);
                                                    Win32IdleTime win32IdleTime=new Win32IdleTime();
                                                    if(win32IdleTime.getState()==Win32IdleTime.State.IDLE){
                                                        breakButtonAction(new ActionEvent());
                                                        countDownTimer.cancel();
                                                    }
                                                });
                                                
                                            } else {
                                                countDownTimer.cancel();
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
    
    private void isSystemAwake() {
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
                                    isSystemAwakeTimer = new Timer();
                                    isSystemAwakeTimer.scheduleAtFixedRate(new TimerTask() {
                                        
                                        @Override
                                        public void run() {
                                            Platform.runLater(() -> {
                                                Win32IdleTime win32IdleTime=new Win32IdleTime();
                                                if(win32IdleTime.getState()==Win32IdleTime.State.ONLINE){
                                                    breakButtonAction(new ActionEvent());
                                                    isSystemAwakeTimer.cancel();
                                                }
                                            });
                                            
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
