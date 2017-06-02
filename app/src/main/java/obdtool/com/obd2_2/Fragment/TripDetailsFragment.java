package obdtool.com.obd2_2.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.hardware.SensorEvent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.github.pires.obd.commands.ObdCommand;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.db.Model.ObdEntry;
import obdtool.com.obd2_2.db.Model.SensorEntry;
import obdtool.com.obd2_2.db.Model.Trip;
import obdtool.com.obd2_2.util.LiveDataPoint;
import obdtool.com.obd2_2.util.ReceiverFragment;

public class TripDetailsFragment extends Fragment implements ReceiverFragment, OnMapReadyCallback {

    private static final String TRIP_ID = "tripID";

    private int tripId;
    private Trip trip;
    private Date tripStart;

    private Map<String, List<LiveDataPoint>> dataMap = new HashMap<>();
    private Map<String, LineGraphSeries<DataPoint>> graphMap = new HashMap<>();
    private GraphView graph;
    private MapView mapView;
    private LocationManager locationManager;

    private final String ACC_X = "Accelerometer - X";
    private final String ACC_Y = "Accelerometer - Y";
    private final String ACC_Z = "Accelerometer - Z";

    private OnFragmentInteractionListener mListener;

    private LinearLayout layoutChkbox;

    public TripDetailsFragment() {
        // Required empty public constructor
    }

    public static TripDetailsFragment newInstance(int tripId) {
        TripDetailsFragment fragment = new TripDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(TRIP_ID, tripId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        if (getArguments() != null) {
            tripId = getArguments().getInt(TRIP_ID);
            trip = DbHandler.getTrip(tripId);
            fillObdMap();
            fillSensorMap();
            tripStart = DbHandler.getTrip(tripId).getStart_time();
        }
    }

    private void fillObdMap() {
        List<ObdEntry> data = new ArrayList<>(trip.getObdEntries());

        for (ObdEntry e : data) {
            List<LiveDataPoint> value = dataMap.get(e.getRequest());
            if (value == null) {
                dataMap.put(e.getRequest(), new ArrayList<LiveDataPoint>());
                value = dataMap.get(e.getRequest());
            }
            value.add(new LiveDataPoint(e.getFormatted_data(), e.getTimestamp()));
        }
    }

    private void fillSensorMap() {
        List<SensorEntry> data = new ArrayList<>(trip.getSensorEntries());

        List<LiveDataPoint> acc_X = null;
        List<LiveDataPoint> acc_Y = null;
        List<LiveDataPoint> acc_Z = null;

        for(SensorEntry s : data) {
            if(s.getSensor()!=null && s.getSensor().equals("Accelerometer")) {
                acc_X = dataMap.get(ACC_X);
                acc_Y = dataMap.get(ACC_Y);
                acc_Z = dataMap.get(ACC_Z);
                if(acc_X==null) {
                    dataMap.put(ACC_X, new ArrayList<LiveDataPoint>());
                    acc_X = dataMap.get(ACC_X);
                }
                if(acc_Y==null) {
                    dataMap.put(ACC_Y, new ArrayList<LiveDataPoint>());
                    acc_Y = dataMap.get(ACC_Y);
                }
                if(acc_Z==null) {
                    dataMap.put(ACC_Z, new ArrayList<LiveDataPoint>());
                    acc_Z = dataMap.get(ACC_Z);
                }
                break;
            }
        }

        for (SensorEntry e : data) {
            if(e.getSensor()!=null && e.getSensor().equals("Accelerometer")) {
                acc_X.add(new LiveDataPoint(e.getData1(), e.getTimestamp()));
                acc_Y.add(new LiveDataPoint(e.getData2(), e.getTimestamp()));
                acc_Z.add(new LiveDataPoint(e.getData3(), e.getTimestamp()));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trip_details, container, false);

        mapView = (MapView) v.findViewById(R.id.tripMap);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        graph = (GraphView) v.findViewById(R.id.tripGraph);
        layoutChkbox = (LinearLayout) v.findViewById(R.id.checkbox_view);
        populateCheckBoxView();

        return v;
    }

    private void populateCheckBoxView() {
        if (dataMap != null) {
            for (Map.Entry e : dataMap.entrySet()) {
                final String eKey = e.getKey().toString();
                CheckBox cb = new CheckBox(getContext());
                cb.setText(eKey);
                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            addToGraph(eKey);
                        } else {
                            removeFromGraph(eKey);
                        }
                    }
                });
                layoutChkbox.addView(cb);
            }
        }
    }

    private void addToGraph(String entries) {
        if (graphMap.containsKey(entries)) {
            graph.addSeries(graphMap.get(entries));
        } else {
            List<LiveDataPoint> entryList = dataMap.get(entries);
            DataPoint[] dataPointList = new DataPoint[entryList.size()];
            for (int i = 0; i<entryList.size(); i++) {
                long diff = Math.abs(entryList.get(i).getTimestamp().getTime() - tripStart.getTime());
                dataPointList[i] = new DataPoint(diff/1000, entryList.get(i).getValue());
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointList);
            graphMap.put(entries, series);

            updateGraph();
        }
    }

    private void removeFromGraph(String key) {
        if (graphMap.containsKey(key)) {
            graph.removeSeries(graphMap.get(key));
            graphMap.remove(key);
        }
    }

    private void updateGraph() {
        graph.removeAllSeries();
        double maxTime = 0;
        for(Map.Entry e : graphMap.entrySet()) {
            graph.addSeries((Series) e.getValue());
            double currentMax = ((Series) e.getValue()).getHighestValueX();
            if(maxTime<currentMax)
                maxTime=currentMax;
        }
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxX(maxTime);
        graph.getViewport().setMinX(0);
        graph.getViewport().setScalable(true);

    }

    private String printData() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<LiveDataPoint>> e : dataMap.entrySet()) {
            sb.append(e.getKey());
            sb.append("\n");
            for (LiveDataPoint oe : e.getValue()) {
                sb.append(oe);
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

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
    public void update(ObdCommand cmd) {

    }

    @Override
    public void update(Location l) {

    }

    @Override
    public void update(SensorEvent e) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng home = new LatLng(47.975956, 20.731853);

        PolylineOptions options = new PolylineOptions();
        options.color( Color.parseColor( "#CC0000FF" ) );
        options.width( 5 );
        options.visible( true );

        List<SensorEntry> gpsEntries = DbHandler.getGpsEntriesOfTrip(trip);
        if(gpsEntries!=null) {
            for (SensorEntry gps : gpsEntries) {
                options.add(new LatLng(gps.getData1(), gps.getData2()));
            }
        }
        else {
            options.add(home);
        }
        googleMap.addPolyline( options );

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(options.getPoints().get(0), 13));
        googleMap.addMarker(new MarkerOptions()
                    .title("Start")
                    .position(options.getPoints().get(0)));
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapView.onResume();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
