package obdtool.com.obd2_2.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.hardware.SensorEvent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.pires.obd.commands.ObdCommand;

import java.util.List;

import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.db.Model.Vehicle;
import obdtool.com.obd2_2.util.ReceiverFragment;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class VehicleListFragment extends Fragment implements ReceiverFragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private FloatingActionButton addVehicle;

    private RecyclerView recyclerView;
    private MyVehicleRecyclerViewAdapter vehListAdapter;
    private List<Vehicle> vehList;

    public VehicleListFragment() {
    }

    public static VehicleListFragment newInstance(int columnCount) {
        VehicleListFragment fragment = new VehicleListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_list, container, false);

        addVehicle = (FloatingActionButton) view.findViewById(R.id.add_vehicle);

        addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createVehicle();
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.veh_list);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }
        vehList = DbHandler.getVehicles();
        vehListAdapter = new MyVehicleRecyclerViewAdapter(vehList, mListener);
        recyclerView.setAdapter(vehListAdapter);

        return view;
    }

    private void createVehicle() {

        final Dialog createDialog = new Dialog(getContext());
        createDialog.setContentView(R.layout.dialog_vehicle);
        createDialog.setTitle(R.string.new_vehicle);

        final EditText edit_name = (EditText) createDialog.findViewById(R.id.edit_veh_name);
        final EditText edit_make = (EditText) createDialog.findViewById(R.id.edit_veh_make);
        final EditText edit_model = (EditText) createDialog.findViewById(R.id.edit_veh_model);

        Button btnCreate = (Button) createDialog.findViewById(R.id.btn_create);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbHandler.addVehicle(
                        new Vehicle(edit_name.getText().toString(),
                                    edit_make.getText().toString(),
                                    edit_model.getText().toString()
                        )
                );
                refreshList();
                createDialog.dismiss();
            }
        });
        createDialog.show();
    }

    private void refreshList() {
        vehList = DbHandler.getVehicles();
        vehListAdapter.notifyDataSetChanged();
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

    }

    @Override
    public void update(Location l) {

    }

    @Override
    public void update(SensorEvent e) {

    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Vehicle item);
    }
}
