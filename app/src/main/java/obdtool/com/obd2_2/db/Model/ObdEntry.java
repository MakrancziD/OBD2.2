package obdtool.com.obd2_2.db.Model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable(tableName = ObdEntry.TABLE_NAME)
public class ObdEntry {

    static final String TABLE_NAME = "obd_entry";

    private static final String FIELD_NAME_ID = "ID_obd_entry";
    private static final String FIELD_NAME_TIMESTAMP = "timestamp";
    private static final String FIELD_NAME_REQ = "request";
    private static final String FIELD_NAME_RESPONSE = "response";
    private static final String FIELD_NAME_FORMATTED_DATA = "formatted_data";
    private static final String FIELD_NAME_TRIP_ID = "trip_ID";

    @DatabaseField(columnName = FIELD_NAME_ID, generatedId = true)
    private int ID_obd_entry;

    @DatabaseField(columnName = FIELD_NAME_TIMESTAMP)
    private Date timestamp;

    @DatabaseField(columnName = FIELD_NAME_REQ)
    private String request;

    @DatabaseField(columnName = FIELD_NAME_RESPONSE)
    private String response;

    @DatabaseField(columnName = FIELD_NAME_FORMATTED_DATA)
    private double formatted_data;

    @DatabaseField(columnName = FIELD_NAME_TRIP_ID, foreign = true, foreignAutoRefresh = true)
    private Trip trip_ID;

    public ObdEntry() {}

    public ObdEntry(Date timestamp, String req, String resp, double calcData, Trip t)
    {
        this.timestamp = timestamp;
        this.request = req;
        this.response = resp;
        this.formatted_data = calcData;
        this.trip_ID = t;
    }

    public String getRequest() {
        return request;
    }

    public double getFormatted_data() {
        return formatted_data;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
