package obdtool.com.obd2_2.ObdCommands.OBD_ext.Service01;

import com.github.pires.obd.commands.ObdCommand;

import obdtool.com.obd2_2.util.Enum;

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
