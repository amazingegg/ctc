package com.atkj.ctc.bean;

/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class FeeBean {


    /**
     * msg : 操作成功
     * obj : {"vipstatus":1,"fee":8.208E-6,"discount":""}
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
         * vipstatus : 1
         * fee : 8.208E-6
         * discount :
         */

        private int vipstatus;
        private String fee;
        private String discount;

        public int getVipstatus() {
            return vipstatus;
        }

        public void setVipstatus(int vipstatus) {
            this.vipstatus = vipstatus;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }
    }
}
