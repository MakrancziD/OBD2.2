package Commands.PID;

import com.github.pires.obd.commands.ObdCommand;

import Enums.FuelSysStat;

/**
 * Created by Maki on 2017. 04. 03..
 */

public class FuelSystemStatusCommand extends ObdCommand {

    private FuelSysStat status1;
    private FuelSysStat status2;

    public FuelSystemStatusCommand() {
        super("01 03");
    }

    @Override
    protected void performCalculations() {
        status1 = FuelSysStat.getByCode(buffer.get(2));
        if(buffer.get(3)!=0)
        {
            status2 = FuelSysStat.getByCode(buffer.get(3));
        }
    }

    @Override
    public String getFormattedResult() {
        return null;
    }

    @Override
    public String getCalculatedResult() {
        StringBuilder sb = new StringBuilder();
        sb.append(status1.getStatus());
        if(status2!=null)
        {
            sb.append("/n+");
            sb.append(status2.getStatus());
        }
        return sb.toString();
    }

    @Override
    public String getName() {
        return null;
    }
}
