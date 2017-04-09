package Commands.PID;

import com.github.pires.obd.enums.FuelTrim;

import Util.CommandUtil;

/**
 * Created by Maki on 2017. 04. 08..
 */

public class FuelTrimCommand extends com.github.pires.obd.commands.fuel.FuelTrimCommand {

    public FuelTrimCommand(FuelTrim trim) { super(trim); }

    public FuelTrimCommand (FuelTrim trim, int frame)
    {
        super(trim);
        this.cmd = CommandUtil.freezeFrameCommand(this.cmd, frame);
    }
}
