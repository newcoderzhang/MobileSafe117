package com.huilong.zhang.mobilesafe117.View;

import android.app.Activity;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.huilong.zhang.mobilesafe117.R;
import com.huilong.zhang.mobilesafe117.dao.AddressDao;

public class AddressActivity extends Activity {
    private static final String TAG = "AddressActivity";
    private EditText editTextnumber;
    private TextView textViewresult;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        editTextnumber = (EditText) findViewById(R.id.et_phone_number);
        textViewresult = (TextView) findViewById(R.id.research_result);
        editTextnumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.v(TAG, "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.v(TAG, "onTextChanged s is " + s);
                result = AddressDao.getAddress(s.toString());

                setstyle();

                textViewresult.setText(result);


            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.v(TAG, "afterTextChanged");

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_address, menu);
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
    public void serch(View view) {
        //添加判断空字符
        String tempresult = editTextnumber.getText().toString().trim();
        if(!TextUtils.isEmpty(tempresult)) {
            result = AddressDao.getAddress(tempresult);
            textViewresult.setText(result);
        }else {
            Animation shake = AnimationUtils.loadAnimation(this,R.anim.shake);
            textViewresult.startAnimation(shake);


        }
    }
    public void setstyle() {
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);//差补器
        textViewresult.startAnimation(shake);
        vibrate();

    }
    private void vibrate() {
        Vibrator vibrat = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrat.vibrate(new long[] { 1000, 2000, 1000, 3000 }, -1);
    }
}
