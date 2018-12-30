/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codemine.iot.device.camera;

import java.awt.image.BufferedImage;

/**
 *
 * @author benchan
 */
public class MockCamera extends Camera {
    
    public MockCamera() {
    }

    @Override
    public BufferedImage takePhoto() throws Throwable {
        return new BufferedImage(1, 1, BufferedImage.TYPE_INT_BGR);
    }

}
