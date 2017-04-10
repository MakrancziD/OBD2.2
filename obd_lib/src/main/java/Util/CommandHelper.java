package Util;

import com.github.pires.obd.commands.ObdCommand;
import com.github.pires.obd.enums.FuelTrim;

import java.util.ArrayList;
import java.util.List;

import Commands.PID.AirIntakeTemperatureCommand;
import Commands.PID.AuxInputStatusCommand;
import Commands.PID.EngineCoolantTemperatureCommand;
import Commands.PID.FreezeDTCCommand;
import Commands.PID.FuelPressureCommand;
import Commands.PID.FuelSystemStatusCommand;
import Commands.PID.FuelTrimCommand;
import Commands.PID.IntakeManifoldPressureCommand;
import Commands.PID.LoadCommand;
import Commands.PID.MassAirFlowCommand;
import Commands.PID.MonitorStatusCommand;
import Commands.PID.ObdStandardCommand;
import Commands.PID.OxygenSensor2BankCommand;
import Commands.PID.OxygenSensor4BankCommand;
import Commands.PID.OxygenSensorCommand;
import Commands.PID.PIDSupport;
import Commands.PID.RPMCommand;
import Commands.PID.RuntimeCommand;
import Commands.PID.SecondaryAirStatusCommand;
import Commands.PID.SpeedCommand;
import Commands.PID.ThrottlePositionCommand;
import Commands.PID.TimingAdvanceCommand;
import Enums.PIDsupport;

/**
 * Created by Maki on 2017. 04. 03..
 */

public class CommandHelper {

    public static ObdCommand getCommandByPID(int pid) {
        switch(pid)
        {
            case 0x01:
                return new MonitorStatusCommand();
            case 0x02:
                return new FreezeDTCCommand();
            case 0x03:
                return new FuelSystemStatusCommand();
            case 0x04:
                return new LoadCommand();
            case 0x05:
                return new EngineCoolantTemperatureCommand();
            case 0x06:
                return new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_1);
            case 0x07:
                return new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_1);
            case 0x08:
                return new FuelTrimCommand(FuelTrim.SHORT_TERM_BANK_2);
            case 0x09:
                return new FuelTrimCommand(FuelTrim.LONG_TERM_BANK_2);
            case 0x0A:
                return new FuelPressureCommand();
            case 0x0B:
                return new IntakeManifoldPressureCommand();
            case 0x0C:
                return new RPMCommand();
            case 0x0D:
                return new SpeedCommand();
            case 0x0E:
                return new TimingAdvanceCommand();
            case 0x0F:
                return new AirIntakeTemperatureCommand();
            case 0x10:
                return new MassAirFlowCommand();
            case 0x11:
                return new ThrottlePositionCommand();
            case 0x12:
                return new SecondaryAirStatusCommand();
            case 0x13:
                return new OxygenSensor2BankCommand();
            case 0x14:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_1);
            case 0x15:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_2);
            case 0x16:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_3);
            case 0x17:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_4);
            case 0x18:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_5);
            case 0x19:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_6);
            case 0x1A:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_7);
            case 0x1B:
                return new OxygenSensorCommand(Enum.oxygenSensor.OXYGEN_SENSOR_8);
            case 0x1C:
                return new ObdStandardCommand();
            case 0x1D:
                return new OxygenSensor4BankCommand();
            case 0x1E:
                return new AuxInputStatusCommand();
            case 0x1F:
                return new RuntimeCommand();
            case 0x20:
                return new PIDSupport(PIDsupport.PID_21_40);
        }
        return null;
    }

    public static List<Boolean> byteToBoolList(int a)
    {
        List<Boolean> out = new ArrayList<>();
        String s = String.format("%8b", a);
        for(char c : s.toCharArray())
        {
            out.add(c=='1');
        }
        return out;
    }

}
