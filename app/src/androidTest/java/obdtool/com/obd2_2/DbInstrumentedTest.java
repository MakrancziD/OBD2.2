package obdtool.com.obd2_2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import obdtool.com.obd2_2.db.DbHandler;
import obdtool.com.obd2_2.db.Model.Trip;
import obdtool.com.obd2_2.db.Model.Vehicle;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;


/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DbInstrumentedTest {

    @Test
    public void dbTripDefaultVehicleTest() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        DbHandler.initDb(appContext);
        DbHandler.startTrip();
        Vehicle v = DbHandler.getCurrentVehicle();
        assertThat(v.getName(), equalTo("default"));
        DbHandler.endTrip();
    }

    @Test
    public void dbTripTest() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        DbHandler.initDb(appContext);
        Trip t = DbHandler.getCurrentTrip();
        assertThat(t, equalTo(null));
        long pre = new Date().getTime();
        DbHandler.startTrip();
        t = DbHandler.getCurrentTrip();
        assertThat(t, not(equalTo(null)));
        DbHandler.endTrip();
        long post = new Date().getTime();
        long start = t.getStart_time().getTime();
        long end = t.getEnd_time().getTime();
        assertThat(start, greaterThanOrEqualTo(pre));
        assertThat(end, lessThanOrEqualTo(post));
    }
}
