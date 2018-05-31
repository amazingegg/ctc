package com.atkj.ctc.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.R;

/**
 * Created by Administrator on 2018/1/9 0009.
 */

public class AssetsItemView extends RelativeLayout {

    private TextView currency;
    private TextView available;
    private TextView freeze;
    private OnViewClickListener listener;
    private int position;

    public AssetsItemView(Context context) {
        this(context,null);
    }

    public AssetsItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public AssetsItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.assets_item_view, this, true);

        initView(view);
        initEvent();
    }

    private void initEvent() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.onViewClick(AssetsItemView.this);
                }
            }
        });

    }

    public void setOnViewClickListener(OnViewClickListener listener ){
        this.listener = listener;
    }

    public void setPosition(int i) {
        position = i;
    }

    public int getPosition() {
        return position;
    }

    public interface OnViewClickListener{
        void onViewClick(AssetsItemView view);
    }

    private void initView(View view) {
        currency = view.findViewById(R.id.tv_currency);
        available = view.findViewById(R.id.tv_available);
        freeze = view.findViewById(R.id.tv_freeze);
    }







    public String getCurrency() {
        return currency.getText().toString();
    }

    public String getAvailable() {
        return available.getText().toString();
    }

    public String getFreeze() {
        return freeze.getText().toString();
    }

    public void setCurrency(String s){
        currency.setText(s);
    }

    public void setAvailable(String s){
        available.setText(s);
    }

    public void setFreeze(String s){
        freeze.setText(s);
    }

    public void setCurrencyTextSize(float size){
        currency.setTextSize(size);
    }

    public void clearData(){
        available.setText("0.000000");
        freeze.setText("0.000000");
    }




}
