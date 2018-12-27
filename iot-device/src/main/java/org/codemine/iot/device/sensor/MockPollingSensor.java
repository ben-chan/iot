/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codemine.iot.device.sensor;

import org.codemine.iot.device.sensor.PollingSensor;
import org.apache.log4j.Logger;
import org.codemine.iot.device.camera.MockCamera;

/**
 *
 * @author benchan
 */
public class MockPollingSensor<OutputValue> extends PollingSensor<OutputValue>{
    private static Logger logger = Logger.getLogger(MockPollingSensor.class);
    
    private OutputValue mockData;
    public MockPollingSensor(OutputValue mockData){
        this.mockData=mockData;
    }

    @Override
    public OutputValue readOutputValue() throws Throwable {
        logger.info("readData mockData="+this.mockData.toString());
        return this.mockData;
    }
    
}
