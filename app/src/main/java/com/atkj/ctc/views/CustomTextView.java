package com.atkj.ctc.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by Administrator on 2018/1/16 0016.
 */

@SuppressLint("AppCompatCustomView")
public class CustomTextView extends TextView {

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public CustomTextView(Context context) {
        this(context,null);

    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "DIN-Medium.otf");
            setTypeface(tf);
        }
    }


}
