package Commands.PID;

import Util.CommandUtil;

/**
 * Created by Maki on 2017. 04. 08..
 */

public class EngineCoolantTemperatureCommand extends com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand {

    public EngineCoolantTemperatureCommand() { super(); }

    public EngineCoolantTemperatureCommand (int frame)
    {
        super();
        this.cmd = CommandUtil.freezeFrameCommand(this.cmd, frame);
    }
}
