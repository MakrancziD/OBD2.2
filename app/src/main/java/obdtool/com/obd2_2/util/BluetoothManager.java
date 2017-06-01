package obdtool.com.obd2_2.util;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BluetoothManager {
    private static final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private static final String TAG = BluetoothManager.class.getName();
    private static final UUID UUID_SELF = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String COMP = BluetoothManager.class.getName();
    private static BluetoothDevice btDevice;

    @Nullable
    public static BluetoothSocket Connect(BluetoothDevice btDevice)
    {
        BluetoothSocket socket=null;
        try {
            socket=btDevice.createRfcommSocketToServiceRecord(UUID_SELF);
            socket.connect();
            Log.d(COMP, "Bluetooth device connected.");
        } catch (IOException e) {
            Log.e(COMP, "Bluetooth connection failed!");
            e.printStackTrace();
        }

        return socket;
    }

    public static BluetoothAdapter getBtAdapter() {
        return btAdapter;
    }

    public static Set<BluetoothDevice> GetBtDevices()
    {
        if(btAdapter.isEnabled()) {
            return getBtAdapter().getBondedDevices();
        }
        return null;
    }

    public static void btDiscovery(boolean turnOn)
    {
        if(btAdapter.isDiscovering())btAdapter.cancelDiscovery();
        if(turnOn)btAdapter.startDiscovery();
    }

    public static BluetoothDevice getBtDevice() {
        return btDevice;
    }

    public static void setBtDevice(BluetoothDevice btDevice) {
        BluetoothManager.btDevice = btDevice;
    }

}
