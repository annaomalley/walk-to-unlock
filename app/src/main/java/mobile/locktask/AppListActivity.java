package mobile.locktask;

/**
 * Created by Anna Orosz on 9/9/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class AppListActivity extends Activity
{
    MyCustomAdapter adapter = null;
    final List<App> blockedApps  = new ArrayList<App>();

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_applist);

        adapter = new MyCustomAdapter(this, R.layout.checkbox, getApps());

        ListView listView = (ListView) findViewById( R.id.listView );
        listView.setAdapter( adapter );

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                App app = (App) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "To check " + app.getName() + ", click on the checkbox",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private class MyCustomAdapter extends ArrayAdapter<App> {

        private ArrayList<App> list;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<App> list) {
            super(context, textViewResourceId, list);
            this.list = new ArrayList<App>();
            this.list.addAll(list);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.checkbox, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        App a = new App(cb.getTag().toString(), false);
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        a.setSelected(cb.isChecked());
                        Log.d("blocked", a.getName());
                        blockedApps.add(a);
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            App a = list.get(position);
            holder.name.setText(a.getName());
            holder.name.setChecked(a.isSelected());
            holder.name.setTag(a);

            return convertView;

        }
    }

    private void checkButtonClick() {

        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                ArrayList<App> list = adapter.list;
                for(int i=0;i<list.size();i++){
                    App a = list.get(i);
                    if(a.isSelected()){
                        responseText.append("\n" + a.getName());
                    }
                }
                Log.d("blocked", "" + blockedApps.size());
                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();

            }
        });

    }

    public ArrayList<App> getApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = this.getPackageManager().queryIntentActivities( mainIntent, 0);

        ArrayList<App> array = new ArrayList<App>();
        for (ResolveInfo ri : pkgAppsList) {
            array.add(new App(ri.loadLabel(this.getPackageManager()).toString(), false));
        }
        return array;
    }
}

class App {

    String name = null;
    boolean selected = false;

    public App(String name, boolean selected) {
        super();
        this.name = name;
        this.selected = selected;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}