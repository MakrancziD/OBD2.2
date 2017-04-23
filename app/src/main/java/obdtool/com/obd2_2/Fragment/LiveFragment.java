package obdtool.com.obd2_2.Fragment;

import android.content.Context;
import android.hardware.SensorEvent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.pires.obd.commands.ObdCommand;

import java.util.ArrayList;
import java.util.List;

import Commands.PID.EngineCoolantTemperatureCommand;
import Commands.PID.RPMCommand;
import Commands.PID.SpeedCommand;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.activity.MainActivity;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.service.LocationService;
import obdtool.com.obd2_2.util.ReceiverFragment;

public class LiveFragment extends Fragment implements ReceiverFragment {

    private OnFragmentInteractionListener mListener;

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

    private boolean liveRecording = false;

    List<ObdCommand> cmdList = new ArrayList<>();

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
        View view =  inflater.inflate(R.layout.fragment_live, container, false);

        tvRPM=(TextView) view.findViewById(R.id.value_RPM);
        tvSpeed=(TextView) view.findViewById(R.id.value_Speed);
        tvCoolant=(TextView) view.findViewById(R.id.value_coolant);

        tvAccX = (TextView)view.findViewById(R.id.value_acc_x);
        tvAccY = (TextView)view.findViewById(R.id.value_acc_y);
        tvAccZ = (TextView)view.findViewById(R.id.value_acc_z);

        tvGpsLat = (TextView)view.findViewById(R.id.value_gps_lat);
        tvGpsLon = (TextView)view.findViewById(R.id.value_gps_lon);
        tvGpsAlt = (TextView)view.findViewById(R.id.value_gps_alt);
        tvGpsSpd = (TextView)view.findViewById(R.id.value_gps_spd);

        btnStartStop = (Button) view.findViewById(R.id.btnStartStop);
        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(liveRecording)
                {
                    stopRecording();
                }
                else
                {
                    startRecording();
                }
            }
        });

        DbHandler.startTrip();

        return view;
    }

    private void startRecording()
    {

        parentActivity.initLiveCommands(cmdList);
        DbHandler.startTrip();
        parentActivity.enableQueue(true);
        parentActivity.enableLocation(true);
        parentActivity.enableSensor(true);
        this.liveRecording=true;
    }

    private void stopRecording()
    {
        parentActivity.enableQueue(false);
        parentActivity.enableLocation(false);
        parentActivity.enableSensor(false);
        DbHandler.endTrip();
        this.liveRecording=false;
    }

    @Override
    public void update(ObdCommand cmd)
    {
        switch(cmd.getName())
        {
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

    public void update(Location l)
    {
        tvGpsLat.setText(Double.toString(l.getLatitude()));
        tvGpsLon.setText(Double.toString(l.getLongitude()));
        tvGpsAlt.setText(Double.toString(l.getAltitude()));
        tvGpsSpd.setText(Double.toString(l.getSpeed()));
    }

    public void update (SensorEvent s)
    {
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
