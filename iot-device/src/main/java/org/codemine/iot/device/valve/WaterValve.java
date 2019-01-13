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
import org.codemine.iot.io.DeviceConfig;

/**
 *
 * @author benchan
 */
public class WaterValve extends Valve {

    private GpioPinDigitalOutput gpioOutputPin = null;

    private final DeviceConfig.TriggerLevel triggerlevel;

    public WaterValve(GpioController gpio, Pin pin) {
        this(gpio, pin, DeviceConfig.TriggerLevel.HIGH);
    }

    public WaterValve(GpioController gpio, Pin pin, DeviceConfig.TriggerLevel triggerLevel) {
        gpioOutputPin = gpio.provisionDigitalOutputPin(pin, "valve");
        this.triggerlevel = triggerLevel;
    }

    @Override
    public Status off() {
        if (triggerlevel == DeviceConfig.TriggerLevel.HIGH) {
            gpioOutputPin.low();
        } else {
            gpioOutputPin.high();
        }

        return super.off();
    }

    @Override
    public Status on() {
        if (triggerlevel == DeviceConfig.TriggerLevel.HIGH) {
            gpioOutputPin.high();
        } else {
            gpioOutputPin.low();
        }
        return super.on();
    }

}
