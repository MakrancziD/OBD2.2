package obdtool.com.obd2_2.db.Model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * Created by Maki on 2017. 03. 27..
 */

@DatabaseTable(tableName = Vehicle.TABLE_NAME)
public class Vehicle {

    public static final String TABLE_NAME = "vehicle";
    public static final String FIELD_NAME_ID = "ID_vehicle";
    public static final String FIELD_NAME_NAME = "name";
    public static final String FIELD_NAME_TRIPS = "trips";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int ID_vehicle;

    @DatabaseField(columnName = FIELD_NAME_NAME)
    private String name;

    @ForeignCollectionField(columnName = FIELD_NAME_TRIPS, eager=true)
    private ForeignCollection<Trip> trips;

    public ForeignCollection<Trip> getTrips()
    {
        return trips;
    }

    public Vehicle() {}

    public Vehicle(String name) {
        this.name=name;
    }

    @Override
    public String toString()
    {
        return this.ID_vehicle+" - "+this.name;
    }
}
