package com.atkj.ctc.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/27 0027.
 */

public class PrivelegeBean {


    /**
     * msg : 操作成功
     * obj : [{"level":"VIP","rebate":"20","discount":"8"},{"level":"VIP1","rebate":"40","discount":"6"},{"level":"VIP2","rebate":"60","discount":"4"},{"level":"VIP3","rebate":"80","discount":"2"}]
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
         * level : VIP
         * rebate : 20
         * discount : 8
         */

        private String level;
        private String rebate;
        private String discount;
        private String total;
        private String account;
        private String every;


        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getEvery() {
            return every;
        }

        public void setEvery(String every) {
            this.every = every;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getRebate() {
            return rebate;
        }

        public void setRebate(String rebate) {
            this.rebate = rebate;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }
    }
}
