/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codemine.iot.device.valve;

/**
 *
 * @author benchan
 */
public abstract class Valve {

    private Status status=Status.Off;
    public static enum Status {
        On, Off
    };
    
    public Status getStatus(){
        return this.status;
    }
    
    
    public Status on(){
        this.status=Status.On;
        return this.status;
    }
    
    public Status off(){
        this.status=Status.Off;
        return this.status;
    }
    
    
}
