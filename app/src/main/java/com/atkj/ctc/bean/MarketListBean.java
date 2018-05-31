package com.atkj.ctc.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/3 0003.
 */

public class MarketListBean implements Serializable{

    /**
     * binary : 0
     * channel : ok_sub_spot_btc_usd_ticker
     * data : {"high":"17980.34","vol":"240","last":"16905.12","low":"16811","buy":"16921.21","change":"-570.75","sell":"17000.58","dayLow":"16811","close":"16905.12","dayHigh":"17587.85","open":"17587.83","timestamp":1507620587000}
     */

    private int binary;
    private String channel;
    private DataBean data;

    public int getBinary() {
        return binary;
    }

    public void setBinary(int binary) {
        this.binary = binary;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable{
        /**
         * high : 17980.34
         * vol : 240
         * last : 16905.12
         * low : 16811
         * buy : 16921.21
         * change : -570.75
         * sell : 17000.58
         * dayLow : 16811
         * close : 16905.12
         * dayHigh : 17587.85
         * open : 17587.83
         * timestamp : 1507620587000
         */

        private double high;
        private double vol;
        private double last;
        private double low;
        private double buy;
        private double change;
        private double sell;
        private double dayLow;
        private double close;
        private double dayHigh;
        private double open;
        private long timestamp;

        public double getHigh() {
            return high;
        }

        public void setHigh(double high) {
            this.high = high;
        }

        public double getVol() {
            return vol;
        }

        public void setVol(double vol) {
            this.vol = vol;
        }

        public double getLast() {
            return last;
        }

        public void setLast(double last) {
            this.last = last;
        }

        public double getLow() {
            return low;
        }

        public void setLow(double low) {
            this.low = low;
        }

        public double getBuy() {
            return buy;
        }

        public void setBuy(double buy) {
            this.buy = buy;
        }

        public double getChange() {
            return change;
        }

        public void setChange(double change) {
            this.change = change;
        }

        public double getSell() {
            return sell;
        }

        public void setSell(double sell) {
            this.sell = sell;
        }

        public double getDayLow() {
            return dayLow;
        }

        public void setDayLow(double dayLow) {
            this.dayLow = dayLow;
        }

        public double getClose() {
            return close;
        }

        public void setClose(double close) {
            this.close = close;
        }

        public double getDayHigh() {
            return dayHigh;
        }

        public void setDayHigh(double dayHigh) {
            this.dayHigh = dayHigh;
        }

        public double getOpen() {
            return open;
        }

        public void setOpen(double open) {
            this.open = open;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}
