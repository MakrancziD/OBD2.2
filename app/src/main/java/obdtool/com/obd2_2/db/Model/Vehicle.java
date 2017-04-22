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

    static final String TABLE_NAME = "vehicle";
    private static final String FIELD_NAME_ID = "ID_vehicle";
    private static final String FIELD_NAME_NAME = "name";
    private static final String FIELD_NAME_MAKE = "make";
    private static final String FIELD_NAME_MODEL = "model";
    private static final String FIELD_NAME_TRIPS = "trips";

    public int getID_vehicle() {
        return ID_vehicle;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public String getMakeModel() {
        return getMake() +" "+ getModel();
    }

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int ID_vehicle;

    @DatabaseField(columnName = FIELD_NAME_NAME)
    private String name;

    @DatabaseField(columnName = FIELD_NAME_MAKE)
    private String make;

    @DatabaseField(columnName = FIELD_NAME_MODEL)
    private String model;

    @ForeignCollectionField(columnName = FIELD_NAME_TRIPS, eager=true)
    private ForeignCollection<Trip> trips;

    public ForeignCollection<Trip> getTrips()
    {
        return trips;
    }

    public Vehicle() {}

    public Vehicle(String name, String make, String model) {
        this.setName(name);
        this.setMake(make);
        this.setModel(model);
    }

    @Override
    public String toString()
    {
        return this.ID_vehicle+" - "+ this.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
