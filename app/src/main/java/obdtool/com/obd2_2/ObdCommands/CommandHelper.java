package obdtool.com.obd2_2.ObdCommands;

import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.commands.SpeedCommand;
import com.github.pires.obd.commands.control.TimingAdvanceCommand;
import com.github.pires.obd.commands.engine.LoadCommand;
import com.github.pires.obd.commands.engine.MassAirFlowCommand;
import com.github.pires.obd.commands.engine.RPMCommand;
import com.github.pires.obd.commands.engine.RuntimeCommand;
import com.github.pires.obd.commands.engine.ThrottlePositionCommand;
import com.github.pires.obd.commands.fuel.FuelTrimCommand;
import com.github.pires.obd.commands.pressure.FuelPressureCommand;
import com.github.pires.obd.commands.pressure.IntakeManifoldPressureCommand;
import com.github.pires.obd.commands.temperature.AirIntakeTemperatureCommand;
import com.github.pires.obd.commands.temperature.EngineCoolantTemperatureCommand;
import com.github.pires.obd.enums.FuelTrim;

import obdtool.com.obd2_2.ObdCommands.OBD_ext.Service01.AuxInputStatusCommand;
import obdtool.com.obd2_2.ObdCommands.OBD_ext.Service01.FreezeDTC;
import obdtool.com.obd2_2.ObdCommands.OBD_ext.Service01.FuelSystemStatus;
import obdtool.com.obd2_2.ObdCommands.OBD_ext.Service01.MonitorStatus;
import obdtool.com.obd2_2.ObdCommands.OBD_ext.Service01.ObdStandardCommand;
import obdtool.com.obd2_2.ObdCommands.OBD_ext.Service01.OxygenSensor2BankCommand;
import obdtool.com.obd2_2.ObdCommands.OBD_ext.Service01.OxygenSensor4BankCommand;
import obdtool.com.obd2_2.ObdCommands.OBD_ext.Service01.OxygenSensorCommand;
import obdtool.com.obd2_2.ObdCommands.OBD_ext.Service01.PIDSupport;
import obdtool.com.obd2_2.ObdCommands.OBD_ext.Service01.SecondaryAirStatusCommand;
import obdtool.com.obd2_2.enums.PIDsupport;
import obdtool.com.obd2_2.util.Enum;

/**
 * Created by Maki on 2017. 04. 03..
 */

public class CommandHelper {

    public static ObdCommand getCommandByPID(int pid) {
        switch(pid)
        {
            case 0x01:
                return new MonitorStatus();
                break;
            case 0x02:
                return new FreezeDTC();
            break;
            case 0x03:
                return new FuelSystemStatus();
            break;
            case 0x04:
                return new LoadCommand();
            break;
            case 0x05:
                return new EngineCoolantTemperatureCommand();
            break;
            case 0x06:
                return new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_1);
            break;
            case 0x07:
                return new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_1);
            break;
            case 0x08:
                return new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_2);
            break;
            case 0x09:
                return new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_2);
            break;
            case 0x0A:
                return new FuelPressureCommand();
            break;
            case 0x0B:
                return new IntakeManifoldPressureCommand();
            break;
            case 0x0C:
                return new RPMCommand();
            break;
            case 0x0D:
                return new SpeedCommand();
            break;
            case 0x0E:
                return new TimingAdvanceCommand();
            break;
            case 0x0F:
                return new AirIntakeTemperatureCommand();
            break;
            case 0x10:
                return new MassAirFlowCommand();
            break;
            case 0x11:
                return new ThrottlePositionCommand();
            break;
            case 0x12:
                return new SecondaryAirStatusCommand();
            break;
            case 0x13:
                return new OxygenSensor2BankCommand();
            break;
            case 0x14:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_1);
            break;
            case 0x15:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_2);
            break;
            case 0x16:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_3);
            break;
            case 0x17:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_4);
            break;
            case 0x18:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_5);
            break;
            case 0x19:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_6);
            break;
            case 0x1A:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_7);
            break;
            case 0x1B:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_8);
            break;
            case 0x1C:
                return new ObdStandardCommand();
            break;
            case 0x1D:
                return new OxygenSensor4BankCommand();
            break;
            case 0x1E:
                return new AuxInputStatusCommand();
            break;
            case 0x1F:
                return new RuntimeCommand();
            break;
            case 0x20:
                return new PIDSupport(PIDsupport.PID_21_40);
            break;
        }
        return null;
    }

}
