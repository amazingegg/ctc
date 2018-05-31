package com.atkj.ctc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/26 0026.
 */

public class SellBean2 implements Serializable{


    /**
     * msg : 操作成功
     * obj : [{"account":100,"bailmoney":2,"createtime":1514196005473,"deleted":0,"durationtime":30,"fee":1,"id":"4f05327611374d019b4c2915cf0b060f","orderStatus":0,"orderno":"YHF1514196005473","paytype":1,"price":16,"sellstatus":"PENDING","selluserid":"7eb27c0328df4c0ea1e48c799b609946","type":1,"username":"牛逼"},{"account":10,"bailmoney":2,"createtime":1514195659617,"deleted":0,"durationtime":30,"fee":0.1,"id":"9092083660b54883a95851d7536c4641","orderStatus":0,"orderno":"JOZ1514195659618","paytype":1,"price":100.1,"sellstatus":"PENDING","selluserid":"456827b4468e4e1090068314ab5bbf61","type":1,"username":"gffg"}]
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

    public static class ObjBean implements Serializable{
        /**
         * account : 100.0
         * bailmoney : 2.0
         * createtime : 1514196005473
         * deleted : 0
         * durationtime : 30
         * fee : 1.0
         * id : 4f05327611374d019b4c2915cf0b060f
         * orderStatus : 0
         * orderno : YHF1514196005473
         * paytype : 1
         * price : 16.0
         * sellstatus : PENDING
         * selluserid : 7eb27c0328df4c0ea1e48c799b609946
         * type : 1
         * username : 牛逼
         */

        private double account;
        private double bailmoney;
        private long createtime;
        private int deleted;
        private int durationtime;
        private double fee;
        private String id;
        private int orderStatus;
        private String orderno;
        private String paytype;
        private double price;
        private String sellstatus;
        private String selluserid;
        private String buystatus;
        private String buyuserid;
        private int type;
        private String sellname;
        private String buyname;

        public String getSellname() {
            return sellname;
        }

        public void setSellname(String sellname) {
            this.sellname = sellname;
        }

        public String getBuyname() {
            return buyname;
        }

        public void setBuyname(String buyname) {
            this.buyname = buyname;
        }

        public String getBuystatus() {
            return buystatus;
        }

        public void setBuystatus(String buystatus) {
            this.buystatus = buystatus;
        }

        public String getBuyuserid() {
            return buyuserid;
        }

        public void setBuyuserid(String buyuserid) {
            this.buyuserid = buyuserid;
        }

        public double getAccount() {
            return account;
        }

        public void setAccount(double account) {
            this.account = account;
        }

        public double getBailmoney() {
            return bailmoney;
        }

        public void setBailmoney(double bailmoney) {
            this.bailmoney = bailmoney;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public int getDeleted() {
            return deleted;
        }

        public void setDeleted(int deleted) {
            this.deleted = deleted;
        }

        public int getDurationtime() {
            return durationtime;
        }

        public void setDurationtime(int durationtime) {
            this.durationtime = durationtime;
        }

        public double getFee() {
            return fee;
        }

        public void setFee(double fee) {
            this.fee = fee;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderno() {
            return orderno;
        }

        public void setOrderno(String orderno) {
            this.orderno = orderno;
        }

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getSellstatus() {
            return sellstatus;
        }

        public void setSellstatus(String sellstatus) {
            this.sellstatus = sellstatus;
        }

        public String getSelluserid() {
            return selluserid;
        }

        public void setSelluserid(String selluserid) {
            this.selluserid = selluserid;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

    }
}
