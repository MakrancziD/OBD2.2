package obdtool.com.obd2_2.Fragment;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.SensorEvent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;

import com.github.pires.obd.commands.ObdCommand;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import obdtool.com.obd2_2.R;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.db.Model.Vehicle;
import obdtool.com.obd2_2.util.ReceiverFragment;

public class PreferencesFragment extends PreferenceFragmentCompat implements ReceiverFragment {
    private OnFragmentInteractionListener mListener;

    private static final String PREFS = "sharedPrefs";
    private static final String DEVICES = "btDevices";

    private ListPreference prefBt;
    private ListPreference prefVeh;

    Gson gson = new Gson();

    private SharedPreferences sharedPreferences;

    public PreferencesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        addPreferencesFromResource(R.xml.preferences);

        prefBt = (ListPreference) findPreference("list_preference_bt");
        prefVeh = (ListPreference) findPreference("list_preference_veh");

        setBtListPreferenceData(prefBt);
        setVehiclePreferenceData(prefVeh);
    }

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {

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

    protected void setBtListPreferenceData(ListPreference lp)
    {
        Type setType = new TypeToken<HashSet<BluetoothDevice>>(){}.getType();
        Set<BluetoothDevice> devList = gson.fromJson(sharedPreferences.getString(DEVICES, null), setType);

        if(devList!=null) {
            CharSequence[] entries = new CharSequence[devList.size()];
            CharSequence[] entryValues = new CharSequence[devList.size()];
            int i = 0;
            for (BluetoothDevice dev : devList) {
                entries[i] = i + ". " + dev.getName() + " - " + dev.getAddress();
                entryValues[i] = gson.toJson(dev);
                i++;
            }
            lp.setEntries(entries);
            lp.setEntryValues(entryValues);
        }
    }

    protected void setVehiclePreferenceData(ListPreference lp)
    {
        List<Vehicle> vehicleList = DbHandler.getVehicles();

        CharSequence[] entries = new CharSequence[vehicleList.size()];
        CharSequence[] entryValues = new CharSequence[vehicleList.size()];
        int i = 0;
        for(Vehicle vehicle : vehicleList)
        {
            entries[i] = vehicle.toString();
            entryValues[i] = Integer.toString(vehicle.getID_vehicle());
            i++;
        }
        lp.setEntries(entries);
        lp.setEntryValues(entryValues);
    }
}
