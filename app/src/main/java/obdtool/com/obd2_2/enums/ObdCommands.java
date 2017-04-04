package obdtool.com.obd2_2.enums;

/**
 * Created by Maki on 2017. 03. 31..
 */

public enum ObdCommands {
    MONITOR_STATUS("Monitor status since DTCs cleared");

    private final String value;

    ObdCommands(String value)
    {
        this.value=value;
    }

    public final String getValue() {
        return value;
    }
}
