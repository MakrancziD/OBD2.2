package obdtool.com.obd2_2.db.Model;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by Maki on 2017. 03. 27..
 */

public class SensorEntry {

    public static final String TABLE_NAME = "sensor_entry";

    public static final String FIELD_NAME_ID = "ID_sensor_entry";
    public static final String FIELD_NAME_SENSOR = "sensor";
    public static final String FIELD_NAME_TIMESTAMP = "timestamp";
    public static final String FIELD_NAME_DATA_1 = "data1";
    public static final String FIELD_NAME_DATA_2 = "data2";
    public static final String FIELD_NAME_DATA_3 = "data3";
    public static final String FIELD_NAME_DATA_4 = "data4";
    public static final String FIELD_NAME_TRIP = "trip_ID";

    @DatabaseField(columnName = FIELD_NAME_ID)
    private int ID_sensor_entry;

    @DatabaseField(columnName = FIELD_NAME_SENSOR)
    private String sensor; //--table with supported sensors?

    @DatabaseField(columnName = FIELD_NAME_TIMESTAMP)
    private Date timestamp;

    @DatabaseField(columnName = FIELD_NAME_DATA_1)
    private double data1;

    @DatabaseField(columnName = FIELD_NAME_DATA_2)
    private double data2;

    @DatabaseField(columnName = FIELD_NAME_DATA_3)
    private double data3;

    @DatabaseField(columnName = FIELD_NAME_DATA_4)
    private double data4;

    @DatabaseField(columnName = FIELD_NAME_TRIP, foreign = true, foreignAutoRefresh = true)
    private Trip trip_ID;

    public SensorEntry(){}

    public SensorEntry(String sensor, Date timestamp, double data1, double data2, double data3, double data4, Trip trip) {
        this.timestamp = timestamp;
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.data4 = data4;
        this.trip_ID = trip;
    }

    public String getSensor() {
        return sensor;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getData1() {
        return data1;
    }

    public double getData2() {
        return data2;
    }

    public double getData3() {
        return data3;
    }

    public double getData4() {
        return data4;
    }
}
