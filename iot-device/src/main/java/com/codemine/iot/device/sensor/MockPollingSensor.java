/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codemine.iot.device.sensor;

import com.codemine.iot.device.sensor.PollingSensor;
import org.apache.log4j.Logger;
import com.codemine.iot.device.camera.MockCamera;

/**
 *
 * @author benchan
 */
public class MockPollingSensor<Data> extends PollingSensor<Data>{
    private static Logger logger = Logger.getLogger(MockPollingSensor.class);
    
    private Data mockData;
    public MockPollingSensor(Data mockData){
        this.mockData=mockData;
    }

    @Override
    public Data readOutputValue() throws Throwable {
        logger.info("readData mockData="+this.mockData.toString());
        return this.mockData;
    }
    
}
