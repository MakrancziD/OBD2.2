package obdtool.com.obd2_2.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pires.obd.commands.ObdCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import obdtool.com.obd2_2.Fragment.ConnectionFragment;
import obdtool.com.obd2_2.Fragment.TerminalFragment;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.service.GatewayService;
import obdtool.com.obd2_2.service.ObdService;
import obdtool.com.obd2_2.util.BluetoothManager;
import obdtool.com.obd2_2.util.CustomObdCommand;
import obdtool.com.obd2_2.util.Enums;
import obdtool.com.obd2_2.util.ObdCommandJob;

import static obdtool.com.obd2_2.util.BottomNavigationViewHelper.disableShiftMode;

public class MainActivity extends AppCompatActivity implements TerminalFragment.OnFragmentInteractionListener, ConnectionFragment.OnFragmentInteractionListener {

    Button btnBt;
    Button btnVeh;
    Button btnLive;
    Button btnTerminal;
    TextView connectionStatus;
    View contentView;
    BottomNavigationView bottomNavigationView;

    private static final int REQUEST_ENABLE_BT = 1111;
    private static final String COMP = MainActivity.class.getName();
    public Enums.connectionState state = Enums.connectionState.DISCONNECTED;
    private boolean isServiceBound;
    private ObdService service;
    private BluetoothDevice btDevice=null;

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentView=findViewById(R.id.nav_content);
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                displayView(item.getItemId());
                return true;
            }
        });

        context = getApplicationContext();



//        btnLive = (Button)findViewById(R.id.btnLive);
//
//        btnLive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(context, DashboardActivity.class);
//                startActivity(i);
//            }
//        });
//
//        btnTerminal=(Button)findViewById(R.id.btnTerminal);
//        btnTerminal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment terminalFragment = new TerminalFragment();
//                if(terminalFragment!=null)
//                {
//                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                    ft.replace(R.id.activity_main, terminalFragment);
//                }
//            }
//        });

        doBindService();

//        DbHelper db = new DbHelper(this);
//        Dao<Vehicle, Integer> vehicleDao = null;
//        Dao<Trip, Integer> tripDao = null;
//        Trip t = null;
//        try {
//            vehicleDao=db.getDao(Vehicle.class);
//            vehicleDao.create(new Vehicle("Batmobile"));
//            Vehicle myVehicle = vehicleDao.queryForAll().get(0);
//            tripDao = db.getDao(Trip.class);
//            Date date = new Date();
//            tripDao.create(new Trip(date, myVehicle));
//            t=tripDao.queryForAll().get(0);
//
//        } catch (SQLException e) {
//            Log.e(COMP, e.getMessage());
//        }
//
//        tv=(TextView)findViewById(R.id.textView);
//        tv.setText(t.toString());


    }

    private void displayView(int itemId) {
        Fragment fragment = null;

        switch (itemId) {
            case R.id.action_connection:
                fragment = new ConnectionFragment();
                break;
            case R.id.action_terminal:
                fragment = new TerminalFragment();
        }

        if(fragment!=null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_content, fragment);
            ft.commit();
        }
    }


    public void CheckBluetoothAdapter()
    {
        BluetoothAdapter btAdapter=BluetoothManager.getBtAdapter();

        if(btAdapter!=null)
        {
            if(!btAdapter.isEnabled()) {
                Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnOn, REQUEST_ENABLE_BT);
            }
            else { SelectBtDevice();}
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode==REQUEST_ENABLE_BT)
        {
            if(resultCode==RESULT_OK)
            {
                SelectBtDevice();
            }
        }
    }

    private boolean SelectBtDevice() {
        //TODO: store default device and try to connect to it automatically on startup
        updateState(Enums.connectionState.BLUETOOTH_ON);

        List<String> deviceInfo = new ArrayList<>();

        final List<BluetoothDevice> pairedDevices = new ArrayList<>();
        pairedDevices.addAll(BluetoothManager.GetBtDevices());

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                deviceInfo.add(device.getName() + " - " + device.getAddress());
            }
        }

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.select_dialog_singlechoice,
                deviceInfo.toArray(new String[deviceInfo.size()]));

        alertDialog.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int position = ((AlertDialog) dialog).getListView().getCheckedItemPosition(); //isn't it the which parameter??
                service.startService(pairedDevices.get(position));
                dialog.dismiss();

            }
        }).setNeutralButton("Discover devices", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        BluetoothManager.btDiscovery(true);

                        final BroadcastReceiver bReciever = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                String action = intent.getAction();
                                if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                                    pairedDevices.add(device);
                                }
                            }
                        };
                    }
                }
        );

        alertDialog.show();

        return true;
    }

    private void doBindService() {
        if (!isServiceBound) {
            Log.d(COMP, "Binding OBD service..");
            updateState(Enums.connectionState.INIT_OBD);
            Intent serviceIntent = new Intent(this, ObdService.class);
            if(bindService(serviceIntent, serviceConn, Context.BIND_AUTO_CREATE))
            {
                Log.d(COMP, "bound true");
            }
            else {Log.d(COMP, "bound false");}
        }
    }

    private ServiceConnection serviceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d(COMP, className.toString() + " service is bound");
            isServiceBound = true;
            service = ((ObdService.ObdServiceBinder) binder).getService();
            service.setContext(MainActivity.this);
            //btnBt.setEnabled(true);
            displayView(R.id.action_connection);
//            Log.d(COMP, "Starting live data");
//            try {
//                service.startService();
//            } catch (IOException ioe) {
//                Log.e(COMP, "Failure Starting live data");
//                updateState(Enums.connectionState.BT_ERROR);
//                doUnbindService();
//            }
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(COMP, className.toString() + " service is unbound");
            isServiceBound = false;
        }
    };

    private void doUnbindService() {
        if (isServiceBound) {
            if (service.isRunning()) {
                service.stopService();
            }
            Log.d(COMP, "Unbinding OBD service..");
            unbindService(serviceConn);
            isServiceBound = false;
        }
    }



//    private class LoadBtSocketAsync extends AsyncTask<String, String, String>
//    {
//        private BluetoothDevice bt;
//        private ProgressDialog loadingDialog;
//
//        public LoadBtSocketAsync(BluetoothDevice bt)
//        {
//            this.bt=bt;
//        }
//
//        @Override
//        protected void onPreExecute()
//        {
//            loadingDialog = ProgressDialog.show(MainActivity.this, "Connecting to device", "Please wait...", true);
//            loadingDialog.setCancelable(true);
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            BluetoothSocket tmpSocket =  BluetoothManager.Connect(bt);
//            if(tmpSocket!=null)
//            {
//                btSocket=tmpSocket;
//            }
//            return tmpSocket==null?"REKT":"OK";
//        }
//
//        @Override
//        protected void onPostExecute(String s)
//        {
//            loadingDialog.dismiss();
//            ShowToast(s.equals("OK")?"Connected":"REKT");
//        }
//    }

    public String ObdCommand(String command)
    {
        ObdCommandJob job = new ObdCommandJob(new CustomObdCommand(command));
        job = service.executeCommand(job);
        return job.getCommand().getResult();
    }

    public String ObdRawCommand(String command)
    {
        BluetoothSocket socket = service.getBtSocket();
        ObdCommand job = new CustomObdCommand(command);
        try {
            job.run(socket.getInputStream(), socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return job.getResult();
    }

    public void updateState(Enums.connectionState _state)
    {
        state=_state;
        //connectionStatus.setText(state.toString());
    }

    private void ShowToast(String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {


    }
}
