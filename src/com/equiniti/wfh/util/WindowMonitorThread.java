/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.equiniti.wfh.util;

/**
 *
 * @author nachimm
 */
public class WindowMonitorThread extends Thread{
    
    @Override
    public void run() {
        Win32IdleTime.getInstance();
    }
}
