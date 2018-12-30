/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codemine.iot.device.camera;

import com.codemine.iot.device.sensor.INA219VoltageCurrentSensor;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import org.apache.log4j.Logger;

/**
 *
 * @author benchan
 */
public class USBCamera extends Camera {
    
    static {
        Webcam.setDriver(new V4l4jDriver());
    }
    private final Dimension imageResolution;
    private static final Logger logger = Logger.getLogger(USBCamera.class);
    
    public USBCamera(Dimension imageResolution) {
        this.imageResolution = imageResolution;
    }
    
    @Override
    public BufferedImage takePhoto() throws Throwable {
        Webcam webcam = null;
        try {
            webcam = Webcam.getDefault();
            webcam.setViewSize(imageResolution);
            webcam.open();
            return webcam.getImage();
        } catch (Throwable t) {
            logger.error("takePhoto", t);
            throw t;
        } finally {
            if (webcam != null && webcam.isOpen()) {
                webcam.close();
            }
        }
        
    }
}
