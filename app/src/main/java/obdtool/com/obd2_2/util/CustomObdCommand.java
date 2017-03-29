package obdtool.com.obd2_2.util;

import com.github.pires.obd.commands.ObdCommand;

/**
 * Created by Maki on 2017. 03. 29..
 */

public class CustomObdCommand extends ObdCommand {

    public CustomObdCommand(String command) {
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

    protected void fillBuffer()
    {
        if(!super.cmd.startsWith("AF"))
        {
            super.fillBuffer();
        }
    }
}
