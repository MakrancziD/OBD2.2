package Util;

import com.github.pires.obd.commands.ObdCommand;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Maki on 2017. 04. 04..
 */

public class CommandUtil {

    public static String freezeFrameCommand(String cmd, int frame)
    {
        List<String> commandList = Arrays.asList(cmd.split(" "));
        return "02"+commandList.get(1)+String.format("%02X", frame);
    }
}
