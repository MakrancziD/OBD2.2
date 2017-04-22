package obdtool.com.obd2_2.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import com.github.pires.obd.commands.ObdCommand;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Commands.PID.SpeedCommand;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.activity.MainActivity;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.db.Model.Acceleration;
import obdtool.com.obd2_2.db.Model.Vehicle;
import obdtool.com.obd2_2.util.ReceiverFragment;
import obdtool.com.obd2_2.util.Stopwatch;


public class AccelerationFragment extends Fragment implements ReceiverFragment {

    private OnFragmentInteractionListener mListener;

    private RangeSeekBar rangeBar;
    private Button btnStart;
    private Chronometer chrTimer;
    private Chronometer chrSpeed;

    private MainActivity parentActivity;

    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER_SUCCESSFUL = 1;
    final int MSG_STOP_TIMER_UNSUCCESSFUL = 2;
    final int MSG_UPDATE_TIMER = 3;

    Stopwatch timer = new Stopwatch();
    private int REFRESH_RATE;

    private SharedPreferences sharedPreferences;

    private List<ObdCommand> cmdList = new ArrayList<>();
    private boolean isRecording = false;


    public AccelerationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity=(MainActivity) getActivity();
        cmdList.add(new SpeedCommand());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        REFRESH_RATE = sharedPreferences.getInt("sensor_rate", 100);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_acceleration, container, false);

        rangeBar = (RangeSeekBar) v.findViewById(R.id.rangeBar);
        rangeBar.setRangeValues(0, 200);
        rangeBar.setSelectedMinValue(0);
        rangeBar.setSelectedMaxValue(100);

        btnStart = (Button) v.findViewById(R.id.btnStartAcceleration);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });

        chrTimer = (Chronometer) v.findViewById(R.id.chronoTimer);
        chrSpeed = (Chronometer) v.findViewById(R.id.chronoSpeed);

        return v;
    }

    private void startRecording()
    {
        parentActivity.initLiveCommands(cmdList);
        parentActivity.enableQueue(true);
        this.isRecording=true;
    }

    private void stopRecording()
    {
        parentActivity.enableQueue(false);
        this.isRecording=false;
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
    public void update(ObdCommand cmd) {
        if(cmd instanceof SpeedCommand) {
            chrSpeed.setText(((SpeedCommand) cmd).getMetricSpeed());

            if(((SpeedCommand) cmd).getMetricSpeed()>rangeBar.getAbsoluteMaxValue().intValue())
            {
                mHandler.sendEmptyMessage(MSG_STOP_TIMER_SUCCESSFUL);
                return;
            }
            if(((SpeedCommand) cmd).getMetricSpeed()>rangeBar.getAbsoluteMinValue().intValue())
            {
                mHandler.sendEmptyMessage(MSG_START_TIMER);
                return;
            }
            else
            {
                mHandler.sendEmptyMessage(MSG_STOP_TIMER_UNSUCCESSFUL);
            }
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_TIMER:
                    timer.start(); //start timer
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
                    break;
                case MSG_UPDATE_TIMER:
                    chrTimer.setText(""+ timer.getElapsedTime());
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER,REFRESH_RATE); //text view is updated every second,
                    break;                                  //though the timer is still running
                case MSG_STOP_TIMER_SUCCESSFUL:
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    timer.stop();//stop timer
                    chrTimer.setText(""+ timer.getElapsedTime());
                    storeTime(timer.getElapsedTime());
                    break;
                case MSG_STOP_TIMER_UNSUCCESSFUL:
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    timer.stop();//stop timer
                    chrTimer.setText(""+ timer.getElapsedTime());
                    break;
                default:
                    break;
            }
        }
    };

    private void storeTime(long elapsed)
    {
        DbHandler.storeAccelResult(new Acceleration(new Date(),
                rangeBar.getAbsoluteMinValue().intValue(),
                rangeBar.getAbsoluteMaxValue().intValue(),
                elapsed,
                new Vehicle())); //TODO: track current vehicle
    }
}
