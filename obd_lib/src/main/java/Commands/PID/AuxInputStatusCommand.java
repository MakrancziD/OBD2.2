package Commands.PID;

import com.github.pires.obd.commands.ObdCommand;

/**
 * Created by Maki on 2017. 04. 03..
 */

public class AuxInputStatusCommand extends ObdCommand {

    public AuxInputStatusCommand() {
        super("01 1E");
    }

    boolean PTOstatus=false;
    @Override
    protected void performCalculations() {
        PTOstatus = (buffer.get(2) & 128)>0;
    }

    @Override
    public String getFormattedResult() {
        return null;
    }

    @Override
    public String getCalculatedResult() {
        return Boolean.toString(PTOstatus);
    }

    @Override
    public String getName() {
        return null;
    }
}
