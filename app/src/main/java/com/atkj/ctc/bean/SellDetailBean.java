package com.atkj.ctc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/18 0018.
 */

public class SellDetailBean implements Serializable{


    /**
     * msg : 操作成功
     * obj : {"ctid":32,"createtime":1523324914776,"fee":0.05,"remake":"","bailmoney":0.1,"userid":"07fa71264cd34293984b7e7296f8369b","durationtime":50,"total":25,"deleted":1,"phone":"12345678922","price":5,"name":"pppp","id":"8d84b775a88f4e458d237f0af08b0528","paytype":"2","account":5,"status":"PENDING","timestamp":0}
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

    public static class ObjBean implements Serializable{
        /**
         * ctid : 32
         * createtime : 1523324914776
         * fee : 0.05
         * remake :
         * bailmoney : 0.1
         * userid : 07fa71264cd34293984b7e7296f8369b
         * durationtime : 50
         * total : 25.0
         * deleted : 1
         * phone : 12345678922
         * price : 5.0
         * name : pppp
         * id : 8d84b775a88f4e458d237f0af08b0528
         * paytype : 2
         * account : 5.0
         * status : PENDING
         * timestamp : 0
         */

        private int ctid;
        private long createtime;
        private double fee;
        private String remake;
        private double bailmoney;
        private String userid;
        private int durationtime;
        private double total;
        private int deleted;
        private String phone;
        private double price;
        private String name;
        private String alipayaccount;
        private String alipayname;
        private String id;
        private String orderid;
        private String paytype;
        private double account;
        private String status;
        private int timestamp;
        private List<BankcardBean> bankcard;

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

        public List<BankcardBean> getBankcard() {
            return bankcard;
        }

        public void setBankcard(List<BankcardBean> bankcard) {
            this.bankcard = bankcard;
        }

        public static class BankcardBean implements Serializable{
            private String bankcardid;
            private String bankname;
            private String bankrealname;
            private String bankbranch;
            private String id;


            public String getBankrealname() {
                return bankrealname;
            }

            public void setBankrealname(String bankrealname) {
                this.bankrealname = bankrealname;
            }

            public String getBankbranch() {
                return bankbranch;
            }

            public void setBankbranch(String bankbranch) {
                this.bankbranch = bankbranch;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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


        }

        public String getOrderid() {
            return orderid;
        }

        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }

        public int getCtid() {
            return ctid;
        }

        public void setCtid(int ctid) {
            this.ctid = ctid;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public double getFee() {
            return fee;
        }

        public void setFee(double fee) {
            this.fee = fee;
        }

        public String getRemake() {
            return remake;
        }

        public void setRemake(String remake) {
            this.remake = remake;
        }

        public double getBailmoney() {
            return bailmoney;
        }

        public void setBailmoney(double bailmoney) {
            this.bailmoney = bailmoney;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public int getDurationtime() {
            return durationtime;
        }

        public void setDurationtime(int durationtime) {
            this.durationtime = durationtime;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public double getAccount() {
            return account;
        }

        public void setAccount(double account) {
            this.account = account;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }
    }





}
