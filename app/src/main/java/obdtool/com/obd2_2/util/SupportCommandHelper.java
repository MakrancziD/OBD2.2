package obdtool.com.obd2_2.util;

import com.github.pires.obd.commands.ObdCommand;

import java.util.ArrayList;
import java.util.List;

import Commands.PID.PIDSupport;
import Enums.PIDsupport;
import obdtool.com.obd2_2.activity.MainActivity;

public class SupportCommandHelper {

    public static List<ObdCommand> getAllSupportedPID(MainActivity activity) {
        List<ObdCommand> out = new ArrayList<>();
        PIDSupport current = new PIDSupport(PIDsupport.PID_01_20);
        do {
            List<ObdCommand> tmp = new ArrayList<>();
            activity.ObdCommand(current);
            tmp = current.getSupportedCommands();
            //check if tmp contains support command
            out.addAll(tmp);
        } while(false);

        return out;
    }
}
