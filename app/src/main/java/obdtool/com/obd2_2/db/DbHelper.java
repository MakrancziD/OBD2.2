package obdtool.com.obd2_2.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import obdtool.com.obd2_2.db.Model.Acceleration;
import obdtool.com.obd2_2.db.Model.ObdEntry;
import obdtool.com.obd2_2.db.Model.SensorEntry;
import obdtool.com.obd2_2.db.Model.Trip;
import obdtool.com.obd2_2.db.Model.Vehicle;

public class DbHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME    = "ormlite.db";
    private static final int    DATABASE_VERSION = 6;

    DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, Vehicle.class);
            TableUtils.createTable(connectionSource, Trip.class);
            TableUtils.createTable(connectionSource, ObdEntry.class);
            TableUtils.createTable(connectionSource, SensorEntry.class);
            TableUtils.createTable(connectionSource, Acceleration.class);
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {
            TableUtils.dropTable(connectionSource, Vehicle.class, true);
            TableUtils.dropTable(connectionSource, Trip.class, true);
            TableUtils.dropTable(connectionSource, ObdEntry.class, true);
            TableUtils.dropTable(connectionSource, SensorEntry.class, true);
            TableUtils.dropTable(connectionSource, Acceleration.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
