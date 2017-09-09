package mobile.locktask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.app.usage.UsageStats;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import android.content.pm.ResolveInfo;
import android.app.ActivityManager;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.provider.SyncStateContract.Constants;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.util.Log;
import java.util.logging.Handler;
import java.util.Timer;
import java.util.TimerTask;
import android.provider.Settings;
import android.content.Intent;

import android.net.Uri;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isServiceRunning(MyService.class,this)) {
            startService(new Intent(this, MyService.class));
        }
        /*new Timer().scheduleAtFixedRate(new TimerTask() {
            int iters = 0;
            @Override
            public void run() {
                //String fgName = getForegroundApp();
                iters += 1;
                //Log.d("iters",Integer.toString(iters));
            }
        }, 0, 1000);*/

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(MainActivity.this, MyService.class);
                startIntent.setAction("START");
                startService(startIntent);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(MainActivity.this, MyService.class);
                stopIntent.setAction("STOP");
                startService(stopIntent);
            }
        });

        Button button3 = (Button) findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("pressed", "yes");
                Intent myIntent = new Intent(MainActivity.this, AppListActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

    }



    public static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }







}
