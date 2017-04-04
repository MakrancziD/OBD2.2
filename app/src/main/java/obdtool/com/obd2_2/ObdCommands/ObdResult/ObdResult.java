package obdtool.com.obd2_2.ObdCommands.ObdResult;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import obdtool.com.obd2_2.util.ResultCalculator;
import obdtool.com.obd2_2.enums.NRC;
import obdtool.com.obd2_2.enums.SVC;
import obdtool.com.obd2_2.enums.SubSVC;

/**
 * Created by Maki on 2017. 03. 31..
 */

public class ObdResult {
    private SVC service;
    private SubSVC subService;
    private NRC nrc;
    private String rawValue;
    private String value;

    public ObdResult(SVC svc, String response) throws ParseException {
        this.service = svc;
        List<String> splitResp = splitResponse(response);
        if(preCheck(splitResp))
        {
            if(splitResp.size()==1)
            {
                this.value="OK";
            }
            else {
                this.value = ResultCalculator.calculate(this.service, null, response);
            }
        }
    }

    public ObdResult(SVC svc, SubSVC subSvc, String response) throws ParseException {
        this.service=svc;
        this.subService=subSvc;

        List<String> splitResp = splitResponse(response);
        if(preCheck(splitResp))
        {
            this.value = ResultCalculator.calculate(this.service, this.subService, response);
        }
    }

    public ObdResult(String response) throws ParseException
    {
        List<String> splitResp = splitResponse(response);
        if(preCheck(splitResp))
        {
            this.value = ResultCalculator.calculate(this.service, this.subService, response);
        }
    }

    private List<String> splitResponse(String resp) throws ParseException {
        this.rawValue=resp;
        return Arrays.asList(resp.split("(?<=\\G.{2})"));
    }

    private boolean preCheck(List<String> responseList) throws ParseException {
        if(responseList.get(0).equals("7F"))
        {
            if(responseList.size()==3) {
                this.nrc = ResultCalculator.getNRC(responseList.get(2));
                this.value="NRC";
                return false;
            }
            else
                throw new ParseException("Invalid NRC!",0);
        }
        else
        {
            if(this.service!=null)
            {
                int intsvc = Integer.parseInt(responseList.get(0))-40;
                if(service!= null && !service.getCode().equals(Integer.toString(intsvc)))
                    throw new ParseException("Invalid service!", 0);

                if(this.subService!=null)
                {
                    if(this.subService.getCode().equals(responseList.get(1)))
                        throw new ParseException("Invalid subService", 0);
                }
                return true;
            }
            else
            {
                //if no service, find it!
                this.service=ResultCalculator.getSvc(responseList.get(0));
                if(this.service==SVC.CURRENT_DATA ||
                    this.service==SVC.FREEZEFRAME_DATA) //TODO: add other supported SVC
                {
                    //this.subService=ResultCalculator.getsubSvc(); //TODO
                }
                return true;
            }
        }
    }
}
