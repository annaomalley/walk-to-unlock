package mobile.locktask;

/**
 * Created by Anna Orosz on 9/9/2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class AppListActivity extends Activity
{
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        Log.d("created", "yes");
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_applist);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, R.layout.checkbox,
                R.id.checkBox1, getApps());

        ListView listView = (ListView) findViewById( R.id.listView );
        listView.setAdapter( adapter );


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                String app = (String) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + app,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    public ArrayList<String> getApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = this.getPackageManager().queryIntentActivities( mainIntent, 0);
        Log.d("length",Integer.toString(pkgAppsList.size()));
        ArrayList<String> array = new ArrayList<String>();
        for (ResolveInfo ri : pkgAppsList) {
            array.add(ri.loadLabel(this.getPackageManager()).toString());
        }
        return array;
    }
}
