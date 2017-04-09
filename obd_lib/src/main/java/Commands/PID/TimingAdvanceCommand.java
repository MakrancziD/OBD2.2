package Commands.PID;

import Util.CommandUtil;

/**
 * Created by Maki on 2017. 04. 08..
 */

public class TimingAdvanceCommand extends com.github.pires.obd.commands.control.TimingAdvanceCommand {

    public TimingAdvanceCommand() { super(); }

    public TimingAdvanceCommand (int frame)
    {
        super();
        this.cmd = CommandUtil.freezeFrameCommand(this.cmd, frame);
    }
}
