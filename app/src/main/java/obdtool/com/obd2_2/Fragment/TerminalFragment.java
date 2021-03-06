package obdtool.com.obd2_2.Fragment;

import android.content.Context;
import android.hardware.SensorEvent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.pires.obd.commands.ObdCommand;

import java.util.ArrayList;
import java.util.List;

import obdtool.com.obd2_2.Adapter.TerminalRecyclerViewAdapter;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.activity.MainActivity;
import obdtool.com.obd2_2.util.ReceiverFragment;

public class TerminalFragment extends Fragment implements ReceiverFragment {

    Button btnSend;
    EditText editCommand;
    private final List<String> items = new ArrayList<>();
    MainActivity parentActivity;
    private TerminalRecyclerViewAdapter adapter;
    private RecyclerView terminalView;

    private OnFragmentInteractionListener mListener;

    public TerminalFragment() {
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
        View view = inflater.inflate(R.layout.fragment_terminal, container, false);
        terminalView= (RecyclerView) view.findViewById(R.id.terminal_list);
        adapter = new TerminalRecyclerViewAdapter(items);
        terminalView.setAdapter(adapter);

        editCommand=(EditText)view.findViewById(R.id.customCommand);
        btnSend=(Button) view.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runCommand(editCommand.getText().toString());
            }
        });

        return view;
    }

    private void runCommand(String command)
    {
        adapter.addCommand(command);
        adapter.addCommand(parentActivity.ObdCommand(command));
        adapter.notifyDataSetChanged();
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

    @Override
    public void update(Location l) {

    }

    @Override
    public void update(SensorEvent e) {

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
