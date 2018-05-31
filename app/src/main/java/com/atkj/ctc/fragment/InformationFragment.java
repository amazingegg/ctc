package com.atkj.ctc.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class InformationFragment extends BaseFragment {


    private static final String TAG = InformationFragment.class.getSimpleName();
    private String name;

    /**
     * 通过工厂方法来创建Fragment实例
     * 同时给Fragment来提供参数来使用
     *
     * @param param1 参数1.
     * @return Fragment的实例.
     */
    public static InformationFragment newInstance(String param1) {
        InformationFragment fragment = new InformationFragment();
        Bundle args = new Bundle();
        args.putString("name", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString("name");
        }
    }





    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        textView.setText("资讯");
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }


}
