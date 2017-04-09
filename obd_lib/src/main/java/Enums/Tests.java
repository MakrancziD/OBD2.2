package Enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Maki on 2017. 04. 08..
 */

public enum Tests {
    //general
    MISFIRE_MON(0x00, "Misfire monitoring",0),
    FUEL_SYS_MON(0x01, "Fuel system monitoring",0),
    COMP_COMP_MON(0x02, "Comprehensive component monitoring",0),
    COMP_IGN_MON(0x03, "Compression ignition monitoring",0),
    //Spark ign
    CAT_MON(0x00, "Catalyst monitoring",1),
    HEATED_CAT_MON(0x01, "Heated catalyst monitoring",1),
    EVAP_SYS_MON(0x02, "Evaporative system monitoring",1),
    SEC_AIR_MON(0x03, "Secondary air system monitoring",1),
    RESERVED_1(0x04, "Reserved",1),
    O2_SENS_MON(0x05, "Oxygen test monitoring",1),
    O2_SENS_HEAT_MON(0x06, "Oxygen test heater monitoring",1),
    EGR_VVT_MON_SPK(0x07, "EGR and/or VVT system monitoring",1),
    //Comp ign
    NMHC_CAT_MON(0x00, "NMHC catalyst monitoring",2),
    NOX_SCR_MON(0x01, "NOx/SCR aftertreatment monitoring",2),
    RESERVED_2(0x02, "Reserved",2),
    BOOST_PRES_SYS_MON(0x03, "Boost pressure system monitoring",2),
    RESERVED_3(0x04, "Reserved",2),
    EXH_GAS_MON(0x05, "Exhaust gas test monitoring",2),
    PM_FILTER_MON(0x06, "PM filter monitoring",2),
    EGR_VVT_MON_CMP(0x07, "EGR and/or VVT system monitoring",2);

    private static Map<Integer, Tests> map_gen = new HashMap<>();
    private static Map<Integer, Tests> map_spk = new HashMap<>();
    private static Map<Integer, Tests> map_cmp = new HashMap<>();

    static {
        for (Tests test : Tests.values())
            switch(test.type)
            {
                case 0:
                    map_gen.put(test.getCode(), test);
                    break;
                case 1:
                    map_spk.put(test.getCode(), test);
                    break;
                case 2:
                    map_cmp.put(test.getCode(), test);
                    break;
            }

    }

    private final int code;
    private final String test;
    private int type;

    private Tests(final int code, final String sensor, final int type) {
        this.code = code;
        this.test = sensor;
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public String getTest() {
        return test;
    }

    public static Tests getByCode(int code, int type) {

        switch(type)
        {
            case 0:
                return map_gen.get(code);
            case 1:
                return map_spk.get(code);
            case 2:
                return map_cmp.get(code);
        }
        return null;
    }
}
