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
    
    Win32IdleTime win32IdleTime;
    @Override
    public void run() {
        System.out.println("Hi ");
        win32IdleTime = Win32IdleTime.getInstance();
        
    }

    public Win32IdleTime getWin32IdleTime() {
        return win32IdleTime;
    }

    public void setWin32IdleTime(Win32IdleTime win32IdleTime) {
        this.win32IdleTime = win32IdleTime;
    }
}
