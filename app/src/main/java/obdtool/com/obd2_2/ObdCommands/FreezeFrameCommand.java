package obdtool.com.obd2_2.ObdCommands;

import com.github.pires.obd.commands.ObdCommand;

/**
 * Created by Maki on 2017. 04. 04..
 */

public abstract class FreezeFrameCommand extends ObdCommand {

    private ObdCommand baseCommand;

    public FreezeFrameCommand(ObdCommand other) {
        super(other);
        this.baseCommand=other;
        baseCommand.
    }
}
