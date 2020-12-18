package com.example.JU_NAVIGATOR.JU_NAVIGATOR;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class gps extends ActionBarActivity {
    AutoCompleteTextView destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route);
        Intent intent=getIntent();
        String source_t=intent.getStringExtra("source");
        final EditText source= (EditText) findViewById(R.id.source);
        source.setEnabled(false);
        //final EditText
        destination= (AutoCompleteTextView) findViewById(R.id.destination);
        source.setText(source_t);
        String[] colors={"conference_hall","confreenc_build","confreenc_room"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,colors);
        //destination = (AutoCompleteTextView) searchRef.findViewById(R.id.autoComplete);
        destination.setAdapter(adapter);


        final RadioGroup group3= (RadioGroup) findViewById(R.id.group3);



        Button back= (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String des=destination.getText().toString();
                int group_id=group3.getCheckedRadioButtonId();
                RadioButton sex= (RadioButton) findViewById(group_id);

                Intent in=new Intent();
                in.putExtra("sex",sex.getText());
                in.putExtra("source",source.getText().toString());
                in.putExtra("destination",destination.getText().toString());
                RadioGroup g1= (RadioGroup) findViewById(R.id.group1);
                RadioButton r1= (RadioButton) findViewById(g1.getCheckedRadioButtonId());
                in.putExtra("r1",r1.getText());
                RadioGroup g2= (RadioGroup) findViewById(R.id.group2);
                RadioButton r2= (RadioButton) findViewById(g2.getCheckedRadioButtonId());
                in.putExtra("r2",r2.getText());





                setResult(1,in);
                System.out.println("finshed");
                finish();
            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gps, menu);
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
