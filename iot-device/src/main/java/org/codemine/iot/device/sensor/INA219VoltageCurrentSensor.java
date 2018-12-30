/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codemine.iot.device.sensor;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author root
 */
public class INA219VoltageCurrentSensor extends PollingSensor<INA219VoltageCurrentSensor.OutputValue> {

    private final I2CDevice device;
    private static final int DATA_BYTE_SIZE = 2;
    private static final int DATA_READ_OFFSET = 0;
    private static final int DATA_VOLTAGE_ADDRESS = 2;
    private static final int DATA_CURRENT_ADDRESS = 1;
    private static final Logger logger = Logger.getLogger(INA219VoltageCurrentSensor.class);

    public static class OutputValue {

        final private double voltage;
        final private double currentInMA;

        public OutputValue(double voltage, double currentInAmp) {
            this.voltage = voltage;
            this.currentInMA = currentInAmp;
        }

        /**
         * @return the voltage
         */
        public double getVoltage() {
            return voltage;
        }

        /**
         * @return the currentInAmp
         */
        public double getCurrentInMA() {
            return currentInMA;
        }
    }

    public INA219VoltageCurrentSensor(I2CBus i2cBus, int deviceAddress) throws IOException {
        device = i2cBus.getDevice(deviceAddress);
    }

    @Override
    public INA219VoltageCurrentSensor.OutputValue readOutputValue() throws Throwable {
        try {
            byte[] voltageInByte = new byte[DATA_BYTE_SIZE];
            device.read(DATA_VOLTAGE_ADDRESS, voltageInByte, DATA_READ_OFFSET, DATA_BYTE_SIZE);
            byte[] currentInByte = new byte[DATA_BYTE_SIZE];
            device.read(DATA_CURRENT_ADDRESS, currentInByte, DATA_READ_OFFSET, DATA_BYTE_SIZE);
            INA219VoltageCurrentSensor.OutputValue outputValue = new INA219VoltageCurrentSensor.OutputValue(
                    calculateVoltage(voltageInByte[0], voltageInByte[1]),
                    calculateCurrent(currentInByte[0], currentInByte[1])
            );
            return outputValue;
        } catch (Throwable t) {
            logger.error("readOutputValue", t);
        }
        return new INA219VoltageCurrentSensor.OutputValue(-1, -1);
    }

    private double calculateVoltage(byte firstByte, byte secondByte) {
        int nonDataBitShiftCount = 3;
        return ((double) convertFrom16Bits(firstByte, secondByte, nonDataBitShiftCount)) / 1000 * 4;
    }

    private double calculateCurrent(byte firstByte, byte secondByte) {
        String firstByteString = convertToBinaryString(firstByte);
        if (firstByteString.charAt(0) == '1') {
            //negative current, treat it as zero
            return 0.0;
        }
        //trim sign bit
        firstByteString = firstByteString.substring(1);
        String secondByteString = convertToBinaryString(secondByte);
        String byteString = firstByteString + secondByteString;
        return (double) Integer.parseInt(byteString, 2) / 10;
    }

    private int convertFrom16Bits(byte firstByte, byte secondByte, int leftShiftCount) {
        String firstByteString = convertToBinaryString(firstByte);
        String secondByteString = convertToBinaryString(secondByte);
        String byteString = firstByteString + secondByteString;
        if (leftShiftCount > 0) {
            byteString = byteString.substring(0, byteString.length() - leftShiftCount);
        }
        return Integer.parseInt(byteString, 2);
    }

    private String convertToBinaryString(byte data) {
        final int positiveValueBinaryLength = Integer.toBinaryString(Byte.MAX_VALUE).length();
        String binaryString = Integer.toBinaryString(data);
        if (binaryString.length() > positiveValueBinaryLength) {
            //Negative number, take the last 8 bit
            binaryString = binaryString.substring(binaryString.length() - 8);
        }
        while (binaryString.length() < 8) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }

}
