package obdtool.com.obd2_2.enums;

/**
 * Created by Maki on 2017. 03. 31..
 */

public enum SVC {
    CURRENT_DATA("01","Current diagnostic data"),
    FREEZEFRAME_DATA("02","Freeze frame data"),
    DTC_LIST("03","List DTCs"),
    DTC_CLEAR("04","Clear DTCs"),
    O2_TESTS("05","O2 sensor test results"),
    OTHER_TESTS("06","Other test results"),
    PENDING_DTC("07","Pending DTCs"),
    VEHICLE_INFO("09","Vehicle Information"),
    PERMANENT_DTC("0A","Permanent DTCs");

    private String code;
    private String value;

    SVC(String code, String value) {
        this.value=value;
    }

    public String getValue() { return this.value;}
    public String getCode() { return this.code;}
}
