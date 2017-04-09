package Commands.PID;

import Util.CommandUtil;

/**
 * Created by Maki on 2017. 04. 08..
 */

public class IntakeManifoldPressureCommand extends com.github.pires.obd.commands.pressure.IntakeManifoldPressureCommand {

    public IntakeManifoldPressureCommand() { super(); }

    public IntakeManifoldPressureCommand (int frame)
    {
        super();
        this.cmd = CommandUtil.freezeFrameCommand(this.cmd, frame);
    }
}
