package obdtool.com.obd2_2.util;

/**
 * Created by Maki on 2017. 03. 28..
 */

public class Enum {

    public enum connectionState {
        DISCONNECTED,
        BLUETOOTH_ON,
        CONNECTING_BT_DEV,
        BT_DEV_CONNECTED,
        INIT_OBD,
        OBD_READY,
        BT_ERROR,
        OBD_ERROR
    }

    public enum oxygenSensor {
        OXYGEN_SENSOR_1,
        OXYGEN_SENSOR_2,
        OXYGEN_SENSOR_3,
        OXYGEN_SENSOR_4,
        OXYGEN_SENSOR_5,
        OXYGEN_SENSOR_6,
        OXYGEN_SENSOR_7,
        OXYGEN_SENSOR_8
    }

    public enum pidSupport {
        PID_01_20,
        PID_21_40,
        PID_41_60,
        PID_61_80,
        PID_81_A0,
        PID_A1_C0,
        PID_C1_E0,
    }

}
