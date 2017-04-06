/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codemine.iot.device.camera;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.v4l4j.V4l4jDriver;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 *
 * @author benchan
 */
public class USBCamera extends Camera {

    static {
        Webcam.setDriver(new V4l4jDriver());
    }
    private final Dimension imageResolution;

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
            throw t;
        } finally {
            if (webcam != null && webcam.isOpen()) {
                webcam.close();
            }
        }
        /*String filename = UUID.randomUUID().toString();
        String imageFilepathname = imageFileDirectory + "/" + filename + ".png";
        Webcam webcam = null;
        try {
            webcam = Webcam.getDefault();
            webcam.setViewSize(imageResolution);
            webcam.open();
            ImageIO.write(webcam.getImage(), "PNG", new File(imageFilepathname));
            webcam.close();
        } catch (Throwable t) {
            throw t;
        } finally {
            if (webcam != null && webcam.isOpen()) {
                webcam.close();
            }
        }

        File imagefile = new File(this.imageFileDirectory, filename + ".png");
        if (!imagefile.exists()) {
            throw new FileNotFoundException(filename);
        }

        return imagefile;*/
    }
}
