package obdtool.com.obd2_2.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorEvent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.NumberPicker;
import android.widget.TextView;

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

    private NumberPicker numPickStart;
    private NumberPicker numPickFinish;
    private Button btnStart;
    private Chronometer chrTimer;
    private TextView txtSpeed;

    private MainActivity parentActivity;

    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER_SUCCESSFUL = 1;
    final int MSG_STOP_TIMER_UNSUCCESSFUL = 2;
    final int MSG_UPDATE_TIMER = 3;

    final int PICKER_MIN = 0;
    final int PICKER_MAX = 200;
    final int PICKER_STEP = 10;
    final int PICKER_DEFAULT_START=0;
    final int PICKER_DEFAULT_FINISH = 100;

    Stopwatch timer = new Stopwatch();
    private int REFRESH_RATE;

    private SharedPreferences sharedPreferences;

    private List<ObdCommand> cmdList = new ArrayList<>();
    private boolean isRecording = false;

    private NumberPicker.OnScrollListener scrollListener = new NumberPicker.OnScrollListener() {
        @Override
        public void onScrollStateChange(NumberPicker view, int scrollState) {
            btnStart.setEnabled(numPickStart.getValue()<numPickFinish.getValue());
        }
    };


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

        numPickStart = (NumberPicker) v.findViewById(R.id.numberPickerStart);
        numPickStart.setMinValue(PICKER_MIN);
        numPickStart.setMaxValue((PICKER_MAX-PICKER_MIN)/PICKER_STEP);
        numPickStart.setDisplayedValues(GetPickerValues());
        numPickStart.setValue(0);
        numPickStart.setOnScrollListener(scrollListener);

        numPickFinish = (NumberPicker) v.findViewById(R.id.numberPickerFinish);
        numPickFinish.setMinValue(PICKER_MIN);
        numPickFinish.setMaxValue((PICKER_MAX-PICKER_MIN)/PICKER_STEP);
        numPickFinish.setDisplayedValues(GetPickerValues());
        numPickFinish.setValue(10);
        numPickFinish.setOnScrollListener(scrollListener);

        btnStart = (Button) v.findViewById(R.id.btnStartAcceleration);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isRecording)
                    startRecording();
                else
                    stopRecording();
            }
        });

        chrTimer = (Chronometer) v.findViewById(R.id.chronoTimer);
        txtSpeed = (TextView) v.findViewById(R.id.txtSpeed);

        return v;
    }

    private String[] GetPickerValues() {
        String[] out = new String[((PICKER_MAX-PICKER_MIN)/PICKER_STEP)+1];
        int current = PICKER_MIN;
        for(int i=0;i<out.length;i++) {
            out[i] = Integer.toString(current);
            current+=PICKER_STEP;
        }
        return out;
    }

    private void startRecording()
    {
        btnStart.setText(R.string.stop);
        numPickStart.setEnabled(false);
        numPickFinish.setEnabled(false);
        parentActivity.initLiveCommands(cmdList);
        parentActivity.enableQueue(true);
        this.isRecording=true;
    }

    private void stopRecording()
    {
        btnStart.setText(R.string.start);
        parentActivity.enableQueue(false);
        this.isRecording=false;
        numPickStart.setEnabled(true);
        numPickFinish.setEnabled(true);
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
        //if(cmd instanceof SpeedCommand) {
            txtSpeed.setText(((SpeedCommand) cmd).getMetricSpeed());

            if(((SpeedCommand) cmd).getMetricSpeed()>numPickFinish.getValue())
            {
                mHandler.sendEmptyMessage(MSG_STOP_TIMER_SUCCESSFUL);
                return;
            }
            if(((SpeedCommand) cmd).getMetricSpeed()>numPickStart.getValue())
            {
                mHandler.sendEmptyMessage(MSG_START_TIMER);
                return;
            }
            else
            {
                mHandler.sendEmptyMessage(MSG_STOP_TIMER_UNSUCCESSFUL);
            }
        //}
    }

    @Override
    public void update(Location l) {

    }

    @Override
    public void update(SensorEvent e) {

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
                    stopRecording();
                    storeTime(timer.getElapsedTime());
                    break;
                case MSG_STOP_TIMER_UNSUCCESSFUL:
                    mHandler.removeMessages(MSG_UPDATE_TIMER); // no more updates.
                    timer.stop();//stop timer
                    chrTimer.setText(""+ timer.getElapsedTime());
                    stopRecording();
                    break;
                default:
                    break;
            }
        }
    };

    private void storeTime(long elapsed)
    {
        DbHandler.storeAccelResult(new Acceleration(new Date(),
                numPickStart.getValue(),
                numPickFinish.getValue(),
                elapsed,
                new Vehicle())); //TODO: track current vehicle
    }
}
