package mobile.locktask;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.support.annotation.Nullable;
import android.os.IBinder;
import android.content.Intent;
import android.util.Log;
import android.app.usage.UsageStatsManager;
import android.app.usage.UsageStats;
import java.util.SortedMap;
import java.util.List;
import java.util.Timer;
import java.util.TreeMap;
import java.util.TimerTask;
import android.support.v4.app.NotificationCompat;
import android.app.PendingIntent;
import android.widget.Toast;
import android.app.ActivityManager.RunningTaskInfo;

/**
 * Created by Anna on 9/8/17.
 */

public class MyService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("working?","yes");
        final Intent emptyIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.foreground_running)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setContentIntent(pendingIntent);
        startForeground(1,mBuilder.build());
        new Timer().scheduleAtFixedRate(new TimerTask() {
            int iters = 0;
            @Override
            public void run() {
                iters += 1;
                Log.d("iters",Integer.toString(iters));
                String currentApp = getForegroundApp();
                if("com.android.chrome".equals(currentApp)){
                    showHomeScreen();
                }


            }
        }, 0, 1000);

        return super.onStartCommand(intent, flags, startId);

    }

    /*public static boolean isAppRunning(final Context context, final String packageName) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        if (procInfos != null)
        {
            for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                if (processInfo.processName.equals(packageName)) {
                    return true;
                }
                else {
                    Log.d("processName",processInfo.processName);
                }
            }
        }
        return false;
    }*/

    public String getForegroundApp() {
        String currentApp = "nah";
        UsageStatsManager usm = (UsageStatsManager)this.getSystemService("usagestats");
        long time = System.currentTimeMillis();
        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - 1000*1000, time);
        if (appList != null && appList.size() > 0) {
            SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
            }
            if (mySortedMap != null && !mySortedMap.isEmpty()) {
                currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
            }
        }
        Log.d("currentApp",currentApp);
        return currentApp;
    }

    public boolean showHomeScreen(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(startMain);
        return true;
    }


}
