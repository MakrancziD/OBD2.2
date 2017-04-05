package Commands.PID;

import java.util.Arrays;
import java.util.List;

import Util.CommandUtil;

public class LoadCommand extends com.github.pires.obd.commands.engine.LoadCommand {

    public LoadCommand() { super(); }

    public LoadCommand (int frame)
    {
        super();
        this.cmd = CommandUtil.freezeFrameCommand(this.cmd, frame);
    }
}
