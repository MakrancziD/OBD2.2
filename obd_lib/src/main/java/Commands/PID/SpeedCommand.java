package Commands.PID;

import Util.CommandUtil;

/**
 * Created by Maki on 2017. 04. 08..
 */

public class SpeedCommand extends com.github.pires.obd.commands.SpeedCommand {
    public SpeedCommand() { super(); }

    public SpeedCommand (int frame)
    {
        super();
        this.cmd = CommandUtil.freezeFrameCommand(this.cmd, frame);
    }
}
