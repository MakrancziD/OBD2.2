package obdtool.com.obd2_2.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.SensorEvent;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.github.pires.obd.commands.ObdCommand;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import obdtool.com.obd2_2.Fragment.AccelerationFragment;
import obdtool.com.obd2_2.Fragment.ConnectionFragment;
import obdtool.com.obd2_2.Fragment.DiagnosticFragment;
import obdtool.com.obd2_2.Fragment.LiveFragment;
import obdtool.com.obd2_2.Fragment.PreferencesFragment;
import obdtool.com.obd2_2.Fragment.TerminalFragment;
import obdtool.com.obd2_2.Fragment.TripDetailsFragment;
import obdtool.com.obd2_2.Fragment.TripFragment;
import obdtool.com.obd2_2.Fragment.VehicleInfoFragment;
import obdtool.com.obd2_2.Fragment.VehicleListFragment;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.db.Model.Acceleration;
import obdtool.com.obd2_2.db.Model.Trip;
import obdtool.com.obd2_2.db.Model.Vehicle;
import obdtool.com.obd2_2.service.LocationService;
import obdtool.com.obd2_2.service.ObdService;
import obdtool.com.obd2_2.service.SensorService;
import obdtool.com.obd2_2.util.BluetoothManager;
import obdtool.com.obd2_2.util.CustomObdCommand;
import obdtool.com.obd2_2.util.ObdCommandJob;
import obdtool.com.obd2_2.util.ReceiverFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        TerminalFragment.OnFragmentInteractionListener,
        ConnectionFragment.OnFragmentInteractionListener,
        LiveFragment.OnFragmentInteractionListener,
        VehicleInfoFragment.OnFragmentInteractionListener,
        DiagnosticFragment.OnFragmentInteractionListener,
        TripFragment.OnListFragmentInteractionListener,
        PreferencesFragment.OnFragmentInteractionListener,
        AccelerationFragment.OnListFragmentInteractionListener,
        TripDetailsFragment.OnFragmentInteractionListener,
        VehicleListFragment.OnListFragmentInteractionListener{

    private Context context;
    private boolean isObdServiceBound;
    private boolean isLocationServiceBound;
    private boolean isSensorServiceBound;
    private static final String COMP = MainActivity.class.getName();
    private ObdService obdService;
    private LocationService locationService;
    private SensorService sensorService;
    private static final int REQUEST_ENABLE_BT = 1111;
    private static final int PERMISSION_REQUEST_LOCATION = 22;

    private static final String DEVICES = "btDevices";
    private SharedPreferences sharedPreferences;
    private ReceiverFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        context = getApplicationContext();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        DbHandler.initDb(context);
        DbHandler.setCurrentVehicle(sharedPreferences.getString("list_preference_veh", "1"));
        UpdateBtDevList();
        doBindObdService();
        doBindLocationService();
        doBindSensorService();
        CheckBluetoothAdapter();

    }

    private void TryConnectDefaultBtDevice() {
//        String btJson = sharedPreferences.getString("list_preference_bt","");
//        BluetoothDevice defaultDevice = gson.fromJson(btJson, BluetoothDevice.class);
//        if (btJson.equals("") /*|| !obdService.startService(defaultDevice)*/) {
//            SelectBtDevice();
//        } else {
//            obdService.startService(defaultDevice);
//        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_connection:
                currentFragment = new ConnectionFragment();
                break;
            case R.id.menu_vehicle:
                currentFragment = new VehicleListFragment();
                break;
            case R.id.menu_accel:
                currentFragment = new AccelerationFragment();
                break;
            case R.id.menu_terminal:
                currentFragment = new TerminalFragment();
                break;
            case R.id.menu_dashboard:
                currentFragment = new LiveFragment();
                break;
            case R.id.menu_diag:
                currentFragment = new DiagnosticFragment();
                break;
            case R.id.menu_trips:
                currentFragment = new TripFragment();
                break;
            case R.id.menu_pref:
                currentFragment = new PreferencesFragment();
                break;
        }

        applyFragment();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void applyFragment()
    {
        if(currentFragment!=null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, (Fragment)currentFragment);
            ft.commit();
        }
    }

    private void doBindObdService() {
        if (!isObdServiceBound()) {
            Log.d(COMP, "Binding OBD obdService..");
            Intent serviceIntent = new Intent(this, ObdService.class);
            if(bindService(serviceIntent, obdServiceConn, Context.BIND_AUTO_CREATE))
            {
                Log.d(COMP, "bound true");
            }
            else {Log.d(COMP, "bound false");}
        }
    }

    private void doBindLocationService() {
        if(!isLocationServiceBound()) {
            Log.d(COMP, "Binding location service..");
            Intent locationServiceIntent = new Intent(this, LocationService.class);
            if(bindService(locationServiceIntent, locationServiceConnection, Context.BIND_AUTO_CREATE))
            {
                Log.d(COMP, "location service bound true");
            }
            else {Log.d(COMP, "location service bound false");}
        }
    }

    private void doBindSensorService() {
        if(!isSensorServiceBound()) {
            Log.d(COMP, "Binding sensor service..");
            Intent sensorServiceIntent = new Intent(this, SensorService.class);
            if(bindService(sensorServiceIntent, sensorServiceConnection, Context.BIND_AUTO_CREATE))
            {
                Log.d(COMP, "location service bound true");
            }
            else {Log.d(COMP, "location service bound false");}
        }
    }

    private void doUnbindObdService() {
        if (isObdServiceBound()) {
            if (obdService.isRunning()) {
                obdService.stopService();
            }
            Log.d(COMP, "Unbinding OBD obdService..");
            unbindService(obdServiceConn);
            isObdServiceBound = false;
        }
    }

    private void doUnbindLocationService() {
        if (isLocationServiceBound()) {
            if (locationService.isRunning()) {
                locationService.stopService();
            }
            Log.d(COMP, "Unbinding location Service..");
            unbindService(locationServiceConnection);
            isLocationServiceBound = false;
        }
    }

    private ServiceConnection obdServiceConn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d(COMP, className.toString() + " obdService is bound");
            isObdServiceBound = true;
            obdService = ((ObdService.ObdServiceBinder) binder).getService();
            obdService.setContext(MainActivity.this);
            TryConnectDefaultBtDevice();
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
            locationService.setContext(MainActivity.this);
            checkLocationPermission();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(COMP, name.toString() + " locationService is unbound");
            isLocationServiceBound = false;
        }
    };

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_LOCATION);
        }
    }

    public Location getLocation() {
            return locationService.getLocation();
    }

    private ServiceConnection sensorServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(COMP, name.toString()+ " sensorService is bound");
            isSensorServiceBound = true;
            sensorService = ((SensorService.SensorServiceBinder) service).getService();
            sensorService.setContext(MainActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(COMP, name.toString() + " locationService is unbound");
            isLocationServiceBound = false;
        }
    };

    public void CheckBluetoothAdapter()
    {
        BluetoothAdapter btAdapter= BluetoothManager.getBtAdapter();

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

        List<String> deviceInfo = new ArrayList<>();

        final List<BluetoothDevice> pairedDevices = new ArrayList<>();
        Set<BluetoothDevice> devices = BluetoothManager.GetBtDevices();
        if(devices!=null) {
            pairedDevices.addAll(devices);
        }

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

    private void UpdateBtDevList() {

        SharedPreferences.Editor ed;
        ed = sharedPreferences.edit();
        Gson gson = new Gson();
        Set<BluetoothDevice> devList = BluetoothManager.GetBtDevices();
        ed.putString(DEVICES, gson.toJson(devList));
        ed.apply();
    }

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

    public void enableQueue(boolean q)
    {
        obdService.setQueuingEnabled(q);
    }

    public void initLiveCommands(List<ObdCommand> cmdList)
    {
        obdService.setLiveCommands(cmdList);
    }

    public void enableLocation(boolean b) {
        locationService.enableLocation(b);
    }

    public void enableSensor(boolean b) {
        sensorService.enableSensors(b);
    }

    public void updateLive(ObdCommand cmd)
    {
        currentFragment.update(cmd);
    }

    public void updateLive(Location cmd)
    {
        currentFragment.update(cmd);
    }

    public void updateLive(SensorEvent cmd)
    {
        currentFragment.update(cmd);
    }

    @Override
    public void onListFragmentInteraction(Trip item) {
        currentFragment = TripDetailsFragment.newInstance(item.getID_trip());
        applyFragment();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Vehicle item) {

    }

    public boolean isObdServiceBound() {
        return isObdServiceBound;
    }

    public boolean isLocationServiceBound() {
        return isLocationServiceBound;
    }

    public boolean isSensorServiceBound() {
        return isSensorServiceBound;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isLocationServiceBound = true;
                }
            }
        }
    }

    @Override
    public void onListFragmentInteraction(Acceleration item) {

    }
}
