package obdtool.com.obd2_2.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maki on 2017. 04. 03..
 */

public enum PIDsupport
{
    PID_01_20(0x00, "Supported PIDs from 0x01-0x20"),
    PID_21_40(0x20, "Supported PIDs from 0x21-0x40"),
    PID_41_60(0x40, "Supported PIDs from 0x41-0x60"),
    PID_61_80(0x60, "Supported PIDs from 0x61-0x80"),
    PID_81_A0(0x80, "Supported PIDs from 0x81-0xA0"),
    PID_A1_C0(0xA0, "Supported PIDs from 0xA1-0xC0"),
    PID_C1_E0(0xC0, "Supported PIDs from 0xC1-0xE0");

    private static Map<Integer, PIDsupport> map = new HashMap<>();

    static {
        for (PIDsupport range : PIDsupport.values())
            map.put(range.getValue(), range);
    }

    private final int value;
    private final String name;

    private PIDsupport(final int value, final String cmd) {
        this.value = value;
        this.name = cmd;
    }

    public static PIDsupport fromValue(final int value) {
        return map.get(value);
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public final String buildObdCommand() {
        return new String("01 " + String.format("%02X", value));
    }
}
