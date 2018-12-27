/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codemine.iot.device.valve;

import org.apache.log4j.Logger;

/**
 *
 * @author benchan
 */
public class MockValve extends Valve {

    private static Logger logger = Logger.getLogger(MockValve.class);

    @Override
    public Status off() {
        logger.info("off");
        return super.off(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Status on() {
        logger.info("on");
        return super.on(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Status getStatus() {
        Status status = super.getStatus();
        logger.info("getStatus status=" + status.toString());
        return status;
    }

}
