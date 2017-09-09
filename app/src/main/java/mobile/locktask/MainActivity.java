package mobile.locktask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.app.usage.UsageStats;
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
import java.util.ArrayList;
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
        Log.d("start","we go");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //startService(new Intent(this, WindowChangeDetectingService.class));

        if (!isServiceRunning(MyService.class,this)) {
            //startService(new Intent(this, MyService.class));
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
                ArrayList<String> forbiddenApps = new ArrayList<String>();
                forbiddenApps.add("com.android.chrome");
                forbiddenApps.add("com.google.android.apps.messaging");
                Intent startIntent = new Intent(MainActivity.this, WindowChangeDetectingService.class);
                startIntent.putStringArrayListExtra("forbiddenApps", forbiddenApps);
                startIntent.setAction("START");
                startService(startIntent);
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stopIntent = new Intent(MainActivity.this, WindowChangeDetectingService.class);
                stopIntent.setAction("STOP");
                startService(stopIntent);
            }
        });


        getApps();
    }

    public void getApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = this.getPackageManager().queryIntentActivities( mainIntent, 0);
        Log.d("length",Integer.toString(pkgAppsList.size()));
        for (ResolveInfo ri : pkgAppsList) {
            Log.d("ri name",ri.activityInfo.applicationInfo.packageName);
        }
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
