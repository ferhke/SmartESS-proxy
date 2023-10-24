package org.smartess.proxy;

public class FakeClient extends ModbusClient {

    public FakeClient(Engine engine) throws Exception {
        super(engine);
        this.engine = engine;
    }

    private Engine engine;
    // private Socket srv;

    private short cnt = 0;
    private static String cfg = "00000001000AFF04050311C20010E142";
    // private static String ping = "00000001000AFF01170A160519350023";
    private static String getData = "00000001000AFF0405031195002D9143";

    public void run() {
        while (true) {
            try {
                sendMsgToClient(cfg);
                // int cnt = 0;
                while (true) {
                    byte[] cntBytes=new byte[]{(byte)(cnt>>>8),(byte)(cnt&0xFF)};
                    int res = sendMsgToClient(Engine.bytesToHex(cntBytes) + getData.substring(4));                   
                    if (cnt >= 0xFFFF)
                        cnt = 0; 
                    else 
                        cnt++;
                    if (res == -1)
                        break;
                    Thread.sleep(Engine.fakeClientUpdateFrequency * 1000);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
 
    public int sendData(byte[] data) throws InterruptedException {
        return 0;
    }
 
    private int sendMsgToClient(String msg) throws InterruptedException {
        while (engine.nsrv == null || engine.nsrv.node == null)
            Thread.sleep(100);
        byte[] data = Engine.hexStringToByteArray(msg);
        int res = engine.nsrv.sendData(data);
        Engine.logger.info("Server: " + msg);
        return res;
    }

}
