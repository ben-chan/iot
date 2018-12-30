/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codemine.iot.device.sensor;

import org.codemine.iot.device.sensor.SR04UltrasonicSensor.OutputValue;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import java.util.concurrent.TimeoutException;

/**
 *
 * @author benchan
 */
public class SR04UltrasonicSensor extends PollingSensor<OutputValue> {

    private final GpioPinDigitalOutput triggerPin;
    private final GpioPinDigitalInput echoPin;
    private final static int ULTRASONIC_SINGLE_TRIP_SPEED_PER_SECOND = 17150;
    private final static int NANOSECOND_PER_SECOND = 1000000000;
    private final static int EMIT_ULTRASONIC_DURATION_IN_NS = 15000;
    private final static int MAX_TIMEOUT_COUNT = 10000000;

    public static class OutputValue {

        private double distanceInCM;

        public OutputValue() {

        }

        /**
         * @return the distanceInCM
         */
        public double getDistanceInCM() {
            return distanceInCM;
        }

        /**
         * @param distanceInCM the distanceInCM to set
         */
        public void setDistanceInCM(double distanceInCM) {
            this.distanceInCM = distanceInCM;
        }

    }

    public SR04UltrasonicSensor(GpioController gpioController, Pin triggerPin, Pin echoPin) {
        this.triggerPin = gpioController.provisionDigitalOutputPin(triggerPin);
        this.triggerPin.low();
        this.echoPin = gpioController.provisionDigitalInputPin(echoPin);
    }

    @Override
    public OutputValue readOutputValue() throws Throwable {

        this.triggerPin.high();
        Thread.sleep(0, EMIT_ULTRASONIC_DURATION_IN_NS);
        this.triggerPin.low();

        long starttime = 0;
        long timeoutCount = MAX_TIMEOUT_COUNT;
        while (this.echoPin.isLow() && timeoutCount >= 0) {
            starttime = System.nanoTime();
            --timeoutCount;
        }
        long endtime = 0;
        timeoutCount = Integer.MAX_VALUE;
        while (this.echoPin.isHigh() && timeoutCount >= 0) {
            endtime = System.nanoTime();
        }
        final double durationInNS = endtime - starttime;
        final double durationInSecond = durationInNS / NANOSECOND_PER_SECOND;
        double distanceInCM = ULTRASONIC_SINGLE_TRIP_SPEED_PER_SECOND * durationInSecond;
        if (timeoutCount <= 0) {
            throw new TimeoutException();
        }
        final SR04UltrasonicSensor.OutputValue outputValue = new SR04UltrasonicSensor.OutputValue();
        outputValue.setDistanceInCM(distanceInCM);
        return outputValue;
    }

}
