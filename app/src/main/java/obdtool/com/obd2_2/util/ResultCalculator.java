package obdtool.com.obd2_2.util;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import obdtool.com.obd2_2.ObdCommands.ObdResult.ObdResult;
import obdtool.com.obd2_2.enums.NRC;
import obdtool.com.obd2_2.enums.SVC;
import obdtool.com.obd2_2.enums.SubSVC;

/**
 * Created by Maki on 2017. 03. 31..
 */

public class ResultCalculator {

//    public static ObdResult calculate(String response) throws ParseException {
//        List<String> responseList = Arrays.asList(response.split("(?<=\\G.{2})"));
//        String service=null;
//        String subService=null;
//        String value=null;
//
//    }

    public static String calculate(SVC svc, SubSVC subSvc, String rawData)
    {
        if(subSvc!=null)
        {
            switch(subSvc)
            {
                case SUPPORT_00:
            }
        }

        return "?";
    }

    public static NRC getNRC(String nrc) throws ParseException {
        switch(nrc)
        {
            case "10":
                return NRC.GR;
            case "11":
                return NRC.SNS;
            case "12":
                return NRC.SFNSIF;
            case "21":
                return NRC.BRR;
            case "22":
                return NRC.CNCORSE;
            case "78":
                return NRC.RCRRP;
        }
        throw new ParseException("Invalid NRC code",3);
    }

    public static SVC getSvc(String svc) throws ParseException
    {
        switch(svc)
        {
            case "01":
            case "41":
                return SVC.CURRENT_DATA;
            case "02":
            case "42":
                return SVC.FREEZEFRAME_DATA;
            case "03":
            case "43":
                return SVC.DTC_LIST;
            case "04":
            case "44":
                return SVC.DTC_CLEAR;
            case "05":
            case "45":
                return SVC.O2_TESTS;
            case "06":
            case "46":
                return SVC.OTHER_TESTS;
            case "07":
            case "47":
                return SVC.PENDING_DTC;
            case "09":
            case "49":
                return SVC.VEHICLE_INFO;
            case "0A":
            case "4A":
                return SVC.PERMANENT_DTC;

        }
        throw new ParseException("Invalid SVC code",3);
    }

    private static List<SubSVC> calculateSupport(String response)
    {
        return null;
    }
}
