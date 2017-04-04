package obdtool.com.obd2_2.enums;

/**
 * Created by Maki on 2017. 03. 31..
 */

public enum SubSVC {
    //PID
    SUPPORT_00("00", "Supported PIDs 00-20"),
    MONITOR_STATUS("01", "Monitor Status"),
    ENGINE_RPM("0C", "Engine RPM");

    private String code;
    private String value;

    SubSVC(String code, String value) {
        this.code=code;
        this.value=value;
    }

    public String getValue() { return this.value;}
    public String getCode() { return this.code;}
}
