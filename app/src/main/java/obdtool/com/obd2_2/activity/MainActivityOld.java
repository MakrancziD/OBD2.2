package obdtool.com.obd2_2.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pires.obd.commands.ObdCommand;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import obdtool.com.obd2_2.Fragment.ConnectionFragment;
import obdtool.com.obd2_2.Fragment.DiagnosticFragment;
import obdtool.com.obd2_2.Fragment.LiveFragment;
import obdtool.com.obd2_2.Fragment.PreferencesFragment;
import obdtool.com.obd2_2.Fragment.TerminalFragment;
import obdtool.com.obd2_2.Fragment.TripDetailsFragment;
import obdtool.com.obd2_2.Fragment.TripFragment;
import obdtool.com.obd2_2.Fragment.VehicleInfoFragment;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.db.Model.Trip;
import obdtool.com.obd2_2.service.LocationService;
import obdtool.com.obd2_2.service.ObdService;
import obdtool.com.obd2_2.util.BluetoothManager;
import obdtool.com.obd2_2.util.CustomObdCommand;
import obdtool.com.obd2_2.util.Enum;
import obdtool.com.obd2_2.util.ObdCommandJob;
import obdtool.com.obd2_2.util.ReceiverFragment;

import static obdtool.com.obd2_2.util.BottomNavigationViewHelper.disableShiftMode;

public class MainActivityOld extends Activity implements TerminalFragment.OnFragmentInteractionListener,
        ConnectionFragment.OnFragmentInteractionListener,
        LiveFragment.OnFragmentInteractionListener,
        VehicleInfoFragment.OnFragmentInteractionListener,
        DiagnosticFragment.OnFragmentInteractionListener,
        TripFragment.OnListFragmentInteractionListener,
        PreferencesFragment.OnFragmentInteractionListener{

    Button btnBt;
    Button btnVeh;
    Button btnLive;
    Button btnTerminal;
    TextView connectionStatus;
    View contentView;
    BottomNavigationView bottomNavigationView;

    private static final String PREFS = "sharedPrefs";
    private static final String DEVICES = "btDevices";

    private static final int REQUEST_ENABLE_BT = 1111;
    private static final String COMP = MainActivityOld.class.getName();
    public Enum.connectionState state = Enum.connectionState.DISCONNECTED;
    private boolean isObdServiceBound;
    private boolean isLocationServiceBound;
    private ObdService obdService;
    private LocationService locationService;
    private BluetoothDevice btDevice=null;
    private ReceiverFragment currentFragment;
    private Context context;

    private SharedPreferences sharedPreferences;


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
        sharedPreferences = getSharedPreferences(PREFS, Context.MODE_PRIVATE);


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

        doBindObdService();
        DbHandler.initDb(context);

        CheckBluetoothAdapter();
        UpdateBtDevList();

    }

    private void UpdateBtDevList() {

        SharedPreferences.Editor ed;
        ed = sharedPreferences.edit();
        Gson gson = new Gson();
        Set<BluetoothDevice> devList = BluetoothManager.GetBtDevices();
        ed.putString(DEVICES, gson.toJson(devList));
        ed.apply();
    }

    public void displayView(int itemId) {

        switch (itemId) {
            case R.id.action_connection:
                currentFragment = new ConnectionFragment();
                break;
            case R.id.action_terminal:
                currentFragment = new TerminalFragment();
                break;
            case R.id.action_live:
                currentFragment = new LiveFragment();
                break;
//            case R.id.action_vehicle:
//                currentFragment = new VehicleInfoFragment();
//                break;
            case R.id.action_diag:
                currentFragment = new DiagnosticFragment();
                break;
//            case R.id.action_trip:
//                currentFragment = new TripFragment();
//                break;
            case R.id.action_prefs:
                currentFragment = new PreferencesFragment();
                break;
        }

        applyFragment();
    }

    private void displayTripDetails(int tripId) {
        currentFragment = TripDetailsFragment.newInstance(tripId);
        applyFragment();
    }

    private void applyFragment()
    {
        if(currentFragment!=null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.nav_content, (Fragment)currentFragment);
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

    public boolean SelectBtDevice() {
        //TODO: store default device and try to connect to it automatically on startup
        updateState(Enum.connectionState.BLUETOOTH_ON);

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
                obdService.startService(pairedDevices.get(position));
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

    private void doBindObdService() {
        if (!isObdServiceBound) {
            Log.d(COMP, "Binding OBD obdService..");
            updateState(Enum.connectionState.INIT_OBD);
            Intent serviceIntent = new Intent(this, ObdService.class);
            if(bindService(serviceIntent, obdServiceConn, Context.BIND_AUTO_CREATE))
            {
                Log.d(COMP, "bound true");
            }
            else {Log.d(COMP, "bound false");}
        }
    }

    private void doBindLocationService() {
        if(!isLocationServiceBound) {
            Log.d(COMP, "Binding location service..");
            Intent locationServiceIntent = new Intent(this, LocationService.class);
            if(bindService(locationServiceIntent, locationServiceConnection, Context.BIND_AUTO_CREATE))
            {
                Log.d(COMP, "location service bound true");
            }
            else {Log.d(COMP, "location service bound false");}
        }
    }

    private ServiceConnection obdServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d(COMP, className.toString() + " obdService is bound");
            isObdServiceBound = true;
            obdService = ((ObdService.ObdServiceBinder) binder).getService();
            obdService.setContext(MainActivityOld.this);
            displayView(R.id.action_connection);
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(COMP, className.toString() + " obdService is unbound");
            isObdServiceBound = false;
        }
    };

    private ServiceConnection locationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(COMP, name.toString()+ " locationService is bound");
            isLocationServiceBound = true;
            locationService = ((LocationService.LocationServiceBinder) service).getService();
            locationService.setContext(MainActivityOld.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(COMP, name.toString() + " locationService is unbound");
            isLocationServiceBound = false;
        }
    };

    private void doUnbindObdService() {
        if (isObdServiceBound) {
            if (obdService.isRunning()) {
                obdService.stopService();
            }
            Log.d(COMP, "Unbinding OBD obdService..");
            unbindService(obdServiceConn);
            isObdServiceBound = false;
        }
    }

    private void doUnbindLocationService() {
        if (isLocationServiceBound) {
            if (locationService.isRunning()) {
                locationService.stopService();
            }
            Log.d(COMP, "Unbinding location Service..");
            unbindService(locationServiceConnection);
            isLocationServiceBound = false;
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
//            loadingDialog = ProgressDialog.show(MainActivityOld.this, "Connecting to device", "Please wait...", true);
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
        job = obdService.executeCommand(job);
        return job.getCommand().getResult();
    }

    public ObdCommand ObdCommand(ObdCommand command)
    {
        ObdCommandJob job = new ObdCommandJob(command);
        job = obdService.executeCommand(job);
        return job.getCommand();
    }

    public String ObdRawCommand(String command)
    {
        BluetoothSocket socket = obdService.getBtSocket();
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

    public void enableQueue(boolean q)
    {
        obdService.setQueuingEnabled(q);
    }

    public void initLiveCommands(List<ObdCommand> cmdList)
    {
        obdService.setLiveCommands(cmdList);
    }

    public void updateLive(ObdCommand cmd)
    {
        currentFragment.update(cmd);
    }

    public void updateState(Enum.connectionState _state)
    {
        state=_state;
    }

    private void ShowToast(String msg)
    {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Trip item) {

    }
}
