package Commands.PID;

import Util.CommandUtil;

/**
 * Created by Maki on 2017. 04. 08..
 */

public class MassAirFlowCommand extends com.github.pires.obd.commands.engine.MassAirFlowCommand {

    public MassAirFlowCommand() { super(); }

    public MassAirFlowCommand (int frame)
    {
        super();
        this.cmd = CommandUtil.freezeFrameCommand(this.cmd, frame);
    }
}
