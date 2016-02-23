package com.huilong.zhang.mobilesafe117;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huilong.zhang.mobilesafe117.View.AToolsActivity;
import com.huilong.zhang.mobilesafe117.View.CallSafeActivity;
import com.huilong.zhang.mobilesafe117.View.LostFindActivity;
import com.huilong.zhang.mobilesafe117.View.SetingActivity;
import com.huilong.zhang.mobilesafe117.utils.MD5Utils;

public class HomeActivity extends Activity {
    private static final String TAG = "HomeActivity";
    private GridView gridView;
    private ImageView imageView;
    private EditText editText;
    private String[] mItems = new String[]{"手机防盗",
            "通讯卫卫士",
            "软件管理",
            "进程管理",
            "流量统计",
            "手机杀毒",
            "缓存清理",
            "高级工具",
            "设置中心"
    };
    private  int[] mPics = new int[]{R.mipmap.home_safe,
            R.mipmap.home_callmsgsafe,
            R.mipmap.home_apps,
            R.mipmap.home_taskmanager,
            R.mipmap.home_netmanager,
            R.mipmap.home_trojan,
            R.mipmap.home_sysoptimize,
            R.mipmap.home_tools,
            R.mipmap.home_settings};
    private HomeAdapter homeAdapter;
    private SharedPreferences sharedPreferences;

    private String actionmps = "com.huilong.zhang.mobilesafe117.palymp3";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(new HomeAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Log.v(TAG, "选择1");
                        showPasswordDialog();
                        break;
                    case 1:
                        startActivity(new Intent(HomeActivity.this, CallSafeActivity.class));
                        break;
                    case 8:
                        Log.v(TAG, "选择8");
                        startActivity(new Intent(HomeActivity.this, SetingActivity.class));
                        break;
                    case 7:
                        Log.v(TAG,"高级工具");
                        startActivity(new Intent(HomeActivity.this, AToolsActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
    class HomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mPics.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(HomeActivity.this,R.layout.home_list_item,null);
            ImageView imageView = (ImageView) view.findViewById(R.id.tv_icon);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            imageView.setImageResource(mPics[position]);
            textView.setText(mItems[position]);
            return view;
        }
    }
    //显示密码弹框
    private void showPasswordDialog() {
        String savepass = sharedPreferences.getString("password",null);
        if(TextUtils.isEmpty(savepass)) {
            showPasswordSetDialog();
        }else {
            passwordInputDialog();
        }

    }

    private void passwordInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(HomeActivity.this, R.layout.dialog_input_password, null);
        Button buttonok = (Button) view.findViewById(R.id.buttonok);
        Button buttoncancle = (Button) view.findViewById(R.id.buttoncancle);

        final EditText passwordfirst = (EditText) view.findViewById(R.id.editTextpassword);
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = passwordfirst.getText().toString();
                String savepass = sharedPreferences.getString("password", null);
                if(!TextUtils.isEmpty(password1)){
                    if(MD5Utils.encode(password1).equals(savepass)) {
                        Toast.makeText(HomeActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        //sharedPreferences.edit().putString("password", password1).commit();

                        dialog.dismiss();
                        startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
                        sendBroadcast(new Intent(actionmps));


                    }else {
                        Toast.makeText(HomeActivity.this,"输入密码与保存密码不一致",Toast.LENGTH_SHORT).show();

                    }

                }else {
                    Toast.makeText(HomeActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }

            }
        });
        buttoncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                dialog.dismiss();
            }
        });
        dialog.setView(view,0,0,0,0);
        dialog.show();

    }



    /**
     * 设置密码
     */

    private void showPasswordSetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(HomeActivity.this, R.layout.dialog_set_password, null);
        Button buttonok = (Button) view.findViewById(R.id.buttonok);
        Button buttoncancle = (Button) view.findViewById(R.id.buttoncancle);

        final EditText passwordfirst = (EditText) view.findViewById(R.id.editTextpassword);
        final EditText passswordtwo = (EditText) view.findViewById(R.id.editTextconfirm);
        buttonok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = passwordfirst.getText().toString();
                String password2 = passswordtwo.getText().toString();

                if(!TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2) ){
                    if(password1.equals(password2)) {
                        Toast.makeText(HomeActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        sharedPreferences.edit().putString("password", MD5Utils.encode(password1)).commit();

                        dialog.dismiss();
                        startActivity(new Intent(HomeActivity.this, LostFindActivity.class));

                    }else {
                        Toast.makeText(HomeActivity.this,"两次输入密码不一致",Toast.LENGTH_SHORT).show();

                    }

                }else {
                    Toast.makeText(HomeActivity.this,"输入框不能为空",Toast.LENGTH_SHORT).show();
                }

            }
        });
        buttoncancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //取消
                dialog.dismiss();
            }
        });
        dialog.setView(view,0,0,0,0);
        dialog.show();

    }
}
