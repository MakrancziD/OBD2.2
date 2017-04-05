package Commands.PID;

import com.github.pires.obd.commands.ObdCommand;

/**
 * Created by Maki on 2017. 04. 03..
 */

public class OxygenSensor4BankCommand extends ObdCommand {
    public OxygenSensor4BankCommand(String command) {
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
