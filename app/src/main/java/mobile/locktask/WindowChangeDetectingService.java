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
import android.os.Handler;

/**
 * Created by Anna on 9/9/17.
 */

public class WindowChangeDetectingService extends AccessibilityService {

    private boolean blockingOn = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {



        if(intent.getAction() != null && intent.getAction().equals("STOP")) {
            blockingOff();
        }

        else if (!blockingOn) {

            blockingOn = true;

            Log.d("making fg...", "make");
            final Intent emptyIntent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.foreground_running)
                            .setContentTitle("LockTask is blocking your apps")
                            .setContentText("Work out to unlock")
                            .setContentIntent(pendingIntent);
            startForeground(1, mBuilder.build());



            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    blockingOff();
                }
            }, 20 * 1000);

            Intent dialogIntent = new Intent(this, BlockingActivity.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);

        }

        return super.onStartCommand(intent, flags, startId);

    }


        @Override
        public void onAccessibilityEvent(AccessibilityEvent event) {
            if (blockingOn) {
                Log.d("Access event", "access event");
                if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                    if (event.getPackageName() != null && event.getClassName() != null) {
                        Log.i("CurrentActivity", event.getPackageName().toString());
                        if ("com.android.chrome".equals(event.getPackageName().toString())) {
                            showHomeScreen();
                        }


                    }
                }
            }
        }

        @Override
        public void onInterrupt() {
        }

    private void blockingOff() {
        blockingOn = false;
        Intent dialogIntent = new Intent(this, MainActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
    }

    public boolean showHomeScreen(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(startMain);
        return true;
    }

}
