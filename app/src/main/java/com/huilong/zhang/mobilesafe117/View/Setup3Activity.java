package com.huilong.zhang.mobilesafe117.View;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.huilong.zhang.mobilesafe117.R;

public class Setup3Activity extends Activity {
    private static final String TAG ="Setup3Activity" ;
    private EditText editPhone;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);
        editPhone = (EditText) findViewById(R.id.number);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup3, menu);
        return true;
    }
    public void select(View v) {
        //Toast.makeText(this,"xuanze",Toast.LENGTH_SHORT).show();
        startActivityForResult(new Intent(this, ContactActivity.class), 1);
    }
    public void next(View viw) {
        if(TextUtils.isEmpty(editPhone.getText())) {
            Toast.makeText(Setup3Activity.this,"号码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        sharedPreferences.edit().putString("phonenumber",editPhone.getText().toString()).commit();
        startActivity(new Intent(Setup3Activity.this, Setup4Activity.class));
        finish();

    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     * <p>This method is never invoked if your activity sets
     * {@link android.R.styleable#AndroidManifestActivity_noHistory noHistory} to
     * <code>true</code>.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String phone = data.getStringExtra("phone");
            Log.v(TAG, "phone = " + phone);
            //phone = phone.replace("-","").replaceAll(" ","");
            editPhone.setText(phone);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void previous(View viw) {
        startActivity(new Intent(Setup3Activity.this,Setup2Activity.class));
        finish();
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
