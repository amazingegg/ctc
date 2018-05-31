package com.atkj.ctc.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/11 0011.
 */

public class EntrustBean {


    /**
     * msg : 操作成功
     * obj : [{"total":110,"createtime":1515642034067,"price":11,"id":"0bd1b25076604520ab4b763857254b04","account":10},{"total":120,"createtime":1515642038872,"price":12,"id":"b3d21dd08b2b47d0957560e60f8e9e2b","account":10},{"total":130,"createtime":1515642043724,"price":13,"id":"1f8fa8d08d9e4ca8ac4ee02d9eef6ec2","account":10},{"total":140,"createtime":1515642047737,"price":14,"id":"35a93aa8112b480c908f8bf3a6aa92f6","account":10}]
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
         * total : 110.0
         * createtime : 1515642034067
         * price : 11.0
         * id : 0bd1b25076604520ab4b763857254b04
         * account : 10.0
         */

        private double total;
        private long createtime;
        private String price;
        private String id;
        private String account;
        private double orderid;
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public double getOrderid() {
            return orderid;
        }

        public void setOrderid(double orderid) {
            this.orderid = orderid;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }
}
