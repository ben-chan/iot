/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codemine.iot.device.valve;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.RaspiPin;

/**
 *
 * @author benchan
 */
public class WaterValve extends Valve {

    private GpioPinDigitalOutput gpioOutputPin = null;

    public WaterValve(GpioController gpio,Pin pin) {
        gpioOutputPin = gpio.provisionDigitalOutputPin(pin, "valve");
    }

    @Override
    public Status off() {
        gpioOutputPin.low();
        assert(gpioOutputPin.isLow());
        return super.off();
    }

    @Override
    public Status on() {
        gpioOutputPin.high();
        assert(gpioOutputPin.isHigh());
        return super.on();
    }
    
    
}
