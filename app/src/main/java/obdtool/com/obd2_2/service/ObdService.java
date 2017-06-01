package obdtool.com.obd2_2.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.commands.protocol.EchoOffCommand;
import com.github.pires.obd.commands.protocol.LineFeedOffCommand;
import com.github.pires.obd.commands.protocol.ObdResetCommand;
import com.github.pires.obd.commands.protocol.SelectProtocolCommand;
import com.github.pires.obd.commands.protocol.TimeoutCommand;
import com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand;
import com.github.pires.obd.enums.ObdProtocols;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.activity.MainActivity;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.util.BluetoothManager;
import obdtool.com.obd2_2.util.ObdCommandJob;

public class ObdService extends GatewayService {

    private BluetoothSocket btSocket = null;
    private static final String COMP = ObdService.class.getName();
    public static final int NOTIFICATION_ID = 1;

    private boolean isRunning=false;
    private boolean queuingEnabled = false;
    private Context context;

    private List<ObdCommand> liveCommands = new ArrayList<>();

    private final IBinder binder = new ObdServiceBinder();

    private Runnable queueCommands = new Runnable() {
        @Override
        public void run() {
            if(isQueuingEnabled() && isRunning && queue.size()==0)
            {
                for(ObdCommand cmd : liveCommands)
                {
                    addToQueue(new ObdCommandJob(cmd));
                }
            }
            new Handler().postDelayed(queueCommands, 100);
        }
    };

    public void startService(BluetoothDevice btDevice) {
        showNotification("Disconnected", "No bt device connected", R.drawable.ic_dashboard, true, true, true);
        Log.d(COMP, "Connecting to Bluetooth device...");
        try {
            if(getBtSocket() !=null)
            {
                if(getBtSocket().getRemoteDevice().equals(btDevice))
                {
                    if(!getBtSocket().isConnected())
                    {
                        getBtSocket().connect();
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

        if(getBtSocket() ==null)
        {
            Log.e(COMP, "Getting Bluetooth socket FAILED");
            stopService();
        }

        if(btSocket.isConnected()) {
            showNotification("Bluetooth device Connected", btDevice.getName(), R.drawable.ic_dashboard, true, true, true);
            initObd();
        }
    }

    private void switchDevice(BluetoothDevice btDevice)
    {
        if(getBtSocket() !=null)
        {
            if(getBtSocket().isConnected())
            {
                try {
                    getBtSocket().close();
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

        executeCommand(new ObdCommandJob(new EchoOffCommand()));
        executeCommand(new ObdCommandJob(new LineFeedOffCommand()));
        executeCommand(new ObdCommandJob(new TimeoutCommand(62)));
        executeCommand(new ObdCommandJob(new SelectProtocolCommand(ObdProtocols.AUTO)));

        executeCommand(new ObdCommandJob(new EngineCoolantTemperatureCommand()));

        queueCommands.run();

        Log.d(COMP, "OBD initialization completed");
        isRunning = true;
        showNotification("OBD Service running", "You can use the application", R.drawable.ic_dashboard, true, true, true);
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

            if (job != null) {
                final ObdCommandJob job2 = job;
                ((MainActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) context).updateLive(job2.getCommand());
                    }
                });
            }

            DbHandler.storeObd(job.getCommand());
        }
    }

    public ObdCommandJob executeCommand(ObdCommandJob job)
    {
        Log.d(COMP, "Executing command");

        if(job.getStatus()==ObdCommandJob.ObdCommandJobStatus.NEW)
        {
            job.setStatus(ObdCommandJob.ObdCommandJobStatus.RUNNING);
            try {
                if (!getBtSocket().isConnected()) {
                    getBtSocket().connect();
                }
                job.getCommand().run(getBtSocket().getInputStream(), getBtSocket().getOutputStream());
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

        if (getBtSocket() != null) {
            try {
                getBtSocket().close();
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

    public BluetoothSocket getBtSocket() {
        return btSocket;
    }

    public List<ObdCommand> getLiveCommands() {
        return liveCommands;
    }

    public void setLiveCommands(List<ObdCommand> liveCommands) {
        this.liveCommands = liveCommands;
    }

    public boolean isQueuingEnabled() {
        return queuingEnabled;
    }

    public void setQueuingEnabled(boolean queuingEnabled) {
        this.queuingEnabled = queuingEnabled;
    }

    public class ObdServiceBinder extends Binder {
        public ObdService getService()
        {
            return ObdService.this;
        }
    }

    protected void showNotification(String contentTitle, String contentText, int icon, boolean ongoing, boolean notify, boolean vibrate) {
        final PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setContentTitle(contentTitle)
                .setContentText(contentText).setSmallIcon(icon)
                .setContentIntent(contentIntent)
                .setWhen(System.currentTimeMillis());
        if (ongoing) {
            notificationBuilder.setOngoing(true);
        } else {
            notificationBuilder.setAutoCancel(true);
        }
        if (vibrate) {
            notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        }
        if (notify) {
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.getNotification());
        }
    }
}
