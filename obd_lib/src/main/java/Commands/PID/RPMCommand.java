package Commands.PID;

import Util.CommandUtil;

/**
 * Created by Maki on 2017. 04. 08..
 */

public class RPMCommand extends com.github.pires.obd.commands.engine.RPMCommand {

    public RPMCommand() { super(); }

    public RPMCommand (int frame)
    {
        super();
        this.cmd = CommandUtil.freezeFrameCommand(this.cmd, frame);
    }
}
