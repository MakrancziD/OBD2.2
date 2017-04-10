package Commands.PID;

import com.github.pires.obd.commands.ObdCommand;

/**
 * Created by Maki on 2017. 04. 03..
 */

public class FreezeDTCCommand extends ObdCommand {
    public FreezeDTCCommand() {
        super("01 02");
    }

    /** Constant <code>dtcLetters={'P', 'C', 'B', 'U'}</code> */
    protected final static char[] dtcLetters = {'P', 'C', 'B', 'U'};
    /** Constant <code>hexArray="0123456789ABCDEF".toCharArray()</code> */
    protected final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    String FFdtc="";

    @Override
    protected void performCalculations() {
        final String result = getResult();
        String workingData;
        int startIndex = 0;//Header size.

        String canOneFrame = result.replaceAll("[\r\n]", "");
        int canOneFrameLength = canOneFrame.length();
        if (canOneFrameLength <= 16 && canOneFrameLength % 4 == 0) {//CAN(ISO-15765) protocol one frame.
            workingData = canOneFrame;//43yy{codes}
            startIndex = 4;//Header is 43yy, yy showing the number of data items.
        } else if (result.contains(":")) {//CAN(ISO-15765) protocol two and more frames.
            workingData = result.replaceAll("[\r\n].:", "");//xxx43yy{codes}
            startIndex = 7;//Header is xxx43yy, xxx is bytes of information to follow, yy showing the number of data items.
        } else {//ISO9141-2, KWP2000 Fast and KWP2000 5Kbps (ISO15031) protocols.
            workingData = result.replaceAll("^43|[\r\n]43|[\r\n]", "");
        }
            String dtc = "";
            byte b1 = hexStringToByteArray(workingData.charAt(startIndex));
            int ch1 = ((b1 & 0xC0) >> 6);
            int ch2 = ((b1 & 0x30) >> 4);
            dtc += dtcLetters[ch1];
            dtc += hexArray[ch2];
            dtc += workingData.substring(startIndex+1, startIndex + 4);
            if (dtc.equals("P0000")) {
                return;
            }
            FFdtc = dtc;
    }

    private byte hexStringToByteArray(char s) {
        return (byte) ((Character.digit(s, 16) << 4));
    }

    @Override
    public String getFormattedResult() {
        return null;
    }

    @Override
    public String getCalculatedResult() {
        return FFdtc;
    }

    @Override
    public String getName() {
        return null;
    }
}
