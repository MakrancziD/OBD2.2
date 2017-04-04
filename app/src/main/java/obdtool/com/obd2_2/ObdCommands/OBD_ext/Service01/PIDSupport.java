package obdtool.com.obd2_2.ObdCommands.OBD_ext.Service01;

import com.github.pires.obd.commands.ObdCommand;

import java.util.ArrayList;
import java.util.List;

import obdtool.com.obd2_2.ObdCommands.CommandHelper;
import obdtool.com.obd2_2.enums.PIDsupport;

/**
 * Created by Maki on 2017. 04. 03..
 */

public class PIDSupport extends ObdCommand {

    String binResult= new String();
    private List<ObdCommand> supportedCommands = new ArrayList<>();

    private final PIDsupport range;

    public PIDSupport(final PIDsupport range) {
        super(range.buildObdCommand());
        this.range=range;
    }

    @Override
    protected void performCalculations() {
        if(buffer.size()==6)
        {
            for(int i=2;i<buffer.size();i++)
            {
                binResult+=Integer.toString(buffer.get(i), 2);
            }
        }
        char[] chars = binResult.toCharArray();

        for(int i=0;i<chars.length;i++)
        {
            if(chars[i]=='1')
            {
                getSupportedCommands().add(CommandHelper.getCommandByPID(i));
            }
        }
    }

    @Override
    public String getFormattedResult() {
        String out=new String();
        for(ObdCommand cmd : getSupportedCommands())
        {
            out+=cmd.getName()+"/n";
        }
        return out;
    }

    @Override
    public String getCalculatedResult() {
        return null;
    }

    @Override
    public String getName() {
        return range.getName();
    }

    public List<ObdCommand> getSupportedCommands() {
        return this.supportedCommands;
    }
}
