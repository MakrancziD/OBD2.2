package obdtool.com.obd2_2.util;

import java.util.Date;

/**
 * Created by Maki on 2017. 06. 02..
 */

public class LiveDataPoint {
    private double value;
    private Date timestamp;

    public LiveDataPoint(double value, Date timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
