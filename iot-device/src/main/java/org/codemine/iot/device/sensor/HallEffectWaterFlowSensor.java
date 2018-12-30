/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codemine.iot.device.sensor;

import org.codemine.iot.device.sensor.EventDrivenSensor;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.log4j.Logger;

/**
 *
 * @author demof
 */
public class HallEffectWaterFlowSensor extends
        EventDrivenSensor<HallEffectWaterFlowSensor.OutputValue> {

    private static final Logger logger = Logger
            .getLogger(HallEffectWaterFlowSensor.class);

    public static class OutputValue {

        private BigDecimal totalMillilitre;

        public OutputValue(BigDecimal totalMillilitre) {
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

        private AtomicInteger pulseCount = new AtomicInteger(0);

        @Override
        public void handleGpioPinDigitalStateChangeEvent(
                GpioPinDigitalStateChangeEvent gpdsce) {
            if (gpdsce.getState() == PinState.LOW) {
                pulseCount.incrementAndGet();
            }
        }

        /**
         * @return the pulseCount
         */
        public int getPulseCount() {
            return pulseCount.intValue();
        }

        /**
         * @param pulseCount the pulseCount to set
         */
        public void resetPulseCount() {
            this.pulseCount.set(0);
        }

    }

    private final GpioPinDigitalInput gpioInputPin;
    private final PulseListener pulseListener = new PulseListener();

    public HallEffectWaterFlowSensor(GpioController gpioController, Pin inputPin) {
        gpioInputPin = gpioController.provisionDigitalInputPin(inputPin,
                "WaterFlowSensor", PinPullResistance.PULL_UP);
    }

    @Override
    public HallEffectWaterFlowSensor.OutputValue readOutputValue()
            throws Throwable {
        return new HallEffectWaterFlowSensor.OutputValue(
                BigDecimal.valueOf(pulseListener.getPulseCount() * 2.27));
    }

    @Override
    public void startListenEvent() {
        this.stopListenEvent();
        pulseListener.resetPulseCount();
        this.gpioInputPin.addListener(pulseListener);
    }

    @Override
    public void stopListenEvent() {
        this.gpioInputPin.removeAllListeners();
    }

}
