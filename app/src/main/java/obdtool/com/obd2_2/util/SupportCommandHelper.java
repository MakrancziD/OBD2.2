package obdtool.com.obd2_2.util;

import android.content.Context;

import com.github.pires.obd.commands.ObdCommand;

import java.util.ArrayList;
import java.util.List;

import Commands.PID.PIDSupport;
import Enums.PIDsupport;
import obdtool.com.obd2_2.activity.MainActivity;

/**
 * Created by Maki on 2017. 05. 02..
 */

public class SupportCommandHelper {

    private MainActivity activity;

    public static List<ObdCommand> getAllSupportedPID(MainActivity activity) {
        List<ObdCommand> out = new ArrayList<>();
        PIDSupport current = new PIDSupport(PIDsupport.PID_01_20);
        do {
            List<ObdCommand> tmp = new ArrayList<>();
            activity.ObdCommand(current);
            tmp = current.getSupportedCommands();
            //check if tmp containt support command
            out.addAll(tmp);
        } while(false);

        return out;
    }
}
