package Commands.PID;

import com.github.pires.obd.commands.ObdCommand;

import Enums.ObdStandard;

/**
 * Created by Maki on 2017. 04. 03..
 */

public class ObdStandardCommand extends ObdCommand {
    public ObdStandardCommand() {
        super("01 1C");
    }

    ObdStandard std;
    @Override
    protected void performCalculations() {
        std = Enums.ObdStandard.getByCode(buffer.get(2));
    }

    @Override
    public String getFormattedResult() {
        return null;
    }

    @Override
    public String getCalculatedResult() {
        return std.getStatus();
    }

    @Override
    public String getName() {
        return null;
    }
}
