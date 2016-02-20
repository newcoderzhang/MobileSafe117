package com.huilong.zhang.mobilesafe117.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huilong.zhang.mobilesafe117.R;

/**
 * Created by Mario on 2/18/16.
 */
public class SettingClickView extends RelativeLayout {

    private TextView tvTitle;
    private TextView tvDesc;
    public SettingClickView(Context context) {
        super(context);
        init();
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SettingClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        View.inflate(getContext(), R.layout.view_setting_click, this);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDesc = (TextView) findViewById(R.id.tv_desc);
    }
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setDesc(String desc) {
        tvDesc.setText(desc);
    }


}
