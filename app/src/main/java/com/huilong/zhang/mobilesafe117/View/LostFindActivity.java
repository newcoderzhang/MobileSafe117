package com.huilong.zhang.mobilesafe117.View;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.huilong.zhang.mobilesafe117.R;

public class LostFindActivity extends Activity {
    private SharedPreferences sharedPreferences;
    private ImageView imageView;
    private EditText numbersafeedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);


        boolean configed = sharedPreferences.getBoolean("configed",false);
        if(configed) {
            setContentView(R.layout.activity_lost_find);
            imageView = (ImageView) findViewById(R.id.imageview2);
            numbersafeedit = (EditText) findViewById(R.id.number_safe111);
            numbersafeedit.setText(sharedPreferences.getString("phonenumber",""));
            boolean protect = sharedPreferences.getBoolean("protect",false);
            if(protect) {
                imageView.setImageResource(R.mipmap.lock);

            }else {
                imageView.setImageResource(R.mipmap.unlock);
            }
        }else {
            startActivity(new Intent(LostFindActivity.this, Setup1Activity.class));
            finish();
        }

    }
    public void reset(View view) {
        startActivity(new Intent(LostFindActivity.this,Setup1Activity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lost_find, menu);
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
