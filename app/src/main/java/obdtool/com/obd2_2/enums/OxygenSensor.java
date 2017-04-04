package obdtool.com.obd2_2.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maki on 2017. 04. 03..
 */

public enum OxygenSensor {
    OXYGEN_SENSOR_1(0x14, "Oxygen Sensor 1"),
    OXYGEN_SENSOR_2(0x15, "Oxygen Sensor 2"),
    OXYGEN_SENSOR_3(0x16, "Oxygen Sensor 3"),
    OXYGEN_SENSOR_4(0x17, "Oxygen Sensor 4"),
    OXYGEN_SENSOR_5(0x18, "Oxygen Sensor 5"),
    OXYGEN_SENSOR_6(0x19, "Oxygen Sensor 6"),
    OXYGEN_SENSOR_7(0x1A, "Oxygen Sensor 7"),
    OXYGEN_SENSOR_8(0x1B, "Oxygen Sensor 8");

    private static Map<Integer, OxygenSensor> map = new HashMap<>();

    static {
        for (OxygenSensor o2sensor : OxygenSensor.values())
            map.put(o2sensor.getValue(), o2sensor);
    }

    private final int value;
    private final String sensor;

    private OxygenSensor(final int value, final String bank) {
        this.value = value;
        this.sensor = bank;
    }

    public int getValue() {
        return value;
    }

    public String getSensor() {
        return sensor;
    }

    public final String buildObdCommand() {
        return new String("01 " + value);
    }
}
