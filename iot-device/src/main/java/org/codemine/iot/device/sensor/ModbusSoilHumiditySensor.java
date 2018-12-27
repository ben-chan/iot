/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.codemine.iot.device.sensor;

import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import org.apache.log4j.Logger;

/**
 *
 * @author benchan
 */
public class ModbusSoilHumiditySensor extends PollingSensor<ModbusSoilHumiditySensor.OutputValue> {

    private static final Logger logger = Logger.getLogger(ModbusSoilHumiditySensor.class);

    public static class OutputValue {

        private final int temperatureInCelsius;
        private final int dryLevel;

        public OutputValue(int temperatureInCelsius, int dryLevel) {
            this.temperatureInCelsius = temperatureInCelsius;
            this.dryLevel = dryLevel;
        }

        /**
         * @return the temperatureInCelsius
         */
        public int getTemperatureInCelsius() {
            return temperatureInCelsius;
        }

        /**
         * @return the dryLevel
         */
        public int getDryLevel() {
            return dryLevel;
        }
    }

    private ModbusSerialMaster serialMaster = null;

    public ModbusSoilHumiditySensor(String serialPortName) throws Exception {
        SerialParameters params = new SerialParameters();
        params.setPortName(serialPortName);
        params.setBaudRate(9600);
        params.setParity(0);
        params.setDatabits(8);
        params.setStopbits(1);
        params.setEncoding("rtu");
        serialMaster = new ModbusSerialMaster(params);
        serialMaster.connect();
    }

    @Override
    public OutputValue readOutputValue() {
        try {
            InputRegister[] temperatureRegisters = serialMaster.readInputRegisters(1, 0, 1);
            byte temperatureLowByte = temperatureRegisters[0].toBytes()[0];
            byte temperatureHighByte = temperatureRegisters[0].toBytes()[1];

            final int temperature = ((temperatureLowByte * 256) + temperatureHighByte) / 100;

            InputRegister[] dryLevelRegisters = serialMaster.readInputRegisters(1, 4, 1);
            byte dryLevelLowByte = dryLevelRegisters[0].toBytes()[0];
            byte dryLevelHighByte = dryLevelRegisters[0].toBytes()[1];

            final int dryLevel = ((dryLevelLowByte * 256) + dryLevelHighByte);
            return new OutputValue(temperature, dryLevel);
        } catch (Throwable t) {
            logger.error("readOutputValue", t);
        }
        return new OutputValue(-1, -1);
    }
}
