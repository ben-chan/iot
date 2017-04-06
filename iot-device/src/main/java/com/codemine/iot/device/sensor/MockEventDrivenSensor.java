/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codemine.iot.device.sensor;

import com.codemine.iot.device.sensor.EventDrivenSensor;

/**
 *
 * @author demof
 */
public class MockEventDrivenSensor<Data> extends EventDrivenSensor<Data> {

    private Data mockData;

    public MockEventDrivenSensor(Data mockData) {
        this.mockData = mockData;
    }

    @Override
    public void startListenEvent() {

    }

    @Override
    public void stopListenEvent() {

    }

    @Override
    public Data readData() throws Throwable {
        return this.mockData;
    }

}
