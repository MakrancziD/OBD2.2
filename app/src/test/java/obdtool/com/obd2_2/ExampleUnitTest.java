package obdtool.com.obd2_2;

import com.github.pires.obd.commands.ObdCommand;

import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Commands.PID.SpeedCommand;
import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.db.DbHelper;
import obdtool.com.obd2_2.db.Model.Trip;
import obdtool.com.obd2_2.db.Model.Vehicle;

import static android.R.attr.value;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void setTripVehicle() {
        Vehicle v = new Vehicle("Name", "Make", "Model");
        DbHandler.setCurrentVehicle(v);
        assertThat(v, equalTo(DbHandler.getCurrentVehicle()));
    }

    @Test
    public void speedCommandTest() {
        SpeedCommand spd = new SpeedCommand();
        commandHelper(spd, "41 1D 22");
        assertThat(spd.getCalculatedResult(), equalTo("34"));
    }

    private ObdCommand commandHelper(ObdCommand cmd, String response) {
        try {
            List<Integer> e = new ArrayList<>();
            for(int i : response.toCharArray()) {
                e.add(i);
            }
            e.add(-1);

            InputStream obdResponse = org.mockito.Mockito.mock(InputStream.class);
            when(obdResponse.read()).thenAnswer(AdditionalAnswers.returnsElementsOf(e));

            Method readResult = ObdCommand.class.getDeclaredMethod("readResult", InputStream.class);
            readResult.setAccessible(true);
            readResult.invoke(cmd, obdResponse);

        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException | IOException e) {
            e.printStackTrace();
        }
        return cmd;
    }
}