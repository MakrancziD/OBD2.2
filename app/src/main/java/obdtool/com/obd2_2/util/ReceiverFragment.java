package obdtool.com.obd2_2.util;

import android.hardware.SensorEvent;
import android.location.Location;

import com.github.pires.obd.commands.ObdCommand;

public interface ReceiverFragment {
    public void update(ObdCommand cmd);
    public void update(Location l);
    public void update(SensorEvent e);
}
