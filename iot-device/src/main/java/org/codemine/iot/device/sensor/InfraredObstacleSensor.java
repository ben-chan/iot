/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codemine.iot.device.sensor;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;

/**
 *
 * @author root
 */
public class InfraredObstacleSensor extends PollingSensor<Boolean> {

    private final GpioPinDigitalInput gpioInputPin;

    public InfraredObstacleSensor(GpioController gpioController, Pin pin) {
        gpioInputPin = gpioController.provisionDigitalInputPin(pin, PinPullResistance.PULL_UP);
    }

    @Override
    public Boolean readOutputValue() throws Throwable {
        return gpioInputPin.isLow();
    }

}
