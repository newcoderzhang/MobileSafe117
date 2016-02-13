package com.huilong.zhang.mobilesafe117.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Mario on 1/18/16.
 */
public class FocusedTextView extends TextView {

    //直接构造此方法
    public FocusedTextView(Context context) {
        super(context);
    }

    //有属性时候走此方法
    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //有style的时候会构造此方法
    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Returns true if this view has focus
     *
     * @return True if this view has focus, false otherwise.
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
