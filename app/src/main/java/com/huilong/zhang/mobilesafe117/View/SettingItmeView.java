package com.huilong.zhang.mobilesafe117.View;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huilong.zhang.mobilesafe117.R;

/**
 * Created by Mario on 1/19/16.
 */
public class SettingItmeView extends RelativeLayout {
    private static final String TAG = "SettingItmeView";
    private static final String  namespace = "http://schemas.android.com/apk/res/com.huilong.zhang.mobilesafe117";

    private String mtitle;
    private String mdescon;
    private String mdescoff;
    private TextView textView11;
    private TextView textViewdesc;
    private CheckBox checkBox;

    public SettingItmeView(Context context) {
        super(context);
        init();
    }

    public SettingItmeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        int attributeCount = attrs.getAttributeCount();
        for(int i=0;i<attributeCount;i++) {
            String attribuename = attrs.getAttributeName(i);
            String attributevalue = attrs.getAttributeValue(i);
            Log.v(TAG,"attribuename is " + attribuename + ":attributevalue is " + attributevalue);
        }

        mtitle = attrs.getAttributeValue(namespace,"titlezhang");
        mdescon = attrs.getAttributeValue(namespace,"desc_on");
        mdescoff = attrs.getAttributeValue(namespace,"desc_off");
        setTitle(mtitle);

        Log.v(TAG,"mtitle is " + mtitle);
        Log.v(TAG,"desc_on " + mdescon);
        Log.v(TAG,"desc_off " + mdescoff);
    }

    public SettingItmeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
//    初始化布局
    private  void init() {
        View view = View.inflate(getContext(), R.layout.view_setting_item,this);
        textView11 = (TextView) view.findViewById(R.id.tv_title1);
        textViewdesc = (TextView) view.findViewById(R.id.text_tv111);
        checkBox = (CheckBox) view.findViewById(R.id.mycheckbox);

    }
    public void setTitle(String title) {
        textView11.setText(title);
    }
    public void setDesc(String desc) {
        textViewdesc.setText(desc);
    }
    public boolean ischecked() {
        return checkBox.isChecked();
    }
    public void setcheckedone(boolean flag) {
        checkBox.setChecked(flag);
        if(flag) {
            setDesc(mdescon);
        }else {
            setDesc(mdescoff);
        }
    }
}
