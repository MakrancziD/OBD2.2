package obdtool.com.obd2_2.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.hardware.SensorEvent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.commands.control.TroubleCodesCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Commands.PID.MonitorStatusCommand;
import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.activity.MainActivity;
import obdtool.com.obd2_2.util.ReceiverFragment;

public class DiagnosticFragment extends Fragment implements ReceiverFragment {

    private OnFragmentInteractionListener mListener;
    private MainActivity parentActivity;

    private TextView milOn;
    private TextView dtcCnt;
    private ListView dtcList;
    private FloatingActionButton btnClear;
    private TextView testResults;

    private MonitorStatusCommand currentMonitorStatus;
    private TroubleCodesCommand currentDTCs;

    private List<String> itemsDTC = new ArrayList<>();
    private ArrayAdapter<String> dtcAdapter =null;

    public DiagnosticFragment() {
        // Required empty public constructor
    }

    public static DiagnosticFragment newInstance(String param1, String param2) {
        DiagnosticFragment fragment = new DiagnosticFragment();
        return fragment;
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
        View v = inflater.inflate(R.layout.fragment_diagnostic, container, false);
        //milOn = (TextView) v.findViewById(R.id.mil_status);
        dtcCnt = (TextView) v.findViewById(R.id.dtc_cnt);
        dtcList = (ListView) v.findViewById(R.id.list_DTC);
        btnClear = (FloatingActionButton) v.findViewById(R.id.btn_clear);
        testResults = (TextView) v.findViewById(R.id.tests);

        dtcAdapter = new ArrayAdapter<String>(parentActivity, android.R.layout.simple_list_item_1, itemsDTC);
        dtcList.setAdapter(dtcAdapter);

        new loadDiagDataAsync().execute();

        return v;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class loadDiagDataAsync extends AsyncTask<String, String, String>
    {
        private ProgressDialog loadingDialog;


        @Override
        protected void onPreExecute()
        {
            loadingDialog = ProgressDialog.show(getActivity(), "Collecting Diagnostic data, please wait...", "Loading...");
            loadingDialog.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... params) {
            currentMonitorStatus = (MonitorStatusCommand) parentActivity.ObdCommand(new MonitorStatusCommand());
            currentDTCs = (TroubleCodesCommand) parentActivity.ObdCommand(new TroubleCodesCommand());

            String fuelSys = parentActivity.ObdCommand("01 03");

            return "?";
        }

        @Override
        protected void onPostExecute(String s)
        {
            loadingDialog.dismiss();
            refreshView();
        }

        private void refreshView()
        {
            milOn.setText(currentMonitorStatus.isMilOn()?R.string.on:R.string.off);
            dtcCnt.setText(Integer.toString(currentMonitorStatus.getNumOfDTCs()));
            testResults.setText(currentMonitorStatus.getCalculatedResult());
            itemsDTC.clear();
            itemsDTC.addAll(Arrays.asList(currentDTCs.getCalculatedResult().split("\n")));
            dtcAdapter.notifyDataSetChanged();
        }
    }
}
