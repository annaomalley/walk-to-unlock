package mobile.locktask;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.support.annotation.Nullable;
import android.os.IBinder;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
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
        new Timer().scheduleAtFixedRate(new TimerTask() {
            int iters = 0;
            @Override
            public void run() {
                iters += 1;
                Log.d("iters",Integer.toString(iters));
                String foregroundAppName = getForegroundApp();
                Log.d("foregroundAppName",foregroundAppName);

            }
        }, 0, 1000);

        return super.onStartCommand(intent, flags, startId);

    }

    public String getForegroundApp() {
        //ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        //List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
// The first in the list of RunningTasks is always the foreground task.
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);

        for (ActivityManager.RunningTaskInfo task:tasks) {
            Log.d("taskName",task);
        }
        String currentApp = tasks.get(tasks.size() - 1).processName;
        return currentApp;
    }

}
