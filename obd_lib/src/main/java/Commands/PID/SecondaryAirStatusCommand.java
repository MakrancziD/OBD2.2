package Commands.PID;

import com.github.pires.obd.commands.ObdCommand;

import Enums.SecondaryAirStatus;

/**
 * Created by Maki on 2017. 04. 03..
 */

public class SecondaryAirStatusCommand extends ObdCommand {
    public SecondaryAirStatusCommand() {
        super("01 12");
    }

    SecondaryAirStatus status;

    @Override
    protected void performCalculations() {
        status = SecondaryAirStatus.getByCode(buffer.get(2));
    }

    @Override
    public String getFormattedResult() {
        return null;
    }

    @Override
    public String getCalculatedResult() {
        return status.getStatus();
    }

    @Override
    public String getName() {
        return null;
    }
}
