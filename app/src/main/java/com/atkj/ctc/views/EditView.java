package com.atkj.ctc.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.R;

/**
 * Created by Administrator on 2017/12/14 0014.
 */

public class EditView extends RelativeLayout {

    private static final String TAG = "EditView";
    private EditText editText;
    private TextView info;
    private View line;
    private OnInfoClickListener mListener;

    public EditView(Context context) {
        this(context,null);
    }

    public EditView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View rootView = LayoutInflater.from(context).inflate(R.layout.edit_view, this, true);
        initView(rootView);
        initEvent();

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditView);
        if (typedArray != null){
            setHint(typedArray.getString(R.styleable.EditView_evHint));
            setInfo(typedArray.getString(R.styleable.EditView_evInfo));
            setInputTypePwd(typedArray.getBoolean(R.styleable.EditView_evPassWord,false));
            if (!typedArray.getBoolean(R.styleable.EditView_evIsLineVisible,true)){
                line.setVisibility(GONE);
            }

            typedArray.recycle();
        }

    }

    private void initEvent() {
        info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onInfoClick(EditView.this);
                }
            }
        });



    }

    public void setDigits(final String digits){
        editText.setKeyListener(new NumberKeyListener() {
            @NonNull
            @Override
            protected char[] getAcceptedChars() {

                return digits.toCharArray();
            }

            @Override
            public int getInputType() {
                //3 数字键盘
                return 3;
            }
        });
    }

    public EditText getEditText(){
        return editText;
    }


    private void setInputTypePwd(boolean b){
        if(b){
            //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            //选择状态 显示明文--设置为可见的密码
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    private void initView(View rootView) {
        editText = rootView.findViewById(R.id.edit_text);
        info = rootView.findViewById(R.id.tv_info);
        line = rootView.findViewById(R.id.line);
    }

    public void setHint(String str){
        editText.setHint(str);
    }

    public void setInfo(String str){
        info.setText(str);
    }

    public String getContentText(){
        return editText.getText().toString().trim();
    }

    public void setInfoEnable(boolean b) {
        info.setClickable(b);
    }

    public void setInfoColor(int color) {
        info.setTextColor(color);
    }


    public interface OnInfoClickListener{
        void onInfoClick(EditView editView);
    }

    public void setOnInfoClickListener(OnInfoClickListener listener){
        this.mListener = listener;
    }




}
