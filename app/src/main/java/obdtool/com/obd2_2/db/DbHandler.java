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

import obdtool.com.obd2_2.db.Model.Acceleration;
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
    private static Dao<Acceleration, Integer> accelerationDao;

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
        if(getCurrentTrip() !=null)
        {
            endTrip();
        }
        Date d = new Date();
        if(getCurrentVehicle() ==null)
        {
            try {
                List<Vehicle> v = vehicleDao.queryBuilder().where().eq("name", "default").query();
                if(v.size()<0)
                {
                    setCurrentVehicle(v.get(0));
                }
                else
                {
                    int vehId = vehicleDao.create(new Vehicle("default", "a", "b"));
                    setCurrentVehicle(vehicleDao.queryForId(vehId));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            Trip t = new Trip(d, getCurrentVehicle());
            if(tripDao.create(t)==1)
            {
                currentTrip=t;
            }
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
    }

    public static void endTrip()
    {
        if(getCurrentTrip() !=null)
        {
            getCurrentTrip().setEnd_time(new Date());
            try {
                tripDao.update(getCurrentTrip());
            } catch (SQLException e) {
                Log.e(COMP, e.getMessage());
            }
            currentTrip = null;
        }
    }

    public static void storeObd(ObdCommand cmd)
    {
        if(getCurrentTrip() ==null) {
            startTrip();
        }
        try {
            obdDao.create(new ObdEntry(new Date(), cmd.getName(), cmd.getResult(), Double.parseDouble(cmd.getCalculatedResult()), getCurrentTrip()));
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

//    public static List<ObdEntry> getTripData(Trip t)
//    {
//        try {
//            return new ArrayList<ObdEntry>(tripDao.queryForId(t).getObdEntries());
//        } catch (SQLException e) {
//            Log.e(COMP, e.getMessage());
//        }
//        return null;
//    }

    public static void storeGpsEntry(Location entry)
    {
        SensorEntry sEntry = new SensorEntry("GPS", new Date(entry.getTime()), entry.getLatitude(), entry.getLongitude(), entry.getAltitude(), entry.getSpeed(), getCurrentTrip());
        try {
            sensorDao.create(sEntry);
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
    }

    public static void storeAccelResult(Acceleration acc)
    {
        try {
            accelerationDao.create(acc);
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
    }

    public static void storeSensorResult(String type, Date timestasmp, double x, double y, double z, double zz)
    {
        SensorEntry sEntry = new SensorEntry(type, timestasmp, x, y, z, zz, getCurrentTrip());
        try {
            sensorDao.create(sEntry);
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
    }

    public static List<Vehicle> getVehicles()
    {
        List<Vehicle> out = new ArrayList<>();
        try {
            out.addAll(vehicleDao.queryForAll());
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
        return out;
    }

    public static Trip getTrip(int tripID)
    {
        try {
            return tripDao.queryForId(tripID);
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
        return null;
    }

    public static Vehicle getCurrentVehicle() {
        return currentVehicle;
    }

    public static void setCurrentVehicle(Vehicle currentVehicle) {
        DbHandler.currentVehicle = currentVehicle;
    }

    public static void setCurrentVehicle(String vehId) {
        try {
            Vehicle v = vehicleDao.queryForId(Integer.parseInt(vehId));
            DbHandler.currentVehicle = v;
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
        int i = 1;
    }

    public static void addVehicle(Vehicle v) {
        try {
            vehicleDao.create(v);
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
    }

    public static void editVehicle(Vehicle v) {
        try {
            vehicleDao.update(v);
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
    }

    public static void deleteVehicle(Vehicle v) {
        try {
            vehicleDao.delete(v);
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
    }

    public static List<ObdEntry> getAllObdEntries() {
        try {
            return obdDao.queryForAll();
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
        return null;
    }
    public static List<SensorEntry> getAllSensorEntries() {
        try {
            return sensorDao.queryForAll();
        } catch (SQLException e) {
            Log.e(COMP, e.getMessage());
        }
        return null;
    }

    public static Trip getCurrentTrip() {
        return currentTrip;
    }
}
