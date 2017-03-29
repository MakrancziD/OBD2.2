package obdtool.com.obd2_2.util;

import com.github.pires.obd.commands.ObdCommand;

/**
 * Created by Maki on 2017. 03. 25..
 */

public class ObdCommandJob {

    private ObdCommand command;
    private ObdCommandJobStatus status;

    public ObdCommandJob(ObdCommand cmd)
    {
        this.setCommand(cmd);
        this.status=ObdCommandJobStatus.NEW;
    }

    public ObdCommandJobStatus getStatus() {
        return status;
    }

    public void setStatus(ObdCommandJobStatus status) {
        this.status = status;
    }

    public ObdCommand getCommand() {
        return command;
    }

    public void setCommand(ObdCommand command) {
        this.command = command;
    }

    public enum ObdCommandJobStatus {
        NEW,
        RUNNING,
        FINISHED,
        ERROR
    }
}
