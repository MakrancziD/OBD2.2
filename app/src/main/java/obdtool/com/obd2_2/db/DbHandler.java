package obdtool.com.obd2_2.db;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.github.pires.obd.commands.ObdCommand;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import obdtool.com.obd2_2.db.Model.ObdEntry;
import obdtool.com.obd2_2.db.Model.SensorEntry;
import obdtool.com.obd2_2.db.Model.Trip;
import obdtool.com.obd2_2.db.Model.Vehicle;

/**
 * Created by Maki on 2017. 04. 09..
 */

public class DbHandler {

    private static Dao<Vehicle, Integer> vehicleDao;
    private static Dao<Trip, Integer> tripDao;
    private static Dao<ObdEntry, Integer> obdDao;
    private static Dao<SensorEntry, Integer> sensorDao;

    private static Vehicle currentVehicle;
    private static Trip currentTrip;

    private static final String COMP = DbHandler.class.getName();

    public static  void initDb(Context context)
    {
        DbHelper db = new DbHelper(context);
        try {
            vehicleDao=db.getDao(Vehicle.class);
            tripDao = db.getDao(Trip.class);
            obdDao = db.getDao(ObdEntry.class);
            sensorDao = db.getDao(SensorEntry.class);


//            vehicleDao.create(new Vehicle("Batmobile"));
//            Vehicle myVehicle = vehicleDao.queryForAll().get(0);
//
//            Date date = new Date();
//            tripDao.create(new Trip(date, myVehicle));
//            t=tripDao.queryForAll().get(0);

        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
    }

    public static void startTrip()
    {
        if(currentTrip!=null)
        {
            endTrip();
        }
        Date d = new Date();
        if(currentVehicle==null)
        {
            try {
                List<Vehicle> v = vehicleDao.queryBuilder().where().eq("name", "default").query();
                if(v.size()<0)
                {
                    currentVehicle = v.get(0);
                }
                else
                {
                    int vehId = vehicleDao.create(new Vehicle("default"));
                    currentVehicle = vehicleDao.queryForId(vehId);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            int tripID = tripDao.create(new Trip(d, currentVehicle));
            currentTrip = tripDao.queryForId(tripID);
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
    }

    public static void endTrip()
    {
        currentTrip.setEnd_time(new Date());
        try {
            tripDao.update(currentTrip);
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
        currentTrip = null;
    }

    public static void storeObd(ObdCommand cmd)
    {
        if(currentTrip==null) {
            startTrip();
        }
        try {
            obdDao.create(new ObdEntry(new Date(), cmd.getName(), cmd.getResult(), cmd.getCalculatedResult(), currentTrip));
            //TODO: batch store every minute?
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
    }

    public static List<Trip> getTrips()
    {
        try {
            return tripDao.queryForAll();
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
        return null;
    }

    public static List<ObdEntry> getTripData(int t)
    {
        try {
            return new ArrayList<ObdEntry>(tripDao.queryForId(t).getObdEntries());
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
        return null;
    }

    public static void storeGpsEntry(Location entry)
    {
        SensorEntry sEntry = new SensorEntry();
        try {
            sensorDao.create(entry);
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
    }
}
