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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.github.pires.obd.commands.ObdCommand;

import java.util.ArrayList;
import java.util.List;

import Commands.PID.PIDSupport;
import Enums.PIDsupport;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.activity.MainActivity;
import obdtool.com.obd2_2.util.ReceiverFragment;


public class VehicleInfoFragment extends Fragment implements ReceiverFragment {

    private OnFragmentInteractionListener mListener;
    private Button supportBtn;
    private ListView supportList;
    private MainActivity parentActivity;

    private List<String> items = new ArrayList<>();
    private ArrayAdapter<String> itemsAdapter = null;

    public VehicleInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentActivity=(MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_vehicle_info, container, false);
        supportList = (ListView) v.findViewById(R.id.supportedList);
        supportBtn = (Button)v.findViewById(R.id.btnPIDsupport);

        itemsAdapter = new ArrayAdapter<String>(parentActivity, android.R.layout.simple_list_item_1, items);
        supportList.setAdapter(itemsAdapter);

        supportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PIDSupport supportCmd = (PIDSupport) parentActivity.ObdCommand(new PIDSupport(PIDsupport.PID_01_20));
                List<ObdCommand> resultList = supportCmd.getSupportedCommands();
                for(ObdCommand o : resultList)
                {
                    items.add(o.getName());
                    itemsAdapter.notifyDataSetChanged();
                }
                itemsAdapter.notifyDataSetChanged();
            }
        });

        return v;
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
