package obdtool.com.obd2_2.util;

import com.github.pires.obd.commands.ObdCommand;

import java.text.ParseException;

import obdtool.com.obd2_2.ObdCommands.ObdResult.ObdResult;
import obdtool.com.obd2_2.enums.SVC;
import obdtool.com.obd2_2.enums.SubSVC;

/**
 * Created by Maki on 2017. 03. 29..
 */

public class CustomObdCommand extends ObdCommand {

    private SVC svc;
    private SubSVC subSvc;
    private String params;
    private String rawCommand;

    public CustomObdCommand(String command) {
        super(command);
        rawCommand=command;
    }

    public CustomObdCommand(SVC svc)
    {
        super(svc.getCode());
        this.rawCommand=svc.getCode();
        this.svc=svc;
    }

    public CustomObdCommand(SVC svc, SubSVC subSvc)
    {
        super(svc.getCode()+subSvc.getCode());
        this.rawCommand=svc.getCode()+subSvc.getCode();
        this.svc=svc;
        this.subSvc=subSvc;
    }

    public CustomObdCommand(SVC svc, SubSVC subSvc, String params)
    {
        super(svc.getCode()+subSvc.getCode()+params);
        this.rawCommand=svc.getCode()+subSvc.getCode()+params;
        this.svc=svc;
        this.subSvc=subSvc;
        this.params=params;
    }

    public ObdResult getObdResult() throws ParseException
    {
        if(svc!=null)
        {
            if(subSvc!=null)
            {
                return new ObdResult(svc, subSvc, rawData);
            }
            else
            {
                return new ObdResult(svc, rawData);
            }
        }
        else
        {
            return new ObdResult(rawData);
        }

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
        String out=null;
        if(svc!=null)
        {
            out=svc.getValue();
            if(subSvc!=null)
            {
                out+=" - "+subSvc.getValue();
            }
        }
        if(out!=null)
        {
            return out;
        }
        return rawCommand;
    }

    protected void fillBuffer()
    {
        if(!super.cmd.startsWith("AF"))
        {
            super.fillBuffer();
        }
    }
}
