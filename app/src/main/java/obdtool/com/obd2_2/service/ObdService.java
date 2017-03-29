package obdtool.com.obd2_2.service;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.ObdResetCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand;
import com.github.pires.obd.enums.ObdProtocols;

import java.io.IOException;

import obdtool.com.obd2_2.util.BluetoothManager;
import obdtool.com.obd2_2.util.Enums;
import obdtool.com.obd2_2.util.ObdCommandJob;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Created by Maki on 2017. 03. 22..
 */

public class ObdService extends GatewayService {

    BluetoothSocket btSocket = null;
    private static final String COMP = ObdService.class.getName();

    private boolean isRunning=false;
    private Context context;

    private final IBinder binder = new ObdServiceBinder();

    public void startService(BluetoothDevice btDevice) {
        Log.d(COMP, "Connecting to Bluetooth device...");
        try {
            if(btSocket!=null)
            {
                if(btSocket.getRemoteDevice().equals(btDevice))
                {
                    if(!btSocket.isConnected())
                    {
                        btSocket.connect();
                    }
                }
                else
                {
                    switchDevice(btDevice);
                }
            }
            else
            {
                btSocket = BluetoothManager.Connect(btDevice);
            }
        }
        catch (Exception e)
        {
            Log.e(COMP, "Connecting to Bluetooth device FAILED");
            stopService();
        }

        if(btSocket==null)
        {
            Log.e(COMP, "Getting Bluetooth socket FAILED");
            stopService();
        }

        initObd();

    }

    private void switchDevice(BluetoothDevice btDevice)
    {
        if(btSocket!=null)
        {
            if(btSocket.isConnected())
            {
                try {
                    btSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            btSocket=BluetoothManager.Connect(btDevice);
        }
    }

    private void initObd()
    {
        executeCommand(new ObdCommandJob(new ObdResetCommand()));

        try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }

        //addToQueue(new ObdCommandJob(new EchoOffCommand()));
        //addToQueue(new ObdCommandJob(new LineFeedOffCommand()));
        //addToQueue(new ObdCommandJob(new TimeoutCommand(62)));
        executeCommand(new ObdCommandJob(new SelectProtocolCommand(ObdProtocols.AUTO)));

        executeCommand(new ObdCommandJob(new EngineCoolantTemperatureCommand()));

        Log.d(COMP, "OBD initialization completed");
        isRunning = true;

    }



    protected void executeQueue()
    {
        Log.d(COMP, "Executing queue...");

        while(!Thread.currentThread().isInterrupted())
        {
            ObdCommandJob job = null;

            try {
                job = queue.take();
                job = executeCommand(job);
            }
            catch(Exception e)
            {
                Log.e(COMP, "Unable to run command!"+e.getMessage());
            }

            //store response, update UI
        }
    }

    public ObdCommandJob executeCommand(ObdCommandJob job)
    {
        Log.d(COMP, "Executing command");

        if(job.getStatus()==ObdCommandJob.ObdCommandJobStatus.NEW)
        {
            job.setStatus(ObdCommandJob.ObdCommandJobStatus.RUNNING);
            try {
                if (btSocket.isConnected()) {
                    btSocket.connect();
                }
                {
                    job.getCommand().run(btSocket.getInputStream(), btSocket.getOutputStream());
                }
            } catch (Exception e) {
                Log.e(COMP, "Unable to perform command");
            }
            return job;
        }
        Log.e(COMP, "Job is not NEW!");
        return null;
    }


    public void stopService() {
        Log.d(COMP, "Stopping service...");

        //notificationManager.cancel(NOTIFICATION_ID);
        queue.clear();
        //isRunning = false;

        if (btSocket != null) {
            try {
                btSocket.close();
            } catch (IOException e) {
                Log.e(COMP, e.getMessage());
            }
        }

        stopSelf();
        isRunning=false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public class ObdServiceBinder extends Binder {
        public ObdService getService()
        {
            return ObdService.this;
        }
    }
}
