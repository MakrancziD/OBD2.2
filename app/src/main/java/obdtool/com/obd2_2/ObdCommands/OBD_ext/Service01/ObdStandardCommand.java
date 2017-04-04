package obdtool.com.obd2_2.ObdCommands.OBD_ext.Service01;

import com.github.pires.obd.commands.ObdCommand;

/**
 * Created by Maki on 2017. 04. 03..
 */

public class ObdStandardCommand extends ObdCommand {
    public ObdStandardCommand(String command) {
        super(command);
    }

    @Override
    protected void performCalculations() {

    }

    @Override
    public String getFormattedResult() {
        return null;
    }

    @Override
    public String getCalculatedResult() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
