package Commands.PID;

import com.github.pires.obd.commands.ObdCommand;

/**
 * Created by makra on 2017. 04. 10..
 */

public class DistanceTraveledMilOnCommand extends ObdCommand {
    public DistanceTraveledMilOnCommand() {
        super("01 21");
    }

    private int distance;

    @Override
    protected void performCalculations() {
        int A = buffer.get(2);
        int B = buffer.get(3);

        distance = 256*A+B;
    }

    @Override
    public String getFormattedResult() {
        return null;
    }

    @Override
    public String getCalculatedResult() {
        return String.valueOf(distance);
    }

    @Override
    public String getName() {
        return null;
    }
}
