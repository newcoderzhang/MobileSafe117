package com.huilong.zhang.mobilesafe117.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huilong.zhang.mobilesafe117.R;
import com.huilong.zhang.mobilesafe117.bean.BlackNumberInfo;
import com.huilong.zhang.mobilesafe117.dao.BlackNumberDao;

import java.util.List;

public class CallSafeActivity extends Activity {
    private static final int FENYE = 1;
    private static final String TAG = "CallSafeActivity";
    private ListView listView;
    private List<BlackNumberInfo> list;
    private LinearLayout linprogress;
    private TextView mPaperNumber;
    private EditText et_page_number;


    //分批加载变量标识
    private int mstartIndex;
    private int maxcount = 20;


    public int mCurrent = 0;
    public int mPagesize = 10;
    private int mTotalsize ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_safe2);
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

    private CallSallAdapter adapter;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FENYE:
                      linprogress.setVisibility(View.INVISIBLE);

                      if(adapter == null) {
                          adapter = new CallSallAdapter();
                          listView.setAdapter(adapter);
                      } else {
                          adapter.notifyDataSetChanged();
                      }

                      //mPaperNumber.setText(mCurrent+ "/" + mTotalsize);
                      break;
                default:
                    break;

            }

        }
    };
    private void ininBlackNumber() {

        new Thread(){
            @Override
            public void run() {
                //super.run();
                BlackNumberDao dao = new BlackNumberDao(CallSafeActivity.this);
                mTotalsize = dao.getTotalNumber();
                if(list == null) {
                    list = dao.findPar2(mstartIndex, maxcount);
                }else {
                    list.addAll(dao.findPar2(mstartIndex, maxcount));
                }

 //               mPaperNumber.setText(mCurrent+ "/" + mTotalsize);
                Message msg = Message.obtain();
                msg.what = FENYE;
                msg.arg1 = mTotalsize;
                handler.sendMessage(msg);
            }
        }.start();



    }
    private void ininUi() {
        listView = (ListView) findViewById(R.id.list_blacknumber);
        linprogress = (LinearLayout) findViewById(R.id.ll_pb);
        mPaperNumber = (TextView) findViewById(R.id.tv_page_numbeer);
        et_page_number = (EditText) findViewById(R.id.et_page_number);

        linprogress.setVisibility(View.VISIBLE);  //显示加载圆圈
        final BlackNumberDao dao = new BlackNumberDao(CallSafeActivity.this);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        Log.v(TAG, "开始滑动");
                        int lastLocation = listView.getLastVisiblePosition();
                        Log.v(TAG, "last is " + lastLocation);
                        mTotalsize = dao.getTotalNumber();
                        mstartIndex += maxcount;
                        if (lastLocation >= mTotalsize - 1) {
                            Toast.makeText(CallSafeActivity.this,"数据已经到最后",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ininBlackNumber();
                        break;
                }


            }


            //          listvie 调用时候的回调，时时调用，当用手指触碰屏幕时候就被回调
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.v(TAG, "滑动结束");
            }
        });

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if(convertView == null) {
                convertView= View.inflate(CallSafeActivity.this, R.layout.item_call_safr, null);
                viewHolder  = new ViewHolder();
                viewHolder.textViewnumber = (TextView) convertView.findViewById(R.id.number_saftnumber);
                viewHolder.textViewmode = (TextView) convertView.findViewById(R.id.saft_mode);
                viewHolder.imageview = (ImageView) convertView.findViewById(R.id.image_view_item);

                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            viewHolder.textViewnumber.setText(list.get(position).getPhonenumber());
            //textViewmode.setText(list.get(position).getMode());
            String mode = list.get(position).getMode();
            if(mode.equals("1")) {
                viewHolder.textViewmode.setText("来电拦截+短信");
            }else if (mode.equals("2")){
                viewHolder.textViewmode.setText("电话拦截");
            }else if(mode.equals("3")){
                viewHolder.textViewmode.setText("短信拦截");
            }
            final  BlackNumberInfo info = list.get(position);
            viewHolder.imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BlackNumberDao dao = new BlackNumberDao(CallSafeActivity.this);
                    String number = info.getPhonenumber();
                    boolean result = dao.deleatBlackNumber(number);
                    if(result) {
                        Toast.makeText(CallSafeActivity.this,"Delete success",Toast.LENGTH_SHORT).show();
                        list.remove(position);
                        adapter.notifyDataSetChanged();

                    }


                }
            });
            return convertView;
        }
    }

    /**
     * 上一页
     * @param view
     */
    public void prePage(View view) {
        if(mCurrent <= 0) {
            Toast.makeText(CallSafeActivity.this,"已经是第一页",Toast.LENGTH_SHORT).show();
            return;
        }
        mCurrent--;
        ininBlackNumber();

    }

    /**
     * 下一页
     * @param view
     */
    public void nextPage(View view) {
        if(mCurrent > mTotalsize) {
            Toast.makeText(CallSafeActivity.this,"当前已经是最后一页",Toast.LENGTH_SHORT).show();
            return;
        }
        mCurrent++;
        ininBlackNumber();

    }


    public void jump(View v) {
        String str_papter_number = et_page_number.getText().toString().trim();
        if(TextUtils.isEmpty(str_papter_number)) {
            Toast.makeText(CallSafeActivity.this,"请输入正确的页码",Toast.LENGTH_SHORT).show();
        }else {
            int number = Integer.parseInt(str_papter_number);
            if(number >= 0 && number <=(mTotalsize-1)) {
                mCurrent = number;
                ininBlackNumber();
            }
        }

    }


    public void addBlackNumber(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View dialog_view = View.inflate(this, R.layout.dialog_add_black_number, null);
        final EditText et_number = (EditText) dialog_view.findViewById(R.id.et_number);
        final BlackNumberDao dao = new BlackNumberDao(CallSafeActivity.this);

        Button btn_ok = (Button) dialog_view.findViewById(R.id.btn_ok);

        Button btn_cancel = (Button) dialog_view.findViewById(R.id.btn_cancel);

        final CheckBox cb_phone = (CheckBox) dialog_view.findViewById(R.id.cb_phone);

        final CheckBox cb_sms = (CheckBox) dialog_view.findViewById(R.id.cb_sms);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str_number = et_number.getText().toString().trim();
                if(TextUtils.isEmpty(str_number)){
                    Toast.makeText(CallSafeActivity.this,"请输入黑名单号码",Toast.LENGTH_SHORT).show();
                    return;
                }

                String mode = "";

                if(cb_phone.isChecked()&& cb_sms.isChecked()){
                    mode = "1";
                }else if(cb_phone.isChecked()){
                    mode = "2";
                }else if(cb_sms.isChecked()){
                    mode = "3";
                }else{
                    Toast.makeText(CallSafeActivity.this,"请勾选拦截模式",Toast.LENGTH_SHORT).show();
                    return;
                }
                BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
                blackNumberInfo.setPhonenumber(str_number);
                blackNumberInfo.setMode(mode);
                list.add(0,blackNumberInfo);
                //把电话号码和拦截模式添加到数据库。
                dao.addBlackNumber(str_number,mode);

                if(adapter == null){
                    adapter = new CallSallAdapter();
                    listView.setAdapter(adapter);
                }else{
                    adapter.notifyDataSetChanged();
                }

                dialog.dismiss();
            }
        });
        dialog.setView(dialog_view);
        dialog.show();
    }

    static class ViewHolder {
        TextView textViewnumber;
        TextView textViewmode;
        ImageView imageview;

    }
}
