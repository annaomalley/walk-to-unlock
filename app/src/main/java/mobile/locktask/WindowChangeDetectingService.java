package mobile.locktask;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.accessibility.AccessibilityEvent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ActivityInfo;
import android.util.Log;

/**
 * Created by Anna on 9/9/17.
 */

public class WindowChangeDetectingService extends AccessibilityService {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("making fg...","make");
        final Intent emptyIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.foreground_running)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setContentIntent(pendingIntent);
        startForeground(1, mBuilder.build());
        return super.onStartCommand(intent, flags, startId);

    }

        @Override
        protected void onServiceConnected() {
            Log.d("service connected","Yes");
            super.onServiceConnected();

            //Configure these here for compatibility with API 13 and below.
            AccessibilityServiceInfo config = new AccessibilityServiceInfo();
            config.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
            config.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;

            if (Build.VERSION.SDK_INT >= 16)
                //Just in case this helps
                config.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS;

            setServiceInfo(config);
        }

        @Override
        public void onAccessibilityEvent(AccessibilityEvent event) {
            Log.d("Access event","access event");
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                if (event.getPackageName() != null && event.getClassName() != null) {
                    ComponentName componentName = new ComponentName(
                            event.getPackageName().toString(),
                            event.getClassName().toString()
                    );
                    Log.i("CurrentActivity", event.getPackageName().toString());
                    if ("com.android.chrome".equals(event.getPackageName().toString())) {
                        showHomeScreen();
                    }


                    ActivityInfo activityInfo = tryGetActivity(componentName);
                    boolean isActivity = activityInfo != null;
                    if (isActivity)
                        Log.i("CurrentActivity", componentName.flattenToShortString());
                    else {
                        Log.d("UGH","not working");
                    }
                }
            }
        }

        private ActivityInfo tryGetActivity(ComponentName componentName) {
            try {
                return getPackageManager().getActivityInfo(componentName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                return null;
            }
        }

        @Override
        public void onInterrupt() {
        }

    public boolean showHomeScreen(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(startMain);
        return true;
    }

}
