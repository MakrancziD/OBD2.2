package Commands.PID;

import com.github.pires.obd.commands.ObdCommand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Enums.Tests;

import static Util.CommandHelper.byteToBoolList;

/**
 * Created by Maki on 2017. 04. 03..
 */

public class MonitorStatusCommand extends ObdCommand {

    private int numOfDTCs=0;
    private boolean milOn =false;
    private boolean isCompIgn=false;
    private Map<Tests, Boolean> testMap = new HashMap<>();

    private List<Boolean> A;
    private List<Boolean> B;
    private List<Boolean> C;
    private List<Boolean> D;

    public MonitorStatusCommand() {
        super("01 01");
    }

    @Override
    protected void performCalculations() {
        A = byteToBoolList(buffer.get(2));
        B = byteToBoolList(buffer.get(3));
        C = byteToBoolList(buffer.get(4));
        D = byteToBoolList(buffer.get(5));

        milOn = A.get(7);
        numOfDTCs = buffer.get(2)%128;
        isCompIgn = B.get(3);
        for(int i=0;i<4;i++)
        {
            if(B.get(i))
            {
                getTestMap().put(Tests.getByCode(i, 0), B.get(i+4));
            }
        }
        for(int i=0;i<8;i++)
        {
            if(C.get(i))
            {
                getTestMap().put(Tests.getByCode(i, isCompIgn() ?2:1), D.get(i));
            }
        }
    }

    @Override
    public String getFormattedResult() {
        return null;
    }

    @Override
    public String getCalculatedResult() {
        StringBuilder sb = new StringBuilder();
        sb.append("MIL: ");
        sb.append(isMilOn()?"ON":"OFF");
        sb.append("/n");
        sb.append("DTC: ");
        sb.append(numOfDTCs);
        sb.append("/n");
        sb.append("Tests:");
        sb.append("/n");
        for(Map.Entry<Tests, Boolean> t : testMap.entrySet())
        {
            sb.append(t.getKey().getTest());
            sb.append(" - ");
            sb.append(t.getValue()?"NOK":"OK");
            sb.append("/n");
        }

        return sb.toString();
    }

    @Override
    public String getName() {
        return null;
    }

    public int getNumOfDTCs() {
        return numOfDTCs;
    }

    public boolean isMilOn() {
        return milOn;
    }

    public boolean isCompIgn() {
        return isCompIgn;
    }

    public Map<Tests, Boolean> getTestMap() {
        return testMap;
    }

    public void setTestMap(Map<Tests, Boolean> testMap) {
        this.testMap = testMap;
    }
}
