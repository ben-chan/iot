/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codemine.iot.device.sensor;

/**
 * 
 * @author benchan
 */
public abstract class Sensor<OutputValue> {
	public abstract OutputValue readOutputValue() throws Throwable;

	
}
