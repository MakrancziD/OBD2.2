package obdtool.com.obd2_2.db.Model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by Maki on 2017. 03. 27..
 */

@DatabaseTable(tableName = Trip.TABLE_NAME)
public class Trip {

    public static final String TABLE_NAME = "trip";

    public static final String FIELD_NAME_ID = "ID_trip";
    public static final String FIELD_NAME_START = "start_time";
    public static final String FIELD_NAME_END = "end_time";
    public static final String FIELD_NAME_VEHICLE = "vehicle_ID";
    public static final String FIELD_NAME_OBD_ENTRIES = "obd_entries";
    public static final String FIELD_NAME_SENSOR_ENTRIES = "sensor_entries";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int ID_trip;

    @DatabaseField(columnName = FIELD_NAME_START)
    private Date start_time;

    @DatabaseField(columnName = FIELD_NAME_END)
    private Date end_time;

    @DatabaseField(columnName = FIELD_NAME_VEHICLE, foreign = true, foreignAutoRefresh = true)
    private Vehicle vehicle_ID;

    @ForeignCollectionField(columnName = FIELD_NAME_OBD_ENTRIES, eager=true)
    private ForeignCollection<ObdEntry> obd_entries;

    public ForeignCollection<ObdEntry> getObdEntries()
    {
        return obd_entries;
    }

    @ForeignCollectionField(columnName = FIELD_NAME_SENSOR_ENTRIES, eager=true)
    private ForeignCollection<SensorEntry> sensor_entries;

    public ForeignCollection<SensorEntry> getSensorEntries()
    {
        return sensor_entries;
    }

    public Trip() {}

    public Trip(Date startTime, Vehicle veh_id)
    {
        this.start_time=startTime;
        this.vehicle_ID=veh_id;
    }

    @Override
    public String toString()
    {
        return this.ID_trip+" - "+this.start_time+" - "+this.vehicle_ID;
    }
}
