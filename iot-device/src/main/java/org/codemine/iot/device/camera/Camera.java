/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codemine.iot.device.camera;

import java.awt.image.BufferedImage;

/**
 *
 * @author benchan
 */
public abstract class Camera {
    public abstract BufferedImage takePhoto() throws Throwable;
}
