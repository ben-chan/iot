/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codemine.iot.device.sensor;

import com.codemine.iot.device.sensor.SR04UltrasonicSensor.OutputValue;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author benchan
 */
public class SR04UltrasonicSensor extends PollingSensor<OutputValue> {
    
    private final GpioPinDigitalOutput triggerPin;
    private final GpioPinDigitalInput echoPin;
    private final Date startTime = null;
    private final Date stopTime = null;
    private OutputValue outputValue = new OutputValue();
    private final ReentrantLock lock = new ReentrantLock();
    private final static int MS_PER_SECOND = 1000;
    private final static int ULTRA_SONIC_SINGLE_TRIP_SPEED_PER_SECOND = 17150;
    
    public SR04UltrasonicSensor(GpioPinDigitalOutput triggerPin, GpioPinDigitalInput echoPin) {
        this.triggerPin = triggerPin;
        this.echoPin = echoPin;
        this.echoPin.addListener((GpioPinListenerDigital) (GpioPinDigitalStateChangeEvent changeEvent) -> {
            if (changeEvent.getState().isHigh()) {
                final long durationInMS = stopTime.getTime() - startTime.getTime();
                final long durationInSecond = durationInMS / MS_PER_SECOND;
                double distanceInCM = ULTRA_SONIC_SINGLE_TRIP_SPEED_PER_SECOND * durationInSecond;
                outputValue.setDistanceInCM(distanceInCM);
                lock.unlock();
            }
        });
    }
    
    @Override
    public OutputValue readOutputValue() throws Throwable {
        lock.lock();
        this.triggerPin.high();
        Thread.sleep(1);
        this.triggerPin.low();
        startTime.setTime(new Date().getTime());
        lock.tryLock();        
        return this.outputValue;
    }
    
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
}
