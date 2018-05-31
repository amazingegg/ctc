package com.atkj.ctc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

public class AssetsListBean {


    /**
     * msg : 操作成功
     * obj : [{"currency":"π","account":1185.48,"unaccount":1185.48},{"currency":"usd","account":0,"unaccount":0}]
     * status : 200
     */

    private String msg;
    private int status;
    private List<ObjBean> obj;

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

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * currency : π
         * account : 1185.48
         * unaccount : 1185.48
         */

        private String currency;
        private String account;
        private String unaccount;
        private int status;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getUnaccount() {
            return unaccount;
        }

        public void setUnaccount(String unaccount) {
            this.unaccount = unaccount;
        }
    }
}
