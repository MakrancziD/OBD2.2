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



}
