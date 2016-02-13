package com.huilong.zhang.mobilesafe117;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huilong.zhang.mobilesafe117.utils.StreamUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";
    private TextView tvVersion;
    private TextView tvProgress;


    private String mVersionName; //版本名
    private int mVersionCode;
    private String mDesc;
    private String mDownloadUrl;

    protected static final int CODE_UPDA_DIALOG = 0;
    protected static final int CODE_ENTER_HOME = 1;
    protected static final int CODE_URI_ERROR = 2;
    protected static final int CODE_NET_ERROR = 3;
    protected static final int CODE_JSON_ERROR =4;

    private SharedPreferences sharedPreferences;


    private MHandler mHandler = new MHandler();
    private RelativeLayout viewById;

    private class  MHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_UPDA_DIALOG:
                    Log.v(TAG,"CODE_UPDA_DIALOG");
                    showUpdateDialog();
                    break;
                case CODE_URI_ERROR:
                    Toast.makeText(SplashActivity.this,"url错误 ",Toast.LENGTH_SHORT).show();
                    break;
                case CODE_NET_ERROR:
                    Toast.makeText(SplashActivity.this,"网络错误 ",Toast.LENGTH_SHORT).show();
                    break;
                case CODE_JSON_ERROR:
                    Toast.makeText(SplashActivity.this,"数据解析错误",Toast.LENGTH_SHORT).show();
                    break;
                case CODE_ENTER_HOME:
                    enterhome();
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splish);
        viewById = (RelativeLayout) findViewById(R.id.relativesplish);
        sharedPreferences = getSharedPreferences("config",MODE_PRIVATE);
        boolean autoupdate = sharedPreferences.getBoolean("auto_update",true);

        tvVersion = (TextView) findViewById(R.id.tv_verion);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        tvVersion.setText("版本号: " + getVersionName());

        copyDb("address.db");
        if(autoupdate) {
            checkVersion();
        }else {
            mHandler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 2 * 1000);
        }
        AlphaAnimation animation = new AlphaAnimation(0.3f,1f);
        animation.setDuration(2000);
        viewById.startAnimation(animation);

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
    private String getVersionName() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versioncode = packageInfo.versionCode;
            String versionname = packageInfo.versionName;
            Log.v(TAG, "versionCode " + versioncode + " versionname " + versionname);
            return versionname;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    private void checkVersion() {
        final Message msg = Message.obtain();
        final long timeStart = System.currentTimeMillis();

        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = null;
                try {
                    URL url = new URL("http://192.168.31.198:8080/apkdown/update.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    conn.connect();

                    int reponseCode = conn.getResponseCode();
                    if(reponseCode == 200) {
                        InputStream inputStream = conn.getInputStream();
                        String result = StreamUtils.readFromString(inputStream);
                        Log.v(TAG,"result is" + result);
                        JSONObject jsonObject = new JSONObject(result);
                        mVersionName = jsonObject.getString("versionName");
                        mVersionCode = jsonObject.getInt("versionCode");
                        mDesc = jsonObject.getString("description");
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        if(mVersionCode > getversionCode()) {
                           msg.what = CODE_UPDA_DIALOG;
                        }
                    }
                } catch (MalformedURLException e) {
                    //url错误的异常
                    msg.what = CODE_URI_ERROR;
                    enterhome();
                    e.printStackTrace();
                } catch (IOException e) {
                    //网络错误
                    msg.what = CODE_NET_ERROR;
                    enterhome();
                    e.printStackTrace();
                } catch (JSONException e) {
                    msg.what = CODE_JSON_ERROR;
                    e.printStackTrace();
                }finally {

                    long endTime = System.currentTimeMillis();
                    long timeUse = endTime-timeStart;
                    if(timeUse < 2000) {
                        try {
                            Thread.sleep(2000-timeUse);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                    if(conn != null)
                    {
                        conn.disconnect();
                    }
                }
            }
        }.start();



    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("最新版本:" + mVersionName);
        builder.setMessage(mDesc);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG, "立即更新");
                downapk();
            }
        });
        builder.setNegativeButton("取消更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.v(TAG, "取消更新");
                enterhome();

            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.v(TAG, "取消更新");
                enterhome();
            }
        });
        builder.show();
    }

    private int getversionCode() {
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versioncode = packageInfo.versionCode;
            String versionname = packageInfo.versionName;
            Log.v(TAG, "versionCode " + versioncode + " versionname " + versionname);
            return versioncode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private void enterhome() {
        Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
        startActivity(intent);
    }

    protected  void downapk() {
        tvProgress.setVisibility(View.VISIBLE);
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String apkPath = Environment.getExternalStorageDirectory() + "/update.apk";

            HttpUtils utils = new HttpUtils();
            utils.download(mDownloadUrl, apkPath, new RequestCallBack<File>() {
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                    Log.v(TAG, "下载进度 " + current + "总进度" + total);
                    tvProgress.setText(current * 100 / total +"%");
                }

                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Log.v(TAG, "onSuccess");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");
                    //startActivity(intent);
                    startActivityForResult(intent,0);  //如果用户取消安装会回调

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Log.v(TAG, "onFailure");

                }
            });
        }else {
            Toast.makeText(SplashActivity.this,"内存卡错误",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        enterhome();
    }
    private void copyDb(String pathname) {
        File file = getFilesDir();
        Log.v(TAG,"路径file is " + file);
        File destFile = new File(getFilesDir(),pathname);
        if (destFile.exists()) {
            Log.v(TAG,"路径file is 已经存在");
            return;
        }
        FileOutputStream out = null;
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("address.db");
            out = new FileOutputStream(destFile);
            int len = 0;
            byte[] buffer = new byte[1024];
            while((len = inputStream.read(buffer))!= -1 ) {
                out.write(buffer,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
