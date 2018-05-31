package com.atkj.ctc.bean;

import java.util.Currency;
import java.util.List;

/**
 * Created by Administrator on 2018/4/27 0027.
 */

public class TestBean {

    private String msg = "testmsg";

    private int status = 200;

    private List<CurrencyBean1> currencyList;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<CurrencyBean1> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<CurrencyBean1> currencyList) {
        this.currencyList = currencyList;
    }

    public static class CurrencyBean1{


        private String symbol;

        private List<CurrencyBean2> currencyList;


        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public List<CurrencyBean2> getCurrencyList() {
            return currencyList;
        }

        public void setCurrencyList(List<CurrencyBean2> currencyList) {
            this.currencyList = currencyList;
        }

        public static class CurrencyBean2{

            private String symbol;

            private int ctid;

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public int getCtid() {
                return ctid;
            }

            public void setCtid(int ctid) {
                this.ctid = ctid;
            }
        }



    }



}
