package com.atkj.ctc.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Administrator on 2018/1/24 0024.
 */

public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = false;

    public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }




}
