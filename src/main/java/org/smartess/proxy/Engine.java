package org.smartess.proxy;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Engine {

    static boolean fakeClient = true;
    static String mqttServer = "homeassistant.local";
    static boolean enableMqttAuth=false;
    static String mqttUser="";
    static String mqttPass="";
    static int mqttPort = 1883;
    static String mqttTopic = "solar/Inverter/";
    static int fakeClientUpdateFrequency = 15; // 10 seconds
    static String realModbusServer="47.242.188.205";


    protected static final Logger logger = LogManager.getLogger();
    
    static Executor pool = Executors.newFixedThreadPool(4);
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF"
            .getBytes(StandardCharsets.US_ASCII);
    ModbusServer nsrv;
    ModbusClient ncli;
    MQTTClient mqtt;
    ProcessInverterData procesor;
    PrometheusExporter exporter;


    byte[] lastData;


    public Engine() throws Exception {

        Properties p = new Properties();
        p.load(new FileInputStream("conf.ini"));
        fakeClient = Boolean.parseBoolean(p.getProperty("fakeClient"));
        mqttServer = p.getProperty("mqttServer");
        mqttPort = Integer.parseInt(p.getProperty("mqttPort"));
        enableMqttAuth = Boolean.parseBoolean(p.getProperty("enableMqttAuth"));
        mqttUser = p.getProperty("mqttUser");
        mqttPass = p.getProperty("mqttPass");
        mqttTopic = p.getProperty("mqttTopic");
        fakeClientUpdateFrequency = Integer.parseInt(p.getProperty("updateFrequency"));


        nsrv = new ModbusServer(this);
        pool.execute(nsrv);
        if (!fakeClient)
            ncli = new ModbusClient(this);
        else
            ncli = new FakeClient(this);
        pool.execute(ncli);
        mqtt = new MQTTClient(this);
        pool.execute(mqtt);
        procesor = new ProcessInverterData(this);
        pool.execute(procesor);
        exporter = new PrometheusExporter(this);
        pool.execute(exporter);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // test();
    }

    public static void main(String[] args) throws Exception {
        new Engine();

    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = (char) HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = (char) HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
