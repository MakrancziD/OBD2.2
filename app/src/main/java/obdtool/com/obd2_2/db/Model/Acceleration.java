package obdtool.com.obd2_2.db.Model;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by Maki on 2017. 04. 18..
 */

public class Acceleration {
    public static final String TABLE_NAME = "acceleration";

    public static final String FIELD_NAME_ID = "ID_acceleration";
    public static final String FIELD_NAME_TIMESTAMP = "timestamp";
    public static final String FIELD_NAME_FROM = "from";
    public static final String FIELD_NAME_TO = "to";
    public static final String FIELD_NAME_DATA = "data";
    public static final String FIELD_NAME_VEHICLE = "vehicle_ID";

    @DatabaseField(columnName = FIELD_NAME_ID)
    private int ID_acceleration;

    @DatabaseField(columnName = FIELD_NAME_TIMESTAMP)
    private Date timestamp;

    @DatabaseField(columnName = FIELD_NAME_FROM)
    private int from;

    @DatabaseField(columnName = FIELD_NAME_TO)
    private int to;

    @DatabaseField(columnName = FIELD_NAME_DATA)
    private double data;

    @DatabaseField(columnName = FIELD_NAME_VEHICLE, foreign = true, foreignAutoRefresh = true)
    private Vehicle vehicle_ID;

    public Acceleration(Date timestamp, int from, int to, double data, Vehicle vehicle)
    {
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
        this.data = data;
        this.vehicle_ID = vehicle;
    }

    public Acceleration(){}

    public int getID_acceleration() {
        return ID_acceleration;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public double getData() {
        return data;
    }

    public Vehicle getVehicle_ID() {
        return vehicle_ID;
    }
}
