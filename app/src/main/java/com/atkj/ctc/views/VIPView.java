package com.atkj.ctc.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.utils.AppUtils;

/**
 * Created by Administrator on 2018/1/13 0013.
 */

public class VIPView extends RelativeLayout {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    private LinearLayout vipView;
    private ImageView head;
    private ImageView ivVip;
    private ImageView ivVip2;
    private TextView name;
    private TextView tvTime;
    private TextView tvTips;
    private ProgressBar progressBar;
    private boolean isVip;
    private LinearLayout level;
    private int maxProgress = 5;

    public VIPView(Context context) {
        this(context, null);
    }

    public VIPView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VIPView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.vip_view, this, true);
        init(view);


    }

    private void init(View view) {
        vipView = view.findViewById(R.id.ll_vipview);
        level = view.findViewById(R.id.ll_level);
        head = view.findViewById(R.id.head);
        ivVip = view.findViewById(R.id.iv_vip);
        ivVip2 = view.findViewById(R.id.iv_vip2);
        name = view.findViewById(R.id.tv_name);
        tvTime = view.findViewById(R.id.tv_vip);
        tvTips = view.findViewById(R.id.tv_tips);
        progressBar = view.findViewById(R.id.progress);

        progressBar.setMax(maxProgress);

        init();
    }

    public void setName(String s) {
        name.setText(s);
    }

    public void setTime(String s) {
        tvTime.setText(s);
    }

    public void setVipImage(Drawable drawable, int direction) {
        if (direction == LEFT) {
            ivVip.setImageDrawable(drawable);
        } else {
            ivVip2.setImageDrawable(drawable);
        }

    }

    public void setProgress(int progress){
        progressBar.setProgress(progress);
    }

    public void setMaxProgress(int maxProgress){
        this.maxProgress = maxProgress;
        progressBar.setMax(maxProgress);
    }

    public void setTips(String s){
        tvTips.setText(s);
    }

    public void setHeadImage(Drawable drawable){
        head.setImageDrawable(drawable);
    }

    public void isVip(boolean b){
        isVip = b;
        init();
    }


    private void init() {
        if (isVip){
            level.setVisibility(VISIBLE);
            tvTips.setVisibility(VISIBLE);

            ViewGroup.LayoutParams lp = vipView.getLayoutParams();
            lp.height = AppUtils.dip2px(getContext(),140);
            vipView.setLayoutParams(lp);

        }else {
            level.setVisibility(GONE);
            tvTips.setVisibility(GONE);

            ViewGroup.LayoutParams lp = vipView.getLayoutParams();
            lp.height = AppUtils.dip2px(getContext(),100);
            vipView.setLayoutParams(lp);

        }
    }


}
