package org.smartess.proxy;

import io.prometheus.client.Gauge;
import io.prometheus.client.Info;
import io.prometheus.client.exporter.HTTPServer;
import java.io.IOException;

public class PrometheusExporter implements Runnable {
    private final ProcessInverterData procesor;
    private final Gauge batteryVoltage;
    private final Gauge batteryCharged;
    private final Gauge batteryChargingCurr;
    private final Gauge batteryDisChargingCurr;
    private final Gauge outputVoltage;
    private final Gauge outputFrequency;
    private final Gauge outputApparentPower;
    private final Gauge outputPower;
    private final Gauge outputEnergyTick;
    private final Gauge outputLoad;
    private final Gauge acVoltage;
    private final Gauge acFrequency;
    private final Gauge pvVoltage;
    private final Gauge pvPower;
    private final Gauge pvEnergyTick;
    private final Gauge mode;
    private final Gauge chargeState;
    private final Gauge loadState;
    private final Info cpuVersion;

    private final HTTPServer server;

    public PrometheusExporter(Engine engine) throws IOException {
        this.procesor = engine.procesor;

        batteryVoltage = Gauge.build()
            .name("inverter_battery_voltage")
            .help("Current inverter battery voltage")
            .register();
        batteryCharged = Gauge.build()
            .name("inverter_battery_charged")
            .help("Current inverter battery charged")
            .register();
        batteryChargingCurr = Gauge.build()
            .name("inverter_battery_charging_curr")
            .help("Current inverter battery charging current")
            .register();
        batteryDisChargingCurr = Gauge.build()
            .name("inverter_battery_discharging_curr")
            .help("Current inverter battery discharging current")
            .register();
        outputVoltage = Gauge.build()
            .name("inverter_output_voltage")
            .help("Current inverter output voltage")
            .register();
        outputFrequency = Gauge.build()
            .name("inverter_output_frequency")
            .help("Current inverter output frequency")
            .register();
        outputApparentPower = Gauge.build()
            .name("inverter_output_apparent_power")
            .help("Current inverter output apparent power")
            .register();
        outputPower = Gauge.build()
            .name("inverter_output_power")
            .help("Current inverter output power")
            .register();
        outputEnergyTick = Gauge.build()
            .name("inverter_output_energy_tick")
            .help("Current inverter output energy tick")
            .register();
        outputLoad = Gauge.build()
            .name("inverter_output_load")
            .help("Current inverter output load")
            .register();
        acVoltage = Gauge.build()
            .name("inverter_ac_voltage")
            .help("Current inverter ac voltage")
            .register();
        acFrequency = Gauge.build()
            .name("inverter_ac_frequency")
            .help("Current inverter ac frequency")
            .register();
        pvVoltage = Gauge.build()
            .name("inverter_pv_voltage")
            .help("Current inverter pv voltage")
            .register();
        pvPower = Gauge.build()
            .name("inverter_pv_power")
            .help("Current inverter pv power")
            .register();
        pvEnergyTick = Gauge.build()
            .name("inverter_pv_energy_tick")
            .help("Current inverter pv energy tick")
            .register();
        mode = Gauge.build()
            .name("inverter_mode")
            .help("Current inverter mode")
            .register();
        chargeState = Gauge.build()
            .name("inverter_charge_state")
            .help("Current inverter charge state")
            .register();
        loadState = Gauge.build()
            .name("inverter_load_state")
            .help("Current inverter load state")
            .register();
        cpuVersion = Info.build()
            .name("inverter_cpu")
            .help("Current inverter main cpu version")
            .register();

        HTTPServer tempServer;
        try {
            tempServer = new HTTPServer(9909, true);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        server = tempServer;
    }

    public void stopServer() {
        if (server != null) {
            server.close();
        }
    }

    public void run() {
        while (true) {
            synchronized (procesor) {
                batteryVoltage.set(procesor.batteryVoltage);
                batteryCharged.set(procesor.batteryCharged);
                batteryChargingCurr.set(procesor.batteryChargingCurr);
                batteryDisChargingCurr.set(procesor.batteryDisChargingCurr);
                outputVoltage.set(procesor.outputVoltage);
                outputFrequency.set(procesor.outputFrequency);
                outputApparentPower.set(procesor.outputApparentPower);
                outputPower.set(procesor.outputPower);
                outputEnergyTick.set(procesor.outputEnergyTick);
                outputLoad.set(procesor.outputLoad);
                acVoltage.set(procesor.acVoltage);
                acFrequency.set(procesor.acFrequency);
                pvVoltage.set(procesor.pvVoltage);
                pvPower.set(procesor.pvPower);
                pvEnergyTick.set(procesor.pvEnergyTick);
                mode.set(procesor.mode);
                chargeState.set(procesor.chargeState);
                loadState.set(procesor.loadState);
                cpuVersion.info("device", "NEXT_VictorMAX","mainCpuVersion", Double.toString(procesor.mainCpuVersion), "secondaryCpuVersion", Double.toString(procesor.secondaryCpuVersion));
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}