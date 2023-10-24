package tester;

public class testDataExtract {
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


    public testDataExtract() {
        String hex = "63BC00010061FF0405035A04001109F301BE11AB00D8000000000000001109F3019C0847081500150000000000750900000200D827D827E6002C00E001E600F4012C0000000001F923D200A80C1900DC01020002000000020000003C00E6003C00E0011C02A396";
        byte[] data = hexStringToByteArray(hex);
        if (data[2] == 0x00 && data[3] == 0x01 && data[4] == 0x00 && data[5] == 0x61) {
            double batteryVoltage = getData(data, batteryVoltageIdx, 10);
            System.out.println("batteryVoltage: " + batteryVoltage);
            int batteryCharged = getData(data, batteryChargedIdx, 1, true);
            System.out.println("batteryCharged: " + batteryCharged);
            double batteryChargingCurr = getData(data,
                    batteryChargingCurrIdx, 10);
            System.out.println("batteryChargingCurr: " + batteryChargingCurr);
            double batteryDisChargingCurr = getData(data,
                    batteryDisChargingCurrIdx, 10);
            System.out.println(
                    "batteryDisChargingCurr: " + batteryDisChargingCurr);
            double outputVoltage = getData(data, outputVoltageIdx, 10);
            System.out.println("outputVoltage: " + outputVoltage);
            double outputFrequency = getData(data, outputFrequencyIdx, 10);
            System.out.println("outputFrequency: " + outputFrequency);
            int outputPower = getData(data, outputPowerIdx, 1, true);
            System.out.println("outputPower: " + outputPower);
            int outputLoad = getData(data, outputLoadIdx, 1, true);
            System.out.println("outputLoad: " + outputLoad);
            double acVoltage = getData(data, acVoltageIdx, 10);
            System.out.println("acVoltage: " + acVoltage);
            double acFrequency = getData(data, acFrequencyIdx, 10);
            System.out.println("acFrequency: " + acFrequency);
            double pvVoltage = getData(data, pvVoltageIdx, 10);
            System.out.println("pvVoltage: " + pvVoltage);
            int pvPower = getData(data, pvPowerIdx, 1, true);
            System.out.println("pvPower: " + pvPower);
            int mode = getData(data, modeIdx, 1, true);
            System.out.println("mode: " + mode);
            int chargeState = getData(data, chargeStateIdx, 1, true);
            System.out.println("chargeState: " + chargeState);
            int loadState = getData(data, loadStateIdx, 1, true);
            System.out.println("loadState"+loadState);
        }
    }

    private double getData(byte[] data, short idx,
            int denominator) {
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

    public static void main(String[] args) {
        new testDataExtract();
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
