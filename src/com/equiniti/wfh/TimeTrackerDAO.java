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
        startEffectiveHour(startDate);
    }

    void stopTimeTracker(Date stopDate) {
        stopEffectiveHour(stopDate);
        System.out.println("Stopping session @ "+stopDate);
    }

    void startBreak(Date breakStartDate) {
        stopEffectiveHour(breakStartDate);
        System.out.println("Starting break @ "+breakStartDate);
    }

    void stopBreak(Date breakStopDate) {
        System.out.println("Stopping break @ "+breakStopDate);
        startEffectiveHour(breakStopDate);
    }

    void startEffectiveHour(Date startDate) {
        System.out.println("Starting effective hour @ "+startDate);
    }

    private void stopEffectiveHour(Date stopDate) {
        System.out.println("Stopping effective hour @ "+stopDate);
    }

    void startIdleHour(Date startDate) {
        stopEffectiveHour(startDate);
        System.out.println("Starting idle hour @ "+startDate);
    }

    void stopIdleHour(Date stopDate) {
        System.out.println("Stopping idle hour @ "+stopDate);
    }
    
}
