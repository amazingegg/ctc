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
 * Created by Administrator on 2018/1/19 0019.
 */

public class VipPackageView extends RelativeLayout implements View.OnClickListener {

    private TextView time;
    private TextView content;
    private TextView price;

    private boolean isChecked;
    public VipPackageView(Context context) {
        this(context,null);
    }

    public VipPackageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VipPackageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.vip_package_view, this, true);
        init(view);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.VipPackageView);
        if (attributes != null){
            setTime(attributes.getString(R.styleable.VipPackageView_time));
            setContent(attributes.getString(R.styleable.VipPackageView_content));
            setPrice(attributes.getString(R.styleable.VipPackageView_price));
            attributes.recycle();
        }




    }

    private void init(View view) {
        time = view.findViewById(R.id.tv_year);
        content = view.findViewById(R.id.tv_content);
        price = view.findViewById(R.id.tv_price);

        setOnClickListener(this);

    }





    public void setTime(String s){
        time.setText(s);
    }

    public void setContent(String s){
        content.setText(s);
    }

    public void setPrice(String s){
        price.setText(s);
    }

    public String getPrice(){
        return price.getText().toString();
    }

    public void isChecked(boolean b){
        if (b){
            setBackground(getResources().getDrawable(R.drawable.shap_my_top_bg_replace));
            time.setTextColor(getResources().getColor(R.color.ff4f3a2));
            price.setTextColor(getResources().getColor(R.color.ff4f3a2));
            content.setTextColor(getResources().getColor(R.color.white));
        }else {
            setBackgroundColor(getResources().getColor(R.color.white));
            time.setTextColor(getResources().getColor(R.color.f333333));
            price.setTextColor(getResources().getColor(R.color.textColor2));
            content.setTextColor(getResources().getColor(R.color.f666666));
        }

    }

    @Override
    public void onClick(View v) {

        isChecked = true;


    }
}
