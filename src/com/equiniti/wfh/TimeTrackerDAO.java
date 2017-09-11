/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh;

import java.util.Date;

/**
 *
 * @author nachimm
 */
class TimeTrackerDAO {

    void startTimeTracker(Date startDate) {
        System.out.println("Starting session @ "+startDate);
    }

    void stopTimeTracker(Date stopDate) {
        System.out.println("Stopping session @ "+stopDate);
    }

    void startBreak(Date breakStartDate) {
        System.out.println("Starting break @ "+breakStartDate);
    }

    void stopBreak(Date breakStopDate) {
        System.out.println("Stopping break @ "+breakStopDate);
    }
    
}
