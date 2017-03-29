package obdtool.com.obd2_2.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import obdtool.com.obd2_2.R;


public class DashboardActivity extends AppCompatActivity {

    private static final String COMP = DashboardActivity.class.getName();

    private boolean serviceBound=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    public void startLiveData()
    {

    }

    private void bindService()
    {
        if(!serviceBound)
        {

        }
    }
}
