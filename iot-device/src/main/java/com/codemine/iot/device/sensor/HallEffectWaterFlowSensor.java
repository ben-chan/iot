/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codemine.iot.device.sensor;

import com.codemine.iot.device.sensor.EventDrivenSensor;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.math.BigDecimal;

/**
 *
 * @author demof
 */
public class HallEffectWaterFlowSensor extends EventDrivenSensor<HallEffectWaterFlowSensor.Data> {

    public static class Data {

        private BigDecimal totalMillilitre;

        public Data(BigDecimal totalMillilitre) {
            this.totalMillilitre = totalMillilitre;
        }

        /**
         * @return the totalMillilitre
         */
        public BigDecimal getTotalMillilitre() {
            return totalMillilitre;
        }

        /**
         * @param totalMillilitre the totalMillilitre to set
         */
        public void setTotalMillilitre(BigDecimal totalMillilitre) {
            this.totalMillilitre = totalMillilitre;
        }

    }

    private class PulseListener implements GpioPinListenerDigital {

        private int pulseCount;

        @Override
        public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent gpdsce) {
            if (gpdsce.getState() == PinState.LOW) {
                ++pulseCount;
            }
        }

        /**
         * @return the pulseCount
         */
        public int getPulseCount() {
            return pulseCount;
        }

        /**
         * @param pulseCount the pulseCount to set
         */
        public void resetPulseCount() {
            this.pulseCount = 0;
        }

    }
    private final GpioPinDigitalInput gpioInputPin;
    private boolean startListenEvent = false;
    private final int pulsePerLiter = 450;
    private final int millilitrePerLiter = 1000;
    private final PulseListener pulseListener = new PulseListener();

    public HallEffectWaterFlowSensor(GpioController gpioController, Pin inputPin) {
        gpioInputPin = gpioController.provisionDigitalInputPin(inputPin, "WaterFlowSensor", PinPullResistance.PULL_UP);
    }

    @Override
    public HallEffectWaterFlowSensor.Data readData() throws Throwable {
        return new HallEffectWaterFlowSensor.Data(BigDecimal.valueOf(pulseListener.getPulseCount() * 2.27));
    }

    @Override
    public void startListenEvent() {
        if (startListenEvent) {
            return;
        }
        pulseListener.resetPulseCount();
        this.gpioInputPin.addListener(pulseListener);
        startListenEvent = true;

    }

    @Override
    public void stopListenEvent() {
        if (!startListenEvent) {
            return;
        }
        this.gpioInputPin.removeAllListeners();
        startListenEvent = false;
    }

}
