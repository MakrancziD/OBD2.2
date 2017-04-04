package obdtool.com.obd2_2.ObdCommands;

import com.github.pires.obd.commands.ObdCommand;

/**
 * Created by Maki on 2017. 04. 03..
 */

public abstract class BaseCommand extends ObdCommand {

    public BaseCommand(String command) {
        super(command);
    }
}
