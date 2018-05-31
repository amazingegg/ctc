package com.atkj.ctc.api;

import com.atkj.ctc.R;
import com.kingja.loadsir.callback.Callback;



/**
 * Description:
 * Create Time:2017/9/4 10:22
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */

public class LoadingCallback extends Callback {

    @Override
    protected int onCreateView() {
        return R.layout.layout_loading;
    }
}
