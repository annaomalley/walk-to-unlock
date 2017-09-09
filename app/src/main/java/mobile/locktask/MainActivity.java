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
import android.app.ActivityManager;
import android.widget.Toast;
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


        new Timer().scheduleAtFixedRate(new TimerTask() {
            int iters = 0;
            @Override
            public void run() {
                //String fgName = getForegroundApp();
                iters += 1;
                //Log.d("iters",Integer.toString(iters));
            }
        }, 0, 1000);

    }



    public String getForegroundApp() {
        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
        String currentApp = tasks.get(0).processName;
        return currentApp;
    }

    public String getForegroundApp2() {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
// The first in the list of RunningTasks is always the foreground task.
        RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);

        String foregroundTaskPackageName = foregroundTaskInfo .topActivity.getPackageName();
        PackageManager pm = this.getPackageManager();
        String foregroundTaskAppName = "none";
        try {
            PackageInfo foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
            foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();

        }
        catch (PackageManager.NameNotFoundException e) {

        }

        return foregroundTaskAppName;
    }



}
