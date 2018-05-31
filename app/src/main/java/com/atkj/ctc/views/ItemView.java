package com.atkj.ctc.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.R;

/**
 * Created by Administrator on 2017/12/16 0016.
 */

public class ItemView extends RelativeLayout {

    private TextView title;
    private TextView info;
    private View line;
    private TextView info2;
    private OnInfoClickedListener listener;

    public ItemView(Context context) {
        this(context,null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, this, true);
        title = view.findViewById(R.id.tv_title);
        info = view.findViewById(R.id.tv_info);
        line = view.findViewById(R.id.line);
        info2 = view.findViewById(R.id.tv_info2);

        initEvent();

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        if (attributes != null){
            setTitle(attributes.getString(R.styleable.ItemView_ivTitle));
            setInfo(attributes.getString(R.styleable.ItemView_ivInfo));
            setLineVisible(attributes.getBoolean(R.styleable.ItemView_lineVisible,true));
            setInfoColor(attributes.getInt(R.styleable.ItemView_ivInfoColor,getResources().getColor(R.color.text_color)));
            setInfo2Text(attributes.getString(R.styleable.ItemView_ivInfo2));
            setInfo2Visible(attributes.getBoolean(R.styleable.ItemView_showInfo2,false));
            setTitleColor(attributes.getInt(R.styleable.ItemView_ivTitleColor,getResources().getColor(R.color.f888888)));

            attributes.recycle();
        }
    }

    private void initEvent() {
        info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onInfoclicked(ItemView.this);
                }
            }
        });



    }

    public void setTitle(String string){
        title.setText(string);
    }

    public void setInfo(String string){
        info.setText(string);
    }

    public void setLineVisible(boolean b){
        line.setVisibility(b ? VISIBLE : GONE);
    }

    public void setInfoColor(int color){
        info.setTextColor(color);
    }

    public void setInfo2Visible(boolean b){
        info2.setVisibility(b ? VISIBLE : GONE);
    }

    public void setInfo2Text(String s){
        info2.setText(s);
    }

    public String getInfo2Text(){return info2.getText().toString();}

    public void setTitleColor(int color){
        title.setTextColor(color);
    }

    public void setOnInfoClickedListener(OnInfoClickedListener listener){
        this.listener = listener;
    }


    public interface OnInfoClickedListener{
        void onInfoclicked(ItemView view);
    }






}
