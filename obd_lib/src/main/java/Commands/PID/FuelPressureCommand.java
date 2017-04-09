package Commands.PID;

import Util.CommandUtil;

/**
 * Created by Maki on 2017. 04. 08..
 */

public class FuelPressureCommand extends com.github.pires.obd.commands.pressure.FuelPressureCommand {

    public FuelPressureCommand() { super(); }

    public FuelPressureCommand (int frame)
    {
        super();
        this.cmd = CommandUtil.freezeFrameCommand(this.cmd, frame);
    }
}
