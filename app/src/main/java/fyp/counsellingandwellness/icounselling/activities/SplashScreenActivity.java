package fyp.counsellingandwellness.icounselling.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.TimerTask;
import java.util.Timer;
import android.content.Intent;

import fyp.counsellingandwellness.icounselling.R;

public class SplashScreenActivity extends AppCompatActivity {
    private int timerDelay=3000;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        timerTask = new TimerTask(){
            @Override
            public void run(){
                startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
                finish();
            }
        };
        new Timer().schedule(timerTask,timerDelay);
    }
}