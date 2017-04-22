package obdtool.com.obd2_2.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import obdtool.com.obd2_2.util.ObdCommandJob;

/**
 * Created by Maki on 2017. 03. 25..
 */

public abstract class GatewayService extends Service {

    private final IBinder binder = new ObdService.GatewayServiceBinder();
    protected NotificationManager notificationManager;
    private static final String COMP = GatewayService.class.getName();

    protected BlockingQueue<ObdCommandJob> queue = new LinkedBlockingQueue<>();

    Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                executeQueue();
            } catch (InterruptedException e) {
                t.interrupt();
            }
        }
    });

    @Override
    public void onCreate() {
        super.onCreate();
        t.start();
        Log.d(COMP, "Service created.");
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //notificationManager.cancel(NOTIFICATION_ID);
        t.interrupt();
        Log.d(COMP, "Service destroyed.");
    }

    public void addToQueue(ObdCommandJob job)
    {
        try {
            queue.put(job);
        }
        catch (InterruptedException e)
        {
            job.setStatus(ObdCommandJob.ObdCommandJobStatus.ERROR);
            Log.e(COMP, "Job queuing failed");
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class GatewayServiceBinder extends Binder {
        public GatewayService getService()
        {
            return GatewayService.this;
        }
    }

    abstract protected void executeQueue() throws InterruptedException;
}
