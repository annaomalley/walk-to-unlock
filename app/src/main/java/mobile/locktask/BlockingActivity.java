package mobile.locktask;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Anna on 9/9/17.
 */

public class BlockingActivity extends AppCompatActivity implements SensorEventListener{
    private static final float GRAVITY_THRESHOLD = 7.0f;
    private long mLastTime = 0;
    private static final long TIME_THRESHOLD_NS = 2000000000; // in nanoseconds (= 2sec)
    private boolean mUp = false;
    private int mJumpCounter;
    private boolean blockingOn = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blocking);
        Log.d("Create","cre8");
        mJumpCounter = 0;
        blockingOn = true;

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);

        if (mJumpCounter != 0) {
            TextView instructions_tv = (TextView) findViewById(R.id.instructions);
            instructions_tv.setText(Integer.toString(15-mJumpCounter));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //Log.d("SensorChanged","sensor_chenged");
        if (blockingOn) {
            detectJump(event.values[0], event.timestamp);
        }
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

        TextView instructions_tv = (TextView) findViewById(R.id.instructions);
        instructions_tv.setText(Integer.toString(15-mJumpCounter));

        if (mJumpCounter >= 15) {
            Intent stopIntent = new Intent(BlockingActivity.this, WindowChangeDetectingService.class);
            stopIntent.setAction("STOP");
            startService(stopIntent);
            blockingOn = false;
            finish();
        }
    }

    @Override
    public void onResume(){
        super.onResume();

        if (mJumpCounter != 0) {
            TextView instructions_tv = (TextView) findViewById(R.id.instructions);
            instructions_tv.setText(Integer.toString(15-mJumpCounter));
        }

    }




}
