package Commands.PID;

import Util.CommandUtil;

/**
 * Created by Maki on 2017. 04. 08..
 */

public class ThrottlePositionCommand extends com.github.pires.obd.commands.engine.ThrottlePositionCommand {

    public ThrottlePositionCommand() { super(); }

    public ThrottlePositionCommand (int frame)
    {
        super();
        this.cmd = CommandUtil.freezeFrameCommand(this.cmd, frame);
    }
}
