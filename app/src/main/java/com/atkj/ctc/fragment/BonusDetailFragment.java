package com.atkj.ctc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.atkj.ctc.R;
import com.atkj.ctc.adapter.CommonRecycleViewAdapter;
import com.atkj.ctc.bean.MyBonusBean;
import com.atkj.ctc.utils.AppUtils;
import com.atkj.ctc.utils.LogUtils;
import com.atkj.ctc.utils.NetUtils;
import com.atkj.ctc.utils.Url;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class BonusDetailFragment extends ListFragment {
    private static final String TAG = "ListFragment";
    // 这里的参数只是一个举例可以根据需求更改
    private String name;
    private List<MyBonusBean.ObjBean.ListBean> mListData;
    private int type;


    /**
     * 通过工厂方法来创建Fragment实例
     * 同时给Fragment来提供参数来使用
     * @param type
     * @return Fragment的实例.
     */
    public static BonusDetailFragment newInstance( int type) {
        BonusDetailFragment fragment = new BonusDetailFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type");
        }
    }


    @Override
    public void initData() {
        setAdapter(adapter);

        getDetailList(type);

    }

    private void getDetailList(final int type) {

        Map<String,String> param = new LinkedHashMap<>();
        param.put("userid", AppUtils.getUserId());
        param.put("type",String.valueOf(type));



        NetUtils.get(Url.myBonusDetails, param, new NetUtils.StringCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                LogUtils.d(TAG, "getDetailList onError: "+e.toString());
                showToast(getString(R.string.a537));

            }

            @Override
            public void onResponse(String response, int id) {
                LogUtils.d(TAG, "getDetailList onResponse: type=="+type+response);
                MyBonusBean bean = AppUtils.getGson().fromJson(response, MyBonusBean.class);
                if (bean.getStatus() == 200){
                    MyBonusBean.ObjBean obj = bean.getObj();
                    mListData = obj.getList();
                    adapter.replaceData(mListData);
                    adapter.notifyDataSetChanged();

                }
            }
        });


    }


    CommonRecycleViewAdapter adapter = new CommonRecycleViewAdapter<MyBonusBean.ObjBean.ListBean>(R.layout.list_item_bonus_detail,mListData){
        @Override
        protected void convert(BaseViewHolder helper, MyBonusBean.ObjBean.ListBean item) {
            String str = item.getStatus() == 1 ? "+" : "-";
            helper.setText(R.id.textView,item.getUsername())
                    .setText(R.id.textView2,AppUtils.timedate(item.getCreatetime(),"MM-dd HH:mm"))
                    .setText(R.id.textView4,item.getType())
                    .setText(R.id.textView5,str + item.getAccount());
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    /**
     *onAttach中连接监听接口 确保Activity支持该接口
     */
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

    }


    /**
     * onDetach中注销接口
     */
    @Override
    public void onDetach() {
        super.onDetach();
    }







}
