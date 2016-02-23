package com.huilong.zhang.mobilesafe117.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.huilong.zhang.mobilesafe117.R;
import com.huilong.zhang.mobilesafe117.bean.BlackNumberInfo;
import com.huilong.zhang.mobilesafe117.dao.BlackNumberDao;

import java.util.List;

public class CallSafeActivity extends AppCompatActivity {
    private ListView listView;
    private List<BlackNumberInfo> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe);
        ininUi();
        ininBlackNumber();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_call_safe, menu);
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
    private void ininBlackNumber() {
        BlackNumberDao dao = new BlackNumberDao(this);
        list = dao.findALl();
        CallSallAdapter adapter = new CallSallAdapter();
        listView.setAdapter(adapter);

    }
    private void ininUi() {
        listView = (ListView) findViewById(R.id.list_blacknumber);

    }
    private class CallSallAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(CallSafeActivity.this, R.layout.item_call_safr, null);
            TextView textViewnumber = (TextView) view.findViewById(R.id.number_saftnumber);
            TextView textViewmode = (TextView) view.findViewById(R.id.saft_mode);
            textViewnumber.setText(list.get(position).getPhonenumber());
            textViewmode.setText(list.get(position).getMode());
            return view;
        }
    }
}
