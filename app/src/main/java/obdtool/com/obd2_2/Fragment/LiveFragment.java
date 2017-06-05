package obdtool.com.obd2_2.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.hardware.SensorEvent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.pires.obd.commands.ObdCommand;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Commands.PID.EngineCoolantTemperatureCommand;
import Commands.PID.PIDSupport;
import Commands.PID.RPMCommand;
import Commands.PID.SpeedCommand;
import Enums.PIDsupport;
import obdtool.com.obd2_2.Adapter.DataTypeSpinnerAdapter;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.activity.MainActivity;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.util.DataTypeItem;
import obdtool.com.obd2_2.util.ReceiverFragment;

public class LiveFragment extends Fragment implements ReceiverFragment, OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;

    private Spinner spnDataType;
    private TextView tvRPM;
    private TextView tvSpeed;
    private TextView tvCoolant;
    private Button btnStartStop;
    private TextView tvAccX;
    private TextView tvAccY;
    private TextView tvAccZ;
    private TextView tvGpsLat;
    private TextView tvGpsLon;
    private TextView tvGpsAlt;
    private TextView tvGpsSpd;

    private MapView mapView;
    private PolylineOptions mapLine;
    private GoogleMap gMap;

    DataTypeSpinnerAdapter spinnerAdapter;

    private boolean liveRecording = false;

    List<ObdCommand> cmdList = new ArrayList<>();

    Map<String, ObdCommand> supportedObdList = new HashMap<>();

    private MainActivity parentActivity;

    public LiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();

        cmdList.add(new SpeedCommand());
        cmdList.add(new RPMCommand());
        cmdList.add(new EngineCoolantTemperatureCommand());
        //TODO: dynamic command loading!
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);

        spnDataType = (Spinner) view.findViewById(R.id.spn_dataType);

        new loadSpinnerAsync().execute();

        tvRPM = (TextView) view.findViewById(R.id.value_RPM);
        tvSpeed = (TextView) view.findViewById(R.id.value_Speed);
        tvCoolant = (TextView) view.findViewById(R.id.value_coolant);

        tvAccX = (TextView) view.findViewById(R.id.value_acc_x);
        tvAccY = (TextView) view.findViewById(R.id.value_acc_y);
        tvAccZ = (TextView) view.findViewById(R.id.value_acc_z);

        tvGpsLat = (TextView) view.findViewById(R.id.value_gps_lat);
        tvGpsLon = (TextView) view.findViewById(R.id.value_gps_lon);
        tvGpsAlt = (TextView) view.findViewById(R.id.value_gps_alt);
        tvGpsSpd = (TextView) view.findViewById(R.id.value_gps_spd);

        btnStartStop = (Button) view.findViewById(R.id.btnStartStop);
        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liveRecording) {
                    stopRecording();
                } else {
                    startRecording();
                }
            }
        });

        mapView = (MapView) view.findViewById(R.id.tripMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    private void populateSpinner() {
        if (spnDataType != null) {
            if (parentActivity != null) {
                if (parentActivity.isObdServiceBound()) {
                    PIDSupport support = new PIDSupport(PIDsupport.PID_01_20);
                    for (ObdCommand cmd : support.getSupportedCommands()) {
                        supportedObdList.put(cmd.getName(), cmd);
                    }
                }
//                if(parentActivity.isSensorServiceBound()) {
//
//                }
                ArrayList<DataTypeItem> listSpinner = new ArrayList<>();
                for (Map.Entry<String, ObdCommand> e : supportedObdList.entrySet()) {
                    DataTypeItem item = new DataTypeItem(e.getKey());
                    listSpinner.add(item);
                }

                listSpinner.add(new DataTypeItem("GPS"));
                listSpinner.add(new DataTypeItem("Accelerometer"));

                spinnerAdapter = new DataTypeSpinnerAdapter(parentActivity, 0, listSpinner);
                spinnerAdapter.setDropDownViewResource(R.layout.live_spinner_item);
                spnDataType.setAdapter(spinnerAdapter);
            }
        }

    }

    private void startRecording() {

        parentActivity.initLiveCommands(cmdList);
        DbHandler.startTrip();
        parentActivity.enableQueue(true);
        parentActivity.enableLocation(true);
        parentActivity.enableSensor(true);
        this.liveRecording = true;
    }

    private void stopRecording() {
        parentActivity.enableQueue(false);
        parentActivity.enableLocation(false);
        parentActivity.enableSensor(false);
        DbHandler.endTrip();
        this.liveRecording = false;
    }

    @Override
    public void update(ObdCommand cmd) {
        switch (cmd.getName()) {
            case "Engine RPM":
                tvRPM.setText(cmd.getCalculatedResult());
                break;
            case "Vehicle Speed":
                tvSpeed.setText(cmd.getCalculatedResult());
                break;
            case "Engine Coolant Temperature":
                tvCoolant.setText(cmd.getCalculatedResult());
                break;
        }
    }

    public void update(Location l) {
        tvGpsLat.setText(Double.toString(l.getLatitude()));
        tvGpsLon.setText(Double.toString(l.getLongitude()));
        tvGpsAlt.setText(Double.toString(l.getAltitude()));
        tvGpsSpd.setText(Double.toString(l.getSpeed()));
        mapLine.add(new LatLng(l.getLatitude(), l.getLongitude()));
        gMap.addPolyline(mapLine);
    }

    public void update(SensorEvent s) {
        tvAccX.setText(Float.toString(s.values[0]));
        tvAccY.setText(Float.toString(s.values[1]));
        tvAccZ.setText(Float.toString(s.values[2]));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        parentActivity.enableQueue(false);
        DbHandler.endTrip();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        Location l=parentActivity.getLocation();
        LatLng ll = new LatLng(l.getLatitude(), l.getLongitude());

        mapLine = new PolylineOptions();
        mapLine.color( Color.parseColor( "#CC0000FF" ) );
        mapLine.width( 5 );
        mapLine.visible( true );
        mapLine.add(ll);
        gMap.addPolyline( mapLine );

        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 13));
        gMap.addMarker(new MarkerOptions()
                .title("Home")
                .position(ll));
        gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapView.onResume();

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private class loadSpinnerAsync extends AsyncTask<String, String, String>
    {
        private ProgressDialog loadingDialog;

        @Override
        protected void onPreExecute()
        {
            loadingDialog = ProgressDialog.show(getActivity(), "Collecting supported data types, please wait...", "Loading...");
            loadingDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {
            populateSpinner();
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            loadingDialog.dismiss();
        }
    }
}
