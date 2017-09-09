package mobile.locktask;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Anna on 9/8/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("worr","yes");
        startService(new Intent(this, MyService.class));
    }
}
