package com.atkj.ctc.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.utils.AppUtils;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class MyItemView extends RelativeLayout {


    private TextView title;
    private TextView info;
    private View line;
    private ImageView image;
    private ImageView right_arrow;

    public MyItemView(Context context) {
        this(context,null);
    }

    public MyItemView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.my_item_view, this, true);
        initView(rootView);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.MyItemView);
        if (attributes != null) {
            setTextTitle(attributes.getString(R.styleable.MyItemView_title));
            setInfo(attributes.getString(R.styleable.MyItemView_info));

            setInfoColor(attributes.getColor(R.styleable.MyItemView_infoColor,getResources().getColor(R.color.text_color)));
            setTextColor(attributes.getColor(R.styleable.MyItemView_miv_textColor,getResources().getColor(R.color.text_color)));

            isShowInfo(attributes.getBoolean(R.styleable.MyItemView_isShowInfo,false));
            isShowLine(attributes.getBoolean(R.styleable.MyItemView_isLineVisible,true));
            isShowRightArrow(attributes.getBoolean(R.styleable.MyItemView_isShowRightArrow,true));
            setImageVisible(attributes.getBoolean(R.styleable.MyItemView_isShowDrawable,false));
            setImage(attributes.getDrawable(R.styleable.MyItemView_drawableLeft));

            setLeftMargin(attributes.getInt(R.styleable.MyItemView_miv_leftMargin,0));
            attributes.recycle();
        }

    }



    private void initView(View rootView) {
        title = rootView.findViewById(R.id.tv_title);
        info = rootView.findViewById(R.id.tv_info);
        line = rootView.findViewById(R.id.line);
        image = rootView.findViewById(R.id.image);
        right_arrow = rootView.findViewById(R.id.iv_right_arrow);
    }

    public void setImage(Drawable drawable){
        image.setImageDrawable(drawable);
    }

    public void setImageVisible(boolean b){
        image.setVisibility(b ? VISIBLE : GONE);
    }

    public void setInfo(String str){
        info.setText(str);
    }

    public void setInfoColor(int color){
        info.setTextColor(color);
    }

    public void setTextTitle(String str){
        title.setText(str);
    }

    public void isShowInfo(boolean b){
        info.setVisibility(b ? VISIBLE : GONE);
    }

    public void isShowLine(boolean b){
        line.setVisibility(b ? VISIBLE : GONE);
    }

    public void isShowRightArrow(boolean b){
        right_arrow.setVisibility(b ? VISIBLE : GONE);
    }

    public void setTextColor(int color){
        title.setTextColor(color);
    }

    public void setLeftMargin(int margin){
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(image.getLayoutParams());
        lp.setMargins(AppUtils.dip2px(getContext(),margin), 0, AppUtils.dip2px(getContext(),10), 0);
        lp.addRule(RelativeLayout.CENTER_VERTICAL);
        image.setLayoutParams(lp);
    }

    public void setDrawableLeft(int res){
        Drawable drawable = getResources().getDrawable(res);

        drawable.setBounds(0, 0, AppUtils.dip2px(getContext(),25),
                AppUtils.dip2px(getContext(),25));
        title.setCompoundDrawables(drawable,null,null,null);
        title.setCompoundDrawablePadding(AppUtils.dip2px(getContext(),10));
    }





}
