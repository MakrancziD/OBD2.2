package obdtool.com.obd2_2.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.pires.obd.commands.ObdCommand;

import java.util.ArrayList;
import java.util.List;

import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.activity.MainActivity;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.db.Model.Vehicle;
import obdtool.com.obd2_2.util.ReceiverFragment;

public class ConnectionFragment extends Fragment implements ReceiverFragment {

    private OnFragmentInteractionListener mListener;
    private TextView connectionStatus;
    private Button btnBt;
    private Spinner vehSpinner;
    private MainActivity parentActivity;

    private List<Vehicle> vehList = new ArrayList<>();

    public ConnectionFragment() {
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
        View view = inflater.inflate(R.layout.fragment_connection, container, false);

        vehList = DbHandler.getVehicles();

        final ArrayAdapter<Vehicle> spinnerAdapter = new ArrayAdapter<Vehicle>(getContext(), android.R.layout.simple_spinner_item, vehList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehSpinner = (Spinner) view.findViewById(R.id.veh_spinner);
        vehSpinner.setAdapter(spinnerAdapter);

        vehSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DbHandler.setCurrentVehicle(spinnerAdapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        connectionStatus=(TextView)view.findViewById(R.id.connectionStatus);



        btnBt =(Button)view.findViewById(R.id.btnBluetooth);

        btnBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.SelectBtDevice();
            }
        });


        return view;
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

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
