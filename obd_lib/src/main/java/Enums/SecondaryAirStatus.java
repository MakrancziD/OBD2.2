package Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by makra on 2017. 04. 10..
 */

public enum SecondaryAirStatus {
    UPSTR(0x01, "Upstream"),
    DNSTR(0x02, "Downstream of catalytic converter"),
    OUT(0x04, "From the outside atmosphere or off"),
    PUMP(0x08, "Pump commanded on for diagnostics");
    private static Map<Integer, SecondaryAirStatus> map = new HashMap<>();

    static {
        for (SecondaryAirStatus state : SecondaryAirStatus.values())
            map.put(state.getCode(), state);
    }

    private final int code;
    private final String status;

    private SecondaryAirStatus(final int code, final String sensor) {
        this.code = code;
        this.status = sensor;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public static SecondaryAirStatus getByCode(int code) {
        return map.get(code);
    }
}