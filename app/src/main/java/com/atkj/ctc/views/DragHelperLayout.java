package com.atkj.ctc.views;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Panda on 2018/1/4 0004.
 */

public class DragHelperLayout extends LinearLayout {
    private ViewDragHelper mDragHelper;
    private int viewH;

    private View t1, t2;

    @SuppressWarnings("static-access")
    public DragHelperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*
         * 创建带回调接口的ViewDragHelper
         */
        mDragHelper = ViewDragHelper.create(this, 5.0f,new DragHelperCallback());// 参数一:该类生成的对象(当前的ViewGroup)
        gestureDC = new GestureDetectorCompat(getContext(),new YSlideDetector());
        // 参数二：敏感度（越大越敏感）
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(
                resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    public static int resolveSizeAndState(int size, int measureSpec,
                                          int childMeasuredState) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                if (specSize < size) {
                    result = specSize | MEASURED_STATE_TOO_SMALL;
                } else {
                    result = size;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result | (childMeasuredState & MEASURED_STATE_MASK);
    }



    /**
     * (non-Javadoc)
     * @see android.view.View#onFinishInflate()
     * 初始化两个View
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        t1 = getChildAt(0);
        t2 = getChildAt(1);
    }



    private void viewFollowChanged(int viewIndex, int posTop) {
        viewH = t1.getMeasuredHeight();
        if (viewIndex == 1) {
            int offsetTopBottom = viewH + t1.getTop() - t2.getTop();
            t2.offsetTopAndBottom(offsetTopBottom);
        } else if (viewIndex == 2) {
            int offsetTopBottom = t2.getTop() - viewH - t1.getTop();
            t1.offsetTopAndBottom(offsetTopBottom);
        }
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private void putStickOrDown(View releasedChild, float yvel) {
        int finalTop = 0; // 默认是粘到最顶端
        if (releasedChild == t1) {
            // 滑动第一个view松开
            if (yvel < 0)//灵敏度自己调吧
                finalTop = -viewH;
        } else {
            // 滑动第二个view松开
            if (yvel > 0)//同上
                finalTop = viewH;
        }
        if (mDragHelper.smoothSlideViewTo(releasedChild, 0, finalTop)) {
            ViewCompat.postInvalidateOnAnimation(this);// 会在下一个Frame开始的时候，发起一些invalidate操作
        }
    }

    class YSlideDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceY) > Math.abs(distanceX);//Y方向绝对值大于X方向，上下滑动
        }
    }

    private GestureDetectorCompat gestureDC;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {


        boolean is_y_slide = gestureDC.onTouchEvent(event);
        boolean shouldIntercept = mDragHelper.shouldInterceptTouchEvent(event);
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            mDragHelper.processTouchEvent(event);// action_down时就让mDragHelper开始工作，否则有时候导致异常
        }
        return shouldIntercept && is_y_slide;
    }

    class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {

            putStickOrDown(releasedChild,yvel);// 滑动松开后，需要置顶或置底
        }



        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            int childIndex = 1;
            if (changedView == t2) {
                childIndex = 2;
            }
            viewFollowChanged(childIndex, top);
        }

        @Override
        public boolean tryCaptureView(View arg0, int arg1) {
            return true;    //返回true表示可以捕捉ViewGroup中的View
        }
        /*
         * (non-Javadoc)
         * @see android.support.v4.widget.ViewDragHelper.Callback#clampViewPositionVertical(android.view.View, int, int)
         * 限定View竖直方向上的活动区域，防止滑出ViewGroup
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int slideTop = top;
            if (child == t1) {
                if (top > 0) {
                    slideTop = 0;
                }
            } else if (child == t2) {
                if (top < 0) {
                    slideTop = 0;
                }
            }
            return child.getTop() + (slideTop - child.getTop());
        }

    }


    /*@Override
    public boolean onInterceptTouchEvent(MotionEvent event)
    {
        return mDragHelper.shouldInterceptTouchEvent(event);//是否应该打断MotionEvent的传递
    }*/

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        mDragHelper.processTouchEvent(event);
        return true;
    }


}
