package com.huilong.zhang.mobilesafe117.View;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.huilong.zhang.mobilesafe117.R;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

public class ContactActivity extends Activity {
    private static final String TAG = "ContactActivity";
    private ListView listView;
    private ArrayList<HashMap<String,String>> readContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        listView = (ListView) findViewById(R.id.lv_list);
        readContact = readContact();
        listView.setAdapter(new SimpleAdapter(this,readContact,R.layout.contact_list_item,
                new String[]{"name","phone"},new int[]{R.id.tv_name,R.id.tv_phone}));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = readContact.get(position).get("phone");
                Intent intent = new Intent();
                intent.putExtra("phone",phone);
                Log.v(TAG, "phone = " + phone);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

    }

    private ArrayList<HashMap<String, String>> readContact() {
        Uri rawContactsUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        Cursor rawCurrsor = getContentResolver().query(rawContactsUri,new String[]{"contact_id"},null,null,null);
        if (rawCurrsor !=null) {
            while(rawCurrsor.moveToNext()) {
                String contactId = rawCurrsor.getString(0);
                Cursor cursorData = getContentResolver().query(dataUri,new String[] {"data1","mimetype"},"contact_id=?",new String[]{contactId},null);
                if(cursorData !=null) {
                    HashMap<String,String>  map = new HashMap<String,String>();
                    while(cursorData.moveToNext()) {
                        String data1 = cursorData.getString(0);
                        String mimetype = cursorData.getString(1);
                        if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                            map.put("phone", data1);
                        } else if ("vnd.android.cursor.item/name"
                                .equals(mimetype)) {
                            map.put("name", data1);
                        }
                    }
                    list.add(map);
                    cursorData.close();
                }
            }
            rawCurrsor.close();
        }
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
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
