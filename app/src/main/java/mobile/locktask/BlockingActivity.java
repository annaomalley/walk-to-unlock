package mobile.locktask;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Anna on 9/9/17.
 */

public class BlockingActivity extends AppCompatActivity implements SensorEventListener{
    private static final float GRAVITY_THRESHOLD = 7.0f;
    private long mLastTime = 0;
    private static final long TIME_THRESHOLD_NS = 2000000000; // in nanoseconds (= 2sec)
    private boolean mUp = false;
    private int mJumpCounter = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blocking);
        Log.d("Create","cre8");

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.d("SensorChanged","sensor_chenged");
        detectJump(event.values[0], event.timestamp);
    }

    private void detectJump(float xValue, long timestamp) {
        if ((Math.abs(xValue) > GRAVITY_THRESHOLD)) {
            if(timestamp - mLastTime < TIME_THRESHOLD_NS && mUp != (xValue > 0)) {
                onJumpDetected(!mUp);
            }
            mUp = xValue > 0;
            mLastTime = timestamp;
        }
    }

    private void onJumpDetected(boolean up) {
        // we only count a pair of up and down as one successful movement
        if (up) {
            return;
        }
        mJumpCounter++;
        Log.d("ONEJUMP",Integer.toString(mJumpCounter));

        if (mJumpCounter >= 5) {
            Intent stopIntent = new Intent(BlockingActivity.this, WindowChangeDetectingService.class);
            stopIntent.setAction("STOP");
            startService(stopIntent);
        }
    }



}
