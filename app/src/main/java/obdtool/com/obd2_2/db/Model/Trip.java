package obdtool.com.obd2_2.db.Model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = Trip.TABLE_NAME)
public class Trip {

    static final String TABLE_NAME = "trip";

    private static final String FIELD_NAME_ID = "ID_trip";
    private static final String FIELD_NAME_START = "start_time";
    private static final String FIELD_NAME_END = "end_time";
    private static final String FIELD_NAME_VEHICLE = "vehicle_ID";
    private static final String FIELD_NAME_OBD_ENTRIES = "obd_entries";
    private static final String FIELD_NAME_SENSOR_ENTRIES = "sensor_entries";

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
        return getObd_entries();
    }

    @ForeignCollectionField(columnName = FIELD_NAME_SENSOR_ENTRIES, eager=true)
    private ForeignCollection<SensorEntry> sensor_entries;

    public ForeignCollection<SensorEntry> getSensorEntries()
    {
        return getSensor_entries();
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
        return this.getID_trip() +" - "+ this.getStart_time() +" - "+ this.getVehicle_ID();
    }

    public int getID_trip() {
        return ID_trip;
    }

    public Date getStart_time() {
        return start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public Vehicle getVehicle_ID() {
        return vehicle_ID;
    }

    public ForeignCollection<ObdEntry> getObd_entries() {
        return obd_entries;
    }

    public ForeignCollection<SensorEntry> getSensor_entries() {
        return sensor_entries;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }
}
