package obdtool.com.obd2_2.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.pires.obd.commands.ObdCommand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.db.Model.ObdEntry;
import obdtool.com.obd2_2.db.Model.Trip;
import obdtool.com.obd2_2.util.ReceiverFragment;

public class TripDetailsFragment extends Fragment implements ReceiverFragment {

    private static final String TRIP_ID = "tripID";

    private int tripId;

    private Map<String, List<ObdEntry>> obdMap = new HashMap<>();

    private OnFragmentInteractionListener mListener;

    private TextView tv;

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
        if (getArguments() != null) {
            tripId = getArguments().getInt(TRIP_ID);
            fillMap(tripId);
        }
    }

    private void fillMap(int trip) {
        List<ObdEntry> data = DbHandler.getTripData(trip);

        for(ObdEntry e : data)
        {
            List<ObdEntry> value = obdMap.get(e.getRequest());
            if(value==null)
            {
                obdMap.put(e.getRequest(), new ArrayList<ObdEntry>());
                value = obdMap.get(e.getRequest());
            }
            value.add(e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_trip_details, container, false);

        tv = (TextView) v.findViewById(R.id.data);
        tv.setText(printData());

        return v;
    }

    private String printData() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, List<ObdEntry>> e : obdMap.entrySet())
        {
            sb.append(e.getKey());
            sb.append("\n");
            for(ObdEntry oe : e.getValue())
            {
                sb.append(oe.getFormatted_data());
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
