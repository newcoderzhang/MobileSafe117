package com.huilong.zhang.mobilesafe117.View;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huilong.zhang.mobilesafe117.R;
import com.huilong.zhang.mobilesafe117.bean.AppInfo;
import com.huilong.zhang.mobilesafe117.engine.PackageManager2;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "AppManagerActivity";
    @ViewInject(R.id.list_view)
    private ListView listView;
    @ViewInject(R.id.tv_rom)
    private TextView textviewtv_rom;
    @ViewInject(R.id.tv_sd)
    private TextView textview_tv_sd;

    @ViewInject(R.id.tv_app)
    private TextView tv_app;
    private List<AppInfo> appInfo;
    private List<AppInfo> userappInfo;
    private List<AppInfo> sysrappInfo;
    private MyBaseAdapter myBaseAdapter;
    private PopupWindow popupWindow;
    private AppInfo clickAppInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);
        ininUI();
        initData();
        UninstallReceiver uninstallReceiver = new UninstallReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addDataScheme("package");
        registerReceiver(uninstallReceiver, intentFilter);
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(myBaseAdapter == null) {
                myBaseAdapter = new MyBaseAdapter();
                listView.setAdapter(myBaseAdapter);
            }


        }
    };

    private void initData() {
        new Thread(){
            @Override
            public void run() {
                appInfo = PackageManager2.getAppInfos(AppManagerActivity.this);
                userappInfo = new ArrayList<AppInfo>();   //用户app
                sysrappInfo = new ArrayList<AppInfo>();   //系统app
                for (AppInfo appinfteo:appInfo
                     ) {
                    if (appinfteo.isUserApp()) {
                        userappInfo.add(appinfteo);
                    }else {
                        sysrappInfo.add(appinfteo);
                    }
                }
                handler.sendEmptyMessage(0);

            }
        }.start();

    }

    private void ininUI() {
        ViewUtils.inject(this);
        long rom_space = Environment.getDataDirectory().getFreeSpace();
        long sd_freespace = Environment.getExternalStorageDirectory().getFreeSpace();
        textviewtv_rom.setText(Formatter.formatFileSize(this,rom_space));
        textview_tv_sd.setText(Formatter.formatFileSize(this, sd_freespace));
        Log.v(TAG, "rom_space is " + rom_space + "sd_freespace is " + sd_freespace);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                popupWindowDismiss();

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                Log.v(TAG, "firstVisibleItem visibleItemCount totalItemCount " + firstVisibleItem + visibleItemCount + totalItemCount);
                popupWindowDismiss();
                if (userappInfo != null && sysrappInfo != null) {
                    if (firstVisibleItem > (userappInfo.size() + 1)) {
                        //系统应用程序
                        tv_app.setText("系统程序(" + sysrappInfo.size() + ")个");
                    } else {
                        //用户应用程序
                        tv_app.setText("用户程序(" + userappInfo.size() + ")个");
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object obj = listView.getItemAtPosition(position);

                if (obj != null && obj instanceof AppInfo) {
                    clickAppInfo = (AppInfo) obj;
                    View inflateview = View.inflate(AppManagerActivity.this, R.layout.item_popup, null);
                    LinearLayout ll_uninstall = (LinearLayout) inflateview.findViewById(R.id.ll_uninstall);

                    LinearLayout ll_share = (LinearLayout) inflateview.findViewById(R.id.ll_share);

                    LinearLayout ll_start = (LinearLayout) inflateview.findViewById(R.id.ll_start);

                    LinearLayout ll_detail = (LinearLayout) inflateview.findViewById(R.id.ll_detail);
                    ll_uninstall.setOnClickListener(AppManagerActivity.this);
                    popupWindowDismiss();

                    popupWindow = new PopupWindow(inflateview, -2, -2);

                    popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    int[] location = new int[2];
                    //获取view展示到窗体上面的位置
                    view.getLocationInWindow(location);

                    popupWindow.showAtLocation(parent, Gravity.LEFT + Gravity.TOP, 70, location[1]);


                    ScaleAnimation sa = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

                    sa.setDuration(1700);

                    inflateview.startAnimation(sa);
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_manager, menu);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_share:

                Intent share_localIntent = new Intent("android.intent.action.SEND");
                share_localIntent.setType("text/plain");
                share_localIntent.putExtra("android.intent.extra.SUBJECT", "f分享");
                share_localIntent.putExtra("android.intent.extra.TEXT",
                        "Hi！推荐您使用软件：" + clickAppInfo.getApkName()+"下载地址:"+"https://play.google.com/store/apps/details?id="+clickAppInfo.getApkName());
                this.startActivity(Intent.createChooser(share_localIntent, "分享"));
                popupWindowDismiss();

                break;

            //运行
            case R.id.ll_start:

                Intent start_localIntent = this.getPackageManager().getLaunchIntentForPackage(clickAppInfo.getApkName());
                this.startActivity(start_localIntent);
                popupWindowDismiss();
                break;
            //卸载
            case R.id.ll_uninstall:

                Intent uninstall_localIntent = new Intent("android.intent.action.DELETE", Uri.parse("package:" + clickAppInfo.getApkName()));
                startActivity(uninstall_localIntent);
                popupWindowDismiss();
                break;

            case R.id.ll_detail:
                Intent detail_intent = new Intent();
                detail_intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                detail_intent.addCategory(Intent.CATEGORY_DEFAULT);
                detail_intent.setData(Uri.parse("package:" + clickAppInfo.getApkName()));
                startActivity(detail_intent);
                break;

        }
    }
    private void popupWindowDismiss() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    private class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return appInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return appInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(position == 0) {
                TextView textview  = new TextView(AppManagerActivity.this);
                textview.setTextColor(Color.RED);
                textview.setText("用户程序（ " + userappInfo.size() + ")");
                return textview;
            }else  if (position == userappInfo.size() +1) {
                TextView textview  = new TextView(AppManagerActivity.this);
                textview.setTextColor(Color.RED);
                textview.setText("系统程序（ " + sysrappInfo.size() + ")");
                return textview;
            }

            AppInfo appInfo;

            if (position < userappInfo.size() + 1) {
                //把多出来的特殊的条目减掉
                appInfo = userappInfo.get(position - 1);

            } else {

                int location = userappInfo.size() + 2;

                appInfo = sysrappInfo.get(position - location);
            }
            View view = null;
            ViewHolder viewHolder;



            if(convertView != null && convertView instanceof LinearLayout) {
                view = convertView;
                viewHolder = (ViewHolder) convertView.getTag();
                Log.v(TAG,"重复使用的view");

            }else{
                viewHolder = new ViewHolder();
                view = View.inflate(AppManagerActivity.this,R.layout.item_app_manager,null);
                viewHolder.view_icon = (ImageView) view.findViewById(R.id.iv_icon);
                viewHolder.textview_apk_size = (TextView) view.findViewById(R.id.tv_apk_size);
                viewHolder.textview_apk_name = (TextView) view.findViewById(R.id.tv_name);
                viewHolder.textview_apk_loacation = (TextView) view.findViewById(R.id.tv_location);
                view.setTag(viewHolder);
                Log.v(TAG,"new item");

            }

//            if(convertView == null) {
//                viewHolder = new ViewHolder();
//                view = View.inflate(AppManagerActivity.this,R.layout.item_app_manager,null);
//                viewHolder.view_icon = (ImageView) view.findViewById(R.id.iv_icon);
//                viewHolder.textview_apk_size = (TextView) view.findViewById(R.id.tv_apk_size);
//                viewHolder.textview_apk_name = (TextView) view.findViewById(R.id.tv_name);
//                viewHolder.textview_apk_loacation = (TextView) view.findViewById(R.id.tv_location);
//                view.setTag(viewHolder);
//                Log.v(TAG,"new item");
//            }else {
//                view = convertView;
//                viewHolder = (ViewHolder) convertView.getTag();
//                Log.v(TAG,"重复使用的view");
//            }
            //AppInfo appInfo = AppManagerActivity.this.appInfo.get(position);
            viewHolder.view_icon.setBackground(appInfo.getDrawable());
            viewHolder.textview_apk_size.setText(Formatter.formatFileSize(AppManagerActivity.this, appInfo.getApksize()));
            viewHolder.textview_apk_name.setText(appInfo.getApkName());
            if(appInfo.isRom()) {
                viewHolder.textview_apk_loacation.setText("手机");
            } else {

            }
            return view;
        }
    }
    static class ViewHolder{
        ImageView view_icon;
        TextView textview_apk_size;
        TextView textview_apk_name;
        TextView textview_apk_loacation;

    }

    @Override
    protected void onDestroy() {
        popupWindowDismiss();
        super.onDestroy();
    }
    private class UninstallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("接收到卸载的广播");
        }
    }
}
