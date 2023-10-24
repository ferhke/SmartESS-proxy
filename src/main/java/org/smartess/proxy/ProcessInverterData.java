package org.smartess.proxy;

import java.sql.Timestamp;

public class ProcessInverterData implements Runnable {

    private Engine engine;
    private short modeIdx = 11;
    private short acVoltageIdx = 13;
    private short acFrequencyIdx = 15;
    private short pvVoltageIdx = 17;
    private short pvPowerIdx = 19;
    private short batteryVoltageIdx = 21;
    private short batteryChargedIdx = 23;
    private short batteryChargingCurrIdx = 25;
    private short batteryDisChargingCurrIdx = 27;
    private short outputVoltageIdx = 29;
    private short outputFrequencyIdx = 31;
    private short outputApparentPowerIdx = 33;
    private short outputPowerIdx = 35;
    private short outputLoadIdx = 37;
    private short chargeStateIdx=84;
    private short loadStateIdx=86;
    private short mainCpuVersionIdx=45;
    private short secondaryCpuVersionIdx=47;

    public Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    public Timestamp lastTimestamp;
    public double batteryVoltage;
    public int batteryCharged;
    public double batteryChargingCurr;
    public double batteryDisChargingCurr;
    public double outputVoltage;
    public double outputFrequency;
    public double outputApparentPower;
    public int outputPower;
    public int outputLoad;
    public double acVoltage;
    public double acFrequency;
    public double pvVoltage;
    public int pvPower;
    public int mode;
    public int chargeState;
    public int loadState;
    public double mainCpuVersion;
    public double secondaryCpuVersion;
    public double pvEnergyTick;
    public double outputEnergyTick;


    public ProcessInverterData(Engine engine) {
        this.engine = engine;
    }

