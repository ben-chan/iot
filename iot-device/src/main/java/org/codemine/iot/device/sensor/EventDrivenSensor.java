/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codemine.iot.device.sensor;

import org.codemine.iot.device.sensor.Sensor;

/**
 *
 * @author demof
 */
public abstract class EventDrivenSensor<OutputValue> extends Sensor<OutputValue> {
    public abstract void startListenEvent();
    public abstract void stopListenEvent();
}
