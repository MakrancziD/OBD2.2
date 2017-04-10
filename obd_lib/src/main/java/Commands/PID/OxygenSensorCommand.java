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

    private double voltage;
    private double trim;

    @Override
    protected void performCalculations() {
        int A = Integer.parseInt(Integer.toString(buffer.get(2), 10));
        int B = Integer.parseInt(Integer.toString(buffer.get(2), 10));

        voltage = A/200;
        trim = (B/1.28)-100;
    }

    @Override
    public String getFormattedResult() {
        return null;
    }

    @Override
    public String getCalculatedResult() {
        return voltage +"V "+trim+"%";
    }

    @Override
    public String getName() {
        return null;
    }
}
