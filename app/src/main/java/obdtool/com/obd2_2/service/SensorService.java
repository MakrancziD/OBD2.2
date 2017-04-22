package obdtool.com.obd2_2.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Date;

import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.db.Model.SensorEntry;
import obdtool.com.obd2_2.db.Model.Trip;

/**
 * Created by Maki on 2017. 04. 18..
 */

public class SensorService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor senAccelerometer;

    private long lastUpdate;
    private int updateInterval=100; //TODO: Shared Pref

    @Override
    public void onCreate() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                DbHandler.storeSensorResult(new SensorEntry(mySensor.getName(),new Date(), x, y, z, new Trip())); //TODO: trip
                lastUpdate = curTime;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void pauseSvc() {
        sensorManager.unregisterListener(this);
    }

    public void resumeSvc() {
        sensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
