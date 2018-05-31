package com.atkj.ctc.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.atkj.ctc.R;

/**
 * Created by Administrator on 2018/1/13 0013.
 */

@SuppressLint("AppCompatCustomView")
public class SwitchView extends ImageView {
    private boolean isOpen;


    public SwitchView(Context context) {
        this(context,null);
    }

    public SwitchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwitchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Drawable drawable = getResources().getDrawable(R.drawable.float_close);
        setImageDrawable(drawable);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setSwitch(!isOpen);
            }
        });
    }



    public void setSwitch(boolean b){
        isOpen = b;
        Drawable close = getResources().getDrawable(R.drawable.float_close);
        Drawable open = getResources().getDrawable(R.drawable.float_open);
        setImageDrawable(isOpen ? open : close);

    }

    public boolean isOpen(){
        return isOpen;
    }


}
