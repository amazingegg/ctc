package com.atkj.ctc.bean;

/**
 * Created by Administrator on 2018/2/6 0006.
 */

public class OrderConfirmBean {


    /**
     * msg : 操作成功
     * obj : {"sellphone":"13428977163","orderid":"ca1fa8907b794f98acc7debcb7fb81d9","alipayaccount":"1","alipayname":"1"}
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
         * sellphone : 13428977163
         * orderid : ca1fa8907b794f98acc7debcb7fb81d9
         * alipayaccount : 1
         * alipayname : 1
         */

        private String sellphone;
        private String orderid;
        private String alipayaccount;
        private String alipayname;
        private String wechatid;
        private String bankcardid;
        private String bankname;
        private String realname;
        private String selluserid;
        private String buyuserid;

        public String getSelluserid() {
            return selluserid;
        }

        public void setSelluserid(String selluserid) {
            this.selluserid = selluserid;
        }

        public String getBuyuserid() {
            return buyuserid;
        }

        public void setBuyuserid(String buyuserid) {
            this.buyuserid = buyuserid;
        }

        public String getWechatid() {
            return wechatid;
        }

        public void setWechatid(String wechatid) {
            this.wechatid = wechatid;
        }

        public String getBankcardid() {
            return bankcardid;
        }

        public void setBankcardid(String bankcardid) {
            this.bankcardid = bankcardid;
        }

        public String getBankname() {
            return bankname;
        }

        public void setBankname(String bankname) {
            this.bankname = bankname;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getSellphone() {
            return sellphone;
        }

        public void setSellphone(String sellphone) {
            this.sellphone = sellphone;
        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public String getAlipayaccount() {
            return alipayaccount;
        }

        public void setAlipayaccount(String alipayaccount) {
            this.alipayaccount = alipayaccount;
        }

        public String getAlipayname() {
            return alipayname;
        }

        public void setAlipayname(String alipayname) {
            this.alipayname = alipayname;
        }
    }
}
