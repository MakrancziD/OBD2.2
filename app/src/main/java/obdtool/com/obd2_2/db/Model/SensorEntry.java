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
    public static final String FIELD_NAME_DATA = "data";
    public static final String FIELD_NAME_TRIP = "trip_ID";

    @DatabaseField(columnName = FIELD_NAME_ID)
    private int ID_sensor_entry;

    @DatabaseField(columnName = FIELD_NAME_SENSOR)
    private String sensor; //--table with supported sensors?

    @DatabaseField(columnName = FIELD_NAME_TIMESTAMP)
    private Date timestamp;

    @DatabaseField(columnName = FIELD_NAME_DATA)
    private String data;

    @DatabaseField(columnName = FIELD_NAME_TRIP, foreign = true, foreignAutoRefresh = true)
    private Trip trip_ID;

    public SensorEntry() {}
}
