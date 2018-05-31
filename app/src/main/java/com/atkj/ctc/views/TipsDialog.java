package com.atkj.ctc.views;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.atkj.ctc.R;

/**
 * Created by Administrator on 2018/1/25 0025.
 */

public class TipsDialog extends Dialog {


    private  TextView cancel;
    private  TextView enter;
    private final TextView content;
    private OnDialogClicked listener;


    public TipsDialog(Context context) {
        super(context);

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_cancel_entrust, null);
        setContentView(dialogView);



        cancel = dialogView.findViewById(R.id.cancel);
        enter = dialogView.findViewById(R.id.enter);
        content = dialogView.findViewById(R.id.content);

        setTitle("");

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onCancel();
                }
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onEnter();
                }
            }
        });



    }

    public void setContent(String str){
        content.setText(str);
    }

    public void dismiss(){
        dismiss();
    }

    public void show(){
        show();
    }

    public void setEnterText(String str){
        enter.setText(str);
    }

    public void setEnterTextColor(int color){
        enter.setTextColor(color);
    }

    public interface OnDialogClicked{
        void onCancel();
        void onEnter();
    }

    public void setOnDialogClicked(OnDialogClicked listener){
        this.listener = listener;
    }


}