    public void run() {
        while (true)
            try {
                while (engine.lastData == null || engine.lastData.length == 0) 
                    Thread.sleep(100);
                byte[] data = engine.lastData;
                lastTimestamp = timestamp;
                timestamp = new Timestamp(System.currentTimeMillis());
                final long timeDiff = timestamp.getTime() - lastTimestamp.getTime();
                String hex = Engine.bytesToHex(data);
                if (data[2] == 0x00 && data[3] == 0x01 && data[4] == 0x00 && data[5] == 0x61 ) {
                    // message type 0xXXXX000106
                    batteryVoltage = getData(data, batteryVoltageIdx,
                            10);
                    engine.mqtt.sendMsg("batteryVoltage", batteryVoltage);
                    batteryCharged = getData(data, batteryChargedIdx, 1,
                            true);
                    engine.mqtt.sendMsg("batteryCharged", batteryCharged);
                    batteryChargingCurr = getData(data,
                            batteryChargingCurrIdx, 10);
                    engine.mqtt.sendMsg("batteryChargingCurr",
                            batteryChargingCurr);
                    batteryDisChargingCurr = getData(data,
                            batteryDisChargingCurrIdx, 10);
                    engine.mqtt.sendMsg("batteryDisChargingCurr",
                            batteryDisChargingCurr);
                    outputVoltage = getData(data, outputVoltageIdx, 10);
                    engine.mqtt.sendMsg("outputVoltage", outputVoltage);
                    outputFrequency = getData(data, outputFrequencyIdx,
                            10);
                    engine.mqtt.sendMsg("outputFrequency", outputFrequency);
                    outputApparentPower = getData(data, outputApparentPowerIdx, 1); 
                    engine.mqtt.sendMsg("outputApparentPower", outputApparentPower);
                    final int lastOutputPower = outputPower;
                    outputPower = getData(data, outputPowerIdx, 1, true);
                    engine.mqtt.sendMsg("outputPower", outputPower);
                    outputEnergyTick = (outputPower + lastOutputPower) / 2 * timeDiff / 1000.0 / 3600.0;
                    engine.mqtt.sendMsg("outputEnergyTick", outputEnergyTick);
                    outputLoad = getData(data, outputLoadIdx, 1, true);
                    engine.mqtt.sendMsg("outputLoad", outputLoad);
                    acVoltage = getData(data, acVoltageIdx, 10);
                    engine.mqtt.sendMsg("acVoltage", acVoltage);
                    acFrequency = getData(data, acFrequencyIdx, 10);
                    engine.mqtt.sendMsg("acFrequency", acFrequency);
                    pvVoltage = getData(data, pvVoltageIdx, 10);
                    engine.mqtt.sendMsg("pvVoltage", pvVoltage);
                    mainCpuVersion = getData(data, mainCpuVersionIdx, 1);
                    engine.mqtt.sendMsg("mainCpuVersion", mainCpuVersion);
                    secondaryCpuVersion = getData(data, secondaryCpuVersionIdx, 1);
                    engine.mqtt.sendMsg("secondaryCpuVersion", secondaryCpuVersion);
                    final int lastPvPower = pvPower;
                    pvPower = getData(data, pvPowerIdx, 1, true);
                    engine.mqtt.sendMsg("pvPower", pvPower);
                    pvEnergyTick = (pvPower + lastPvPower) / 2 * timeDiff / 1000.0 / 3600.0;
                    engine.mqtt.sendMsg("pvEnergyTick", pvEnergyTick);
                    mode = getData(data, modeIdx, 1, true);
                    engine.mqtt.sendMsg("mode", mode);
                    chargeState = getData(data, chargeStateIdx, 1, true);
                    engine.mqtt.sendMsg("chargeState", chargeState);
                    loadState = getData(data, loadStateIdx, 1, true);
                    engine.mqtt.sendMsg("loadState", loadState);
                    Engine.logger.info("{ batteryVoltage: " + batteryVoltage
                        + " ,batteryCharged: " + batteryCharged
                        + " ,batteryChargingCurr: " + batteryChargingCurr
                        + " ,batteryDisChargingCurr: " + batteryDisChargingCurr
                        + " ,outputVoltage: " + outputVoltage
                        + " ,outputFrequency: " + outputFrequency
                        + " ,outputApparentPower: " + outputApparentPower
                        + " ,outputPower: " + outputPower
                        + " ,outputEnergyTick: " + outputEnergyTick
                        + " ,outputLoad: " + outputLoad
                        + " ,acVoltage: " + acVoltage
                        + " ,acFrequency: " + acFrequency
                        + " ,pvVoltage: " + pvVoltage
                        + " ,pvPower: " + pvPower
                        + " ,pvEnergyTick: " + pvEnergyTick
                        + " ,mainCpuVersion: " + mainCpuVersion
                        + " ,secondaryCpuVersion: " + secondaryCpuVersion
                        + " ,mode: " + mode
                        + " ,chargeState: " + chargeState
                        + " ,loadState: " + loadState + " }"
                    );                          
                } if (data[2] == 0x00 && data[3] == 0x01 && data[4] == 0x00 && data[5] == 0x27 ) {
                    int chargeState=-1;
                    int loadState=-1;
                    if(hex.equals(MQTTClient.chargeSolarOnly)) chargeState=3;
                    else if(hex.equals(MQTTClient.chargeSolarUtility)) chargeState=2;
                    else if(hex.equals(MQTTClient.loadSBU)) loadState=2;
                    else if(hex.equals(MQTTClient.loadUtility)) loadState=0;
                    if(chargeState!=-1) engine.mqtt.sendMsg("chargeState", chargeState);
                    if(loadState!=-1) engine.mqtt.sendMsg("loadState", loadState);
                    Engine.logger.info("chargeState: " + chargeState
                        + " loadState: " + loadState);
                } if (data[2] == 0x00 && data[3] == 0x01 && data[4] == 0x01) {
                    int chargeState=-1;
                    int loadState=-1;
                    if(hex.equals(MQTTClient.chargeSolarOnly)) chargeState=3;
                    else if(hex.equals(MQTTClient.chargeSolarUtility)) chargeState=2;
                    else if(hex.equals(MQTTClient.loadSBU)) loadState=2;
                    else if(hex.equals(MQTTClient.loadUtility)) loadState=0;
                    if(chargeState!=-1) engine.mqtt.sendMsg("chargeState", chargeState);
                    if(loadState!=-1) engine.mqtt.sendMsg("loadState", loadState);
                    Engine.logger.info("chargeState: " + chargeState
                        + " loadState: " + loadState); 
                } if  (data[2] == 0x01 && data[3] == 0x02 && data[4] == 0x00 && data[5] == 0x10) {
                    Engine.logger.info("Ping: " + hex);
                } else {
                    Engine.logger.info("Unknown data: " + hex);
                }
                engine.lastData = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    private double getData(byte[] data, short idx, int denominator) {
        int b = 0;
        for (int i = 1; i >= 0; i--) {
            b = (b << 8) + (data[idx + i] & 0xFF);
        }
        return b * 1.0 / denominator;
    }

    private int getData(byte[] data, short idx, int denominator,
            boolean toInt) {
        int b = 0;
        for (int i = 1; i >= 0; i--) {
            b = (b << 8) + (data[idx + i] & 0xFF);
        }
        return b / denominator;
    }

}
