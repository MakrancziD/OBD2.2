package obdtool.com.obd2_2.Fragment;

import android.content.Context;
import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.github.pires.obd.commands.ObdCommand;

import java.util.ArrayList;
import java.util.List;

import Commands.PID.SpeedCommand;
import obdtool.com.obd2_2.Adapter.AccelerationRecyclerViewAdapter;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.activity.MainActivity;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.db.Model.Acceleration;
import obdtool.com.obd2_2.util.ReceiverFragment;
import obdtool.com.obd2_2.util.Stopwatch;


public class AccelerationFragment extends Fragment implements ReceiverFragment {

    private OnListFragmentInteractionListener mListener;

    private NumberPicker numPickStart;
    private NumberPicker numPickFinish;
    private Button btnStart;
    private TextView txtTimer;
    private TextView txtSpeed;
    private RecyclerView listAcc;

    private MainActivity parentActivity;

    final int MSG_START_TIMER = 0;
    final int MSG_STOP_TIMER = 1;
    final int MSG_UPDATE_TIMER = 3;
    final int REFRESH_RATE=50;

    final int PICKER_MIN = 0;
    final int PICKER_MAX = 200;
    final int PICKER_STEP = 10;

    Stopwatch timer = new Stopwatch();

    private List<ObdCommand> cmdList = new ArrayList<>();
    private boolean isRecording = false;
    private boolean isStarted = false;

    //private int testSpeed=0;

    private NumberPicker.OnScrollListener scrollListener = new NumberPicker.OnScrollListener() {
        @Override
        public void onScrollStateChange(NumberPicker view, int scrollState) {
            btnStart.setEnabled(numPickStart.getValue()<numPickFinish.getValue());
        }
    };

//    private Thread testThread = new Thread(new Runnable() {
//        @Override
//        public void run() {
//            for(int i=0;i<22;i++) {
//                testSpeed+=5;
//                testTimer();
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    });

    public AccelerationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity=(MainActivity) getActivity();
        cmdList.add(new SpeedCommand());
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

        txtTimer = (TextView) v.findViewById(R.id.txtTimer);
        txtSpeed = (TextView) v.findViewById(R.id.txtSpeed);

        listAcc = (RecyclerView) v.findViewById(R.id.acc_list);
        listAcc.setAdapter(new AccelerationRecyclerViewAdapter(DbHandler.getAllAcc(), mListener));

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
//        if(!testThread.isAlive())
//            testThread.start();
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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

        if(isStarted) {
            if(((SpeedCommand) cmd).getMetricSpeed()>numPickFinish.getValue()) {
                endMeasurement(true);
            }
            else if(((SpeedCommand) cmd).getMetricSpeed()<=numPickStart.getValue()) {
                endMeasurement(false);
            }
        }
        else if(((SpeedCommand) cmd).getMetricSpeed()>numPickStart.getValue()) {
            startMeasurment();
        }
    }

    private void startMeasurment() {
        isStarted=true;
        mHandler.sendEmptyMessage(MSG_START_TIMER);
    }

    private void endMeasurement(boolean success) {
        isStarted=false;
        mHandler.sendEmptyMessage(MSG_STOP_TIMER);
        stopRecording();
        if(success) {
            storeTime(timer.getElapsedTime());
        }
    }

    @Override
    public void update(Location l) {

    }

    @Override
    public void update(SensorEvent e) {

    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Acceleration item);
    }

    private void storeTime(long elapsed)
    {
        DbHandler.storeAccelResult(numPickStart.getValue()*PICKER_STEP,
                numPickFinish.getValue()*PICKER_STEP,
                elapsed); //TODO: track current vehicle
    }

    Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_TIMER:
                    timer.start();
                    mHandler.sendEmptyMessage(MSG_UPDATE_TIMER);
                    break;

                case MSG_UPDATE_TIMER:
                    updateTimer(timer.getElapsedTime());
                    mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER,REFRESH_RATE);
                    break;
                case MSG_STOP_TIMER:
                    mHandler.removeMessages(MSG_UPDATE_TIMER);
                    timer.stop();
                    updateTimer(timer.getElapsedTime());
                    break;
                default:
                    break;
            }
        }
    };

    private void updateTimer(long time) {
        int sec = (int) Math.floor(time/1000);
        int ms = (int) (time-(sec*1000));
        String strMs = Integer.toString(ms);
        while(strMs.length()<3) {
            strMs+="0";
        }
        txtTimer.setText(sec+"."+strMs);
    }

//    private void testTimer() {
//
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                txtSpeed.setText(Integer.toString(testSpeed));
//
//                if(isStarted) {
//                    if(testSpeed>(numPickFinish.getValue()*PICKER_STEP)) {
//                        endMeasurement(true);
//                    }
//                    else if(testSpeed<=(numPickStart.getValue()*PICKER_STEP)) {
//                        endMeasurement(false);
//                    }
//                }
//                else if(testSpeed>(numPickStart.getValue()*PICKER_STEP) && testSpeed<(numPickFinish.getValue()*PICKER_STEP)) {
//                    startMeasurment();
//                }
//            }
//        });
//    }
}
