package com.example.JU_NAVIGATOR.JU_NAVIGATOR;

import android.app.Activity;
import android.os.Environment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class gps_trying extends Activity {

    MainActivity m1;
    //public gps_trying(MainActivity m2){
    //    m1=m2;

        //onCreate(null);
       // onCreateOptionsMenu()
    //}


    final String tpkPath1 = "/Download/gashu.tpk";
    final String extern = Environment.getExternalStorageDirectory().getPath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_trying);
        ArrayList<String> events = new ArrayList<String>();
        events.add("Birthday party");
        events.add("Epic party");
        events.add("Beach BBQ");

        ListView lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, events));








    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
