package obdtool.com.obd2_2.util;

import com.github.pires.obd.commands.ObdCommand;

public class CustomObdCommand extends ObdCommand {

    private String rawCommand;

    public CustomObdCommand(String command) {
        super(command);
        rawCommand=command;
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
        return rawCommand;
    }

    protected void fillBuffer()
    {
        if(!super.cmd.startsWith("AT"))
        {
            super.fillBuffer();
        }
    }
}
