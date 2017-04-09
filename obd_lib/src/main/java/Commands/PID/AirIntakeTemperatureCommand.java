package Commands.PID;

import Util.CommandUtil;

/**
 * Created by Maki on 2017. 04. 08..
 */

public class AirIntakeTemperatureCommand extends com.github.pires.obd.commands.temperature.AirIntakeTemperatureCommand {

    public AirIntakeTemperatureCommand() { super(); }

    public AirIntakeTemperatureCommand (int frame)
    {
        super();
        this.cmd = CommandUtil.freezeFrameCommand(this.cmd, frame);
    }
}
