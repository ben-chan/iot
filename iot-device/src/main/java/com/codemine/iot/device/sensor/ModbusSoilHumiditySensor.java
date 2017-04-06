/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codemine.iot.device.sensor;

import com.codemine.iot.device.sensor.PollingSensor;
import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.util.SerialParameters;

/**
 *
 * @author benchan
 */
public class ModbusSoilHumiditySensor extends PollingSensor<ModbusSoilHumiditySensor.Data>{
    public static class Data {

        public Data(int temperatureInCelsius, int dryLevel) {
            this.temperatureInCelsius = temperatureInCelsius;
            this.dryLevel = dryLevel;
        }
        
        public Data(){
            
        }

        private int temperatureInCelsius;
        private int dryLevel;

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
    public Data readData() throws ModbusException {

        InputRegister[] temperatureRegisters = serialMaster.readInputRegisters(1, 0, 1);
        byte temperatureLowByte = temperatureRegisters[0].toBytes()[0];
        byte temperatureHighByte = temperatureRegisters[0].toBytes()[1];

        final int temperature = ((temperatureLowByte * 256) + temperatureHighByte) / 100;

        InputRegister[] dryLevelRegisters = serialMaster.readInputRegisters(1, 4, 1);
        byte dryLevelLowByte = dryLevelRegisters[0].toBytes()[0];
        byte dryLevelHighByte = dryLevelRegisters[0].toBytes()[0];

        final int dryLevel = ((dryLevelLowByte * 256) + dryLevelHighByte);
        return new Data(temperature,dryLevel);
    }
}
