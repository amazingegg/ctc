package com.atkj.ctc.bean;

/**
 * Created by Administrator on 2018/4/2 0002.
 */

public class CtsBean {

    /**
     * msg : 操作成功
     * obj : {"surplus":19988000,"usd":0.5,"inviteno":"bace4deaf38a49538ebffd7ddea1538b"}
     * status : 200
     */

    private String msg;
    private ObjBean obj;
    private int status;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class ObjBean {
        /**
         * surplus : 19988000
         * usd : 0.5
         * inviteno : bace4deaf38a49538ebffd7ddea1538b
         */

        private int surplus;
        private String usd;
        private String eth;
        private String inviteno;

        public String getEth() {
            return eth;
        }

        public void setEth(String eth) {
            this.eth = eth;
        }

        public int getSurplus() {
            return surplus;
        }

        public void setSurplus(int surplus) {
            this.surplus = surplus;
        }

        public String getUsd() {
            return usd;
        }

        public void setUsd(String usd) {
            this.usd = usd;
        }

        public String getInviteno() {
            return inviteno;
        }

        public void setInviteno(String inviteno) {
            this.inviteno = inviteno;
        }
    }
}
