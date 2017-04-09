package obdtool.com.obd2_2.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.pires.obd.commands.ObdCommand;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Commands.PID.EngineCoolantTemperatureCommand;
import Commands.PID.RPMCommand;
import Commands.PID.SpeedCommand;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.activity.MainActivity;
import obdtool.com.obd2_2.util.ReceiverFragment;

public class LiveFragment extends Fragment implements ReceiverFragment {

    private OnFragmentInteractionListener mListener;

    private TextView tvRPM;
    private TextView tvSpeed;
    private TextView tvCoolant;

    private MainActivity parentActivity;

    public LiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_live, container, false);

        tvRPM=(TextView) view.findViewById(R.id.value_RPM);
        tvSpeed=(TextView) view.findViewById(R.id.value_Speed);
        tvCoolant=(TextView) view.findViewById(R.id.value_coolant);

        List<ObdCommand> cmdList = new ArrayList<>();
        cmdList.add(new SpeedCommand());
        cmdList.add(new RPMCommand());
        cmdList.add(new EngineCoolantTemperatureCommand());

        parentActivity.initLiveCommands(cmdList);
        parentActivity.enableQueue(true);

        return view;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
