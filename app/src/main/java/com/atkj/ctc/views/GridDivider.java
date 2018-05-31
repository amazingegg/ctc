package com.atkj.ctc.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class GridDivider extends RecyclerView.ItemDecoration {
    public static final int DIRECTION_VERTICAL = 0;
    public static final int DIRECTION_HORIZONTAL = 1;
    private Drawable divider;
    private int direction;
    private final int column;

    public GridDivider(Context context, int direction, int column,int[] attr) {
        super();

        TypedArray typedArray = context.obtainStyledAttributes(attr);
        divider = typedArray.getDrawable(0);
        this.direction = direction;
        this.column = column;
        typedArray.recycle();
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (divider == null)
            return;
        if (direction == DIRECTION_VERTICAL) {
            drawVerticalDivider(c, parent);
        } else {
            drawHoriziontalDivider(c, parent);
        }
    }

    private void drawHoriziontalDivider(Canvas c, RecyclerView parent) {
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

            //头部第一条线
            if (i % column == 0) {
                top = child.getTop() - layoutParams.topMargin - divider.getIntrinsicHeight();
                bottom = top + divider.getIntrinsicHeight();
                left = child.getLeft() - layoutParams.leftMargin;
                right = child.getRight() + layoutParams.rightMargin;
                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
            //最右边的线
            if (i >= parent.getChildCount() - column) {
                top = child.getTop() - layoutParams.topMargin - divider.getIntrinsicHeight();
                bottom = child.getBottom() + layoutParams.bottomMargin + divider.getIntrinsicHeight();
                left = child.getRight() + layoutParams.rightMargin;
                right = left + divider.getIntrinsicWidth();
                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
            //最后一个补线
//            if(i==parent.getChildCount()-1&&(i + 1) % column!=0){
//                top = child.getTop() - layoutParams.topMargin - divider.getIntrinsicHeight();
//                bottom = child.getBottom() + layoutParams.bottomMargin+divider.getIntrinsicHeight();
//                left = child.getRight() + layoutParams.rightMargin;
//                right = left + divider.getIntrinsicWidth();
//                divider.setBounds(left, top, right, bottom);
//                divider.draw(c);
//
//            }
            //左边的线
            top = child.getTop() - layoutParams.topMargin - divider.getIntrinsicHeight();
            bottom = child.getBottom() + layoutParams.bottomMargin + divider.getIntrinsicHeight();
            left = child.getLeft() - layoutParams.leftMargin - divider.getIntrinsicWidth();
            right = left + divider.getIntrinsicWidth();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);

            //下面的线
            top = child.getBottom() + layoutParams.bottomMargin;
            bottom = top + divider.getIntrinsicHeight();
            left = child.getLeft() - layoutParams.leftMargin;
            right = child.getRight() + layoutParams.rightMargin;
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }

    }


    private void drawVerticalDivider(Canvas c, RecyclerView parent) {
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

            //头部第一条线
            if (i / column == 0) {
                top = child.getTop() - layoutParams.topMargin - divider.getIntrinsicHeight();
                bottom = top + divider.getIntrinsicHeight();
                left = child.getLeft() - layoutParams.leftMargin;
                right = child.getRight() + layoutParams.rightMargin;
                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
            //最右边的线
            if ((i + 1) % column == 0) {
                top = child.getTop() - layoutParams.topMargin - divider.getIntrinsicHeight();
                bottom = child.getBottom() + layoutParams.bottomMargin + divider.getIntrinsicHeight();
                left = child.getRight() + layoutParams.rightMargin;
                right = left + divider.getIntrinsicWidth();
                divider.setBounds(left, top, right, bottom);
                divider.draw(c);
            }
            //最后一个补线
            if (i == parent.getChildCount() - 1 && (i + 1) % column != 0) {
                top = child.getTop() - layoutParams.topMargin - divider.getIntrinsicHeight();
                bottom = child.getBottom() + layoutParams.bottomMargin + divider.getIntrinsicHeight();
                left = child.getRight() + layoutParams.rightMargin;
                right = left + divider.getIntrinsicWidth();
                divider.setBounds(left, top, right, bottom);
                divider.draw(c);

            }
            //左边的线
            top = child.getTop() - layoutParams.topMargin - divider.getIntrinsicHeight();
            bottom = child.getBottom() + layoutParams.bottomMargin + divider.getIntrinsicHeight();
            left = child.getLeft() - layoutParams.leftMargin - divider.getIntrinsicWidth();
            right = left + divider.getIntrinsicWidth();
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);

            //下面的线
            top = child.getBottom() + layoutParams.bottomMargin;
            bottom = top + divider.getIntrinsicHeight();
            left = child.getLeft() - layoutParams.leftMargin;
            right = child.getRight() + layoutParams.rightMargin;
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        System.out.println("divider:" + divider.getIntrinsicHeight() + "  " + divider.getIntrinsicWidth());
        int p = parent.getChildAdapterPosition(view);
        int p1 = parent.getChildLayoutPosition(view);
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;
        System.out.println("p:" + p + " p1:" + p1);
        if (direction == DIRECTION_VERTICAL) {
            int position = parent.getChildLayoutPosition(view);
            if (position / column == 0)
                top = divider.getIntrinsicHeight();
            if ((position + 1) % column == 0)
                right = divider.getIntrinsicWidth();
            left = divider.getIntrinsicWidth();
            bottom = divider.getIntrinsicHeight();
            outRect.set(left, top, right, bottom);
        } else {
            int position = parent.getChildLayoutPosition(view);
            if (position / column == 0)
                left = divider.getIntrinsicWidth();
            if (position % column == 0)
                top = divider.getIntrinsicHeight();
            right = divider.getIntrinsicWidth();
            bottom = divider.getIntrinsicHeight();
            outRect.set(left, top, right, bottom);
        }
    }
}

