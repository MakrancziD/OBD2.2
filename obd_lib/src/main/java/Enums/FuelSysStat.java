package Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maki on 2017. 04. 09..
 */

public enum FuelSysStat {
    OPEN(0x01, "Open loop - has not yet satisfied conditions to go closed loop"),
    CLOSED(0x02, "Closed loop - using oxygen sensor(s) as feedback for fuel control"),
    OPEN_DRV(0x04, "Open loop due to driving conditions (e.g. power enrichment, deceleration enleanment)"),
    OPEN_FLT(0x08, "Open loop - due to detected system fault"),
    CLOSED_FLT(0x16, "Closed loop, but fault with at least one oxygen sensor - may be using single oxygen sensor for fuel control");

    private static Map<Integer, FuelSysStat> map = new HashMap<>();

    static {
        for (FuelSysStat state : FuelSysStat.values())
            map.put(state.getCode(), state);
    }

    private final int code;
    private final String status;

    private FuelSysStat(final int code, final String sensor) {
        this.code = code;
        this.status = sensor;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public static FuelSysStat getByCode(int code) {
        return map.get(code);
    }
}
