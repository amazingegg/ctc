package com.atkj.ctc.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atkj.ctc.R;
import com.atkj.ctc.utils.LogUtils;

import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/12/16 0016.
 */

public class InputBox extends RelativeLayout implements TextWatcher {

    private Context mContext;
    //private TextView title;
    private EditText input;
    private TextView currency;
    private TextChangedListener listener;
    private java.lang.String TAG = "InputBox";

    public InputBox(Context context) {
        this(context, null);
        mContext = context;
    }

    public InputBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InputBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.input_box, this, true);

        //title = view.findViewById(R.id.tv_title);
        input = view.findViewById(R.id.et_input);
        currency = view.findViewById(R.id.tv_currency);

        initEvent();


        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.InputBox);
        if (attributes != null) {
            //setTitle(attributes.getString(R.styleable.InputBox_ibTitle));
            setCurrency(attributes.getString(R.styleable.InputBox_ibCurrency));
            setHint(attributes.getString(R.styleable.InputBox_ibHint));
            attributes.recycle();
        }


        /*input.setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String str = source.toString();
                String input = InputBox.this.input.getText().toString().trim();

                //判断第一位输入是否是“.”
                if (input.length() == 0 && str.startsWith(".")) {
                    return "0";
                }

                if (input.contains(".")){
                    //截取第一个小数点后面的数字
                    String substring = input.substring(input.indexOf(".") + 1, input.length());

                    //截取的第一个小数点后的数字包含 .
                    if (str.contains(".")) {
                       return "";
                    }

                    //"100.22222222"
                    //判断小数点后8位
                    if (substring.length() > 7) {
                        return "";
                    }
                }

                return source;
            }
        }});*/

    }


    private void initEvent() {
        input.addTextChangedListener(this);
        String digits = ".0123456789";
        setDigits(digits);

    }

    public void setDigits(final String digits) {
        input.setKeyListener(new NumberKeyListener() {
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


    /* public void setTitle(String string){
         title.setText(string);
     }*/
    //设置文本提示
    public void setHint(String str) {
        input.setHint(str);
    }

    //设置交易币种
    public void setCurrency(String string) {
        currency.setText(string);
    }

    //获取文本
    public String getText() {
        return input.getText().toString().trim();
    }

    //设置文本
    public void setText(String s) {
        input.setText(s);
        //input.setSelection(s.length());
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


    }

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
    }


    @Override
    public void afterTextChanged(Editable editable) {

        String str = editable.toString().trim();
        //判断第一位输入是否是“.”
        if (str.startsWith(".")) {
            str = "0" + str;
            input.setText(str);
            if (str.length() == 2) {
                input.setSelection(str.length());
            }
            return;
        }

        //判断首位是否是“0”
        if (str.startsWith("0") && str.length() > 1) {
            //判断第二位不是“.”
            if (!str.substring(1, 2).equals(".")) {

                str = str.substring(1, str.length());
                input.setText(str);
                input.setSelection(str.length());
                return;
            }
        }

        //限制只能输入一个小数点
        if (str.contains(".")) {

            //截取第一个小数点后面的数字
            String substring = str.substring(str.indexOf(".") + 1, str.length());

            //截取的第一个小数点后的数字是.
            if (substring.equals(".")) {
                str = str.substring(0, str.indexOf(".") + 1);
                input.setText(str);
                input.setSelection(str.length());
                return;
            }

            //如果后面的数字包含.
            if (substring.contains(".")) {
                String s1 = str.substring(0, str.indexOf(".") + 1);
                String s2 = substring.substring(0, substring.indexOf("."));
                str = s1 + s2;
                input.setText(str);
                input.setSelection(str.length());
                return;
            }


            //判断小数点后8位
            if (str.length() - 1 - str.indexOf(".") > 4) {
                str = str.substring(0, str.indexOf(".") + 5);
                input.setText(str);
                input.setSelection(str.length());
            }
        }

        if (listener != null) {
            listener.afterTextChanged(editable);
        }

    }

    //设置文本颜色
    public void setTextColor(int i) {
        input.setTextColor(i);
    }


    public interface TextChangedListener {
        //void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2);
        //void onTextChanged(CharSequence charSequence, int i, int i1, int i2);
        void afterTextChanged(Editable editable);
    }


    public void setTextChangedListener(TextChangedListener listener) {
        this.listener = listener;
    }

    //设置是否可编辑
    public void setFocusable(boolean b) {
        input.setFocusable(b);
        input.setFocusableInTouchMode(b);
    }

    //设置币种文本颜色
    public void setRightTextSize(float size) {
        currency.setTextSize(size);
    }

    //清除输入内容
    public void clearText() {
        input.setText("");
    }

    public EditText getEditText() {
        return input;
    }


}
