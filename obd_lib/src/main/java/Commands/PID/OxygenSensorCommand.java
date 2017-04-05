package Commands.PID;

import com.github.pires.obd.commands.ObdCommand;

import Util.Enum;


/**
 * Created by Maki on 2017. 04. 03..
 */

public class OxygenSensorCommand extends ObdCommand {
    public OxygenSensorCommand(Enum.oxygenSensor sensor) {
        super(sensor.toString()); //TODO!!!
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
