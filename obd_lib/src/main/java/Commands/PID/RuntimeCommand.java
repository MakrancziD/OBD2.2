package Commands.PID;

import Util.CommandUtil;

/**
 * Created by Maki on 2017. 04. 08..
 */

public class RuntimeCommand extends com.github.pires.obd.commands.engine.RuntimeCommand {

    public RuntimeCommand() { super(); }

    public RuntimeCommand (int frame)
    {
        super();
        this.cmd = CommandUtil.freezeFrameCommand(this.cmd, frame);
    }
}
