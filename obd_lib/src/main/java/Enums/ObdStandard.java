package Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by makra on 2017. 04. 10..
 */

public enum ObdStandard {
    OBD2_CARB(0x1, "OBD-II as defined by the CARB"),
    OBD_EPA(0x02, "OBD as defined by the EPA"),
    OBD_OBD2(0x03, "OBD and OBD-II"),
    OBD1(0x04, "OBD-I"),
    NON_OBD(0x05, "Not OBD compliant"),
    EOBD(0x06, "EOBD (Europe)"),
    EOBD_OBD2(0x07, "EOBD and OBD-II"),
    EOBD_OBD(0x08, "EOBD and OBD"),
    EOBD_OBD_OBD2(0x09, "EOBD, OBD and OBD II"),
    JOBD(0x0A, "JOBD (Japan)"),
    JOBD_OBD2(0x0B, "JOBD and OBD II"),
    JOBD_EOBD(0x0C, "JOBD and EOBD"),
    JOBD_EOBD_OBD2(0x0D, "JOBD, EOBD, and OBD II"),
    EMD(0x11, "Engine Manufacturer Diagnostics (EMD)"),
    EMDE(0x12, "Engine Manufacturer Diagnostics Enhanced (EMD+)"),
    HD_OBD_C(0x13, "Heavy Duty On-Board Diagnostics (Child/Partial) (HD OBD-C)"),
    HD_OBD(0x14, "Heavy Duty On-Board Diagnostics (HD OBD)"),
    WWH_OBD(0x15, "World Wide Harmonized OBD (WWH OBD)"),
    HD_EOBD_I(0x17, "Heavy Duty Euro OBD Stage I without NOx control (HD EOBD-I)"),
    HD_EOBD_I_N(0x18, "Heavy Duty Euro OBD Stage I with NOx control (HD EOBD-I N)"),
    HD_EOBD_II(0x19, "Heavy Duty Euro OBD Stage II without NOx control (HD EOBD-II)"),
    HD_EOBD_II_N(0x1A, "Heavy Duty Euro OBD Stage II with NOx control (HD EOBD-II N)"),
    OBD_BR_1(0x1C, "Brazil OBD Phase 1 (OBDBr-1)"),
    OBD_BR_2(0x1D, "Brazil OBD Phase 2 (OBDBr-2)"),
    KOBD(0x1E, "Korean OBD (KOBD)"),
    IOBD_I(0x1F, "India OBD I (IOBD I)"),
    IOBD_II(0x20, "India OBD II (IOBD II)"),
    HD_EOBD_IV(0x21, "Heavy Duty Euro OBD Stage VI (HD EOBD-IV)")
    ;
    private static Map<Integer, ObdStandard> map = new HashMap<>();

    static {
        for (ObdStandard state : ObdStandard.values())
            map.put(state.getCode(), state);
    }

    private final int code;
    private final String status;

    private ObdStandard(final int code, final String sensor) {
        this.code = code;
        this.status = sensor;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public static ObdStandard getByCode(int code) {
        return map.get(code);
    }
    }
