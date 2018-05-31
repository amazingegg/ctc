package com.atkj.ctc.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.R;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class MyToolBar extends RelativeLayout{


    private ImageView back;
    private TextView title;
    private OnBackClickListener mListener;
    private TextView tvRight;
    private OnRightClickListener mRightListener;

    public MyToolBar(Context context) {
        this(context,null);
    }

    public MyToolBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyToolBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.my_tool_bar, this, true);
        initView(rootView);
        initEvent();
    }

    private void initEvent() {
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onBackClick();
                }
            }
        });
        tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRightListener != null){
                    mRightListener.onRightClick();
                }
            }
        });
    }

    private void initView(View rootView) {
        back = rootView.findViewById(R.id.iv_back);
        title = rootView.findViewById(R.id.tv_title);
        tvRight = rootView.findViewById(R.id.tv_right);
    }

    public void setTitle(String str){
        title.setText(str);
    }

    public void setOnBackClickListener(OnBackClickListener listener){
        this.mListener = listener;
    }
    public void setOnRightClickListener(OnRightClickListener listener){
        this.mRightListener = listener;
    }


    public interface OnBackClickListener{
        void onBackClick();
    }
    public interface OnRightClickListener{
        void onRightClick();
    }

    public void setTextRightVisibility(boolean b){
        tvRight.setVisibility(b ? VISIBLE : GONE);
    }

    public void setRightText(String s){
        tvRight.setText(s);
    }

    public TextView getRightTextView(){
        return tvRight;
    }




}
