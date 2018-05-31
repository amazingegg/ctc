package com.atkj.ctc.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/5 0005.
 */

public class KMap {

    /**
     * channel : ok_sub_spot_btc_usd_kline_1min
     * data : [["1490337840000","995.37","996.75","995.36","996.75","9.112"],["1490337840000","995.37","996.75","995.36","996.75","9.112"]]
     */

    private String channel;
    private List<List<String>> data;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }
}
