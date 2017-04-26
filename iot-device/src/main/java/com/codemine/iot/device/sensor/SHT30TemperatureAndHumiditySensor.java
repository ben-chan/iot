/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codemine.iot.device.sensor;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class SHT30TemperatureAndHumiditySensor extends PollingSensor<SHT30TemperatureAndHumiditySensor.OutputValue> {

    private static final Logger logger = Logger.getLogger(SHT30TemperatureAndHumiditySensor.class);
    private final I2CDevice device;
    private final static int CONFIG_BYTE_SIZE = 2;
    private final static int DATA_BYTE_SIZE = 6;
    private final static int READ_START_OFFSET = 0;

    public static class OutputValue {

        private final double humidity;
        private final double temperatureInCelsius;

        public OutputValue(double humidity, double temperatureInCelsius) {
            this.humidity = humidity;
            this.temperatureInCelsius = temperatureInCelsius;
        }

        /**
         * @return the humidity
         */
        public double getHumidity() {
            return humidity;
        }

        /**
         * @return the temperatureInCelsius
         */
        public double getTemperatureInCelsius() {
            return temperatureInCelsius;
        }
    }

    public SHT30TemperatureAndHumiditySensor(I2CBus i2cBus, int deviceAddress) throws IOException {
        device = i2cBus.getDevice(deviceAddress);
        byte[] config = new byte[CONFIG_BYTE_SIZE];
        config[0] = 0x2c;
        config[1] = 0x06;
        this.device.write(config);
    }

    @Override
    public OutputValue readOutputValue() throws Throwable {
        try {
            // Read 6 bytes of data
            // Temp msb, Temp lsb, Temp CRC, Humididty msb, Humidity lsb, Humidity CRC
            byte[] data = new byte[DATA_BYTE_SIZE];
            device.read(data, READ_START_OFFSET, DATA_BYTE_SIZE);

            // Convert the data
            int temp = ((data[0] & 0xFF) * 256) + (data[1] & 0xFF);
            double cTemp = -45 + (175 * temp / 65535.0);
            double fTemp = -49 + (315 * temp / 65535.0);
            double humidity = 100 * (((data[3] & 0xFF) * 256) + (data[4] & 0xFF)) / 65535.0;
            return new SHT30TemperatureAndHumiditySensor.OutputValue(cTemp, humidity);
        } catch (Throwable t) {
            logger.error("readOutputValue",t);
        }
        return new SHT30TemperatureAndHumiditySensor.OutputValue(-1, -1);
    }

   
}
