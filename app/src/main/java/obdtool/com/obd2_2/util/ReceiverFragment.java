package obdtool.com.obd2_2.util;

import com.github.pires.obd.commands.ObdCommand;

/**
 * Created by Maki on 2017. 03. 30..
 */

public interface ReceiverFragment {
    public void update(ObdCommand cmd);
}
