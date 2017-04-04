package obdtool.com.obd2_2.enums;

/**
 * Created by Maki on 2017. 03. 31..
 */

public enum NRC {
    GR("10","General Reject"),
    SNS("11", "Service Not Supported"),
    SFNSIF("12", "subFunctionNotSupported-InvalidFormat"),
    BRR("21", "busy-repeatRequest"),
    CNCORSE("22", "conditionsNotCorrect OrRequestSequenceError"),
    RCRRP("78", "requestCorrectlyReceived-ResponsePending");

    private String code;
    private String value;

    NRC(String code, String value) {
        this.code=code;
        this.value=value;
    }

    public String getValue() { return this.value;}
}
