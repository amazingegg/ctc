package com.atkj.ctc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11 0011.
 */

public class OrderDetailBean {

    /**
     * msg : 操作成功
     * obj : {"selluserid":"7f588851b67944338f0c73c5721186c0","total":"25.0000","createtime":1523354195908,"orderno":"VEA1523354195908","hour":0,"phone":"12345678911","name":"yyyy","remake":"","paytype":"2","sellstatus":"TRANSATION","minute":33,"second":52}
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
         * selluserid : 7f588851b67944338f0c73c5721186c0
         * total : 25.0000
         * createtime : 1523354195908
         * orderno : VEA1523354195908
         * hour : 0
         * phone : 12345678911
         * name : yyyy
         * remake :
         * paytype : 2
         * sellstatus : TRANSATION
         * minute : 33
         * second : 52
         */

        private String selluserid;
        private String buyuserid;
        private String total;
        private long createtime;
        private String orderno;
        private int hour;
        private String sellphone;
        private String buyphone;
        private String buyname;
        private String sellname;
        private String remake;
        private String paytype;
        private String sellstatus;
        private String alipayaccount;
        private String alipayname;
        private int minute;
        private int second;
        private List<BankcardBean> bankcard;

        public String getBuyuserid() {
            return buyuserid;
        }

        public void setBuyuserid(String buyuserid) {
            this.buyuserid = buyuserid;
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

            public String getBankrealname() {
                return bankrealname;
            }

            public void setBankrealname(String bankrealname) {
                this.bankrealname = bankrealname;
            }
        }

        public String getBuyname() {
            return buyname;
        }

        public void setBuyname(String buyname) {
            this.buyname = buyname;
        }

        public String getSellname() {
            return sellname;
        }

        public void setSellname(String sellname) {
            this.sellname = sellname;
        }

        public String getAlipayaccount() {
            return alipayaccount;
        }

        public void setAlipayaccount(String alipayaccount) {
            this.alipayaccount = alipayaccount;
        }

        public String getSelluserid() {
            return selluserid;
        }

        public void setSelluserid(String selluserid) {
            this.selluserid = selluserid;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(long createtime) {
            this.createtime = createtime;
        }

        public String getOrderno() {
            return orderno;
        }

        public void setOrderno(String orderno) {
            this.orderno = orderno;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public String getSellphone() {
            return sellphone;
        }

        public void setSellphone(String sellphone) {
            this.sellphone = sellphone;
        }

        public String getBuyphone() {
            return buyphone;
        }

        public void setBuyphone(String buyphone) {
            this.buyphone = buyphone;
        }

        public String getRemake() {
            return remake;
        }

        public void setRemake(String remake) {
            this.remake = remake;
        }

        public String getPaytype() {
            return paytype;
        }

        public void setPaytype(String paytype) {
            this.paytype = paytype;
        }

        public String getSellstatus() {
            return sellstatus;
        }

        public void setSellstatus(String sellstatus) {
            this.sellstatus = sellstatus;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public int getSecond() {
            return second;
        }

        public void setSecond(int second) {
            this.second = second;
        }
    }
}
