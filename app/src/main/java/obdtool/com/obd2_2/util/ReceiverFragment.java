package obdtool.com.obd2_2.util;

import android.hardware.SensorEvent;
import android.location.Location;

import com.github.pires.obd.commands.ObdCommand;

/**
 * Created by Maki on 2017. 03. 30..
 */

public interface ReceiverFragment {
    public void update(ObdCommand cmd);
    public void update(Location l);
    public void update(SensorEvent e);
}
