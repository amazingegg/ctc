package com.atkj.ctc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

public class OrderBean implements Serializable{

    /**
     * msg : 操作成功
     * obj : {"list":[{"account":100,"bailmoney":2,"buystatus":"CANCEL","buyuserid":"40c3504c46c14bd0bd173799f8a3b46a","createtime":1513754349794,"deleted":0,"durationtime":50,"fee":1,"id":"ffeb72059314402cbecceb6a427ee155","orderStatus":4,"orderno":"YBT1513754349794","paytype":1,"price":3.2,"sellstatus":"CANCEL","selluserid":"432437b1ba434ed0a7dd52d244c5ce77","transactiontime":1513757279685,"type":1,"username":"18085195871"},{"account":22,"bailmoney":2,"buystatus":"CANCEL","buyuserid":"40c3504c46c14bd0bd173799f8a3b46a","createtime":1513735086465,"deleted":0,"durationtime":33,"fee":0.22,"id":"183c553bdb974088ac08ddcdb836873f","orderStatus":4,"orderno":"QOJ1513735086466","paytype":2,"price":222,"remake":"11","sellstatus":"CANCEL","selluserid":"40c3504c46c14bd0bd173799f8a3b46a","transactiontime":1513758090527,"type":2,"username":"13923417234"}],"pageNum":1,"pageSize":20,"totalPage":1,"totalSize":2}
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
         * list : [{"account":100,"bailmoney":2,"buystatus":"CANCEL","buyuserid":"40c3504c46c14bd0bd173799f8a3b46a","createtime":1513754349794,"deleted":0,"durationtime":50,"fee":1,"id":"ffeb72059314402cbecceb6a427ee155","orderStatus":4,"orderno":"YBT1513754349794","paytype":1,"price":3.2,"sellstatus":"CANCEL","selluserid":"432437b1ba434ed0a7dd52d244c5ce77","transactiontime":1513757279685,"type":1,"username":"18085195871"},{"account":22,"bailmoney":2,"buystatus":"CANCEL","buyuserid":"40c3504c46c14bd0bd173799f8a3b46a","createtime":1513735086465,"deleted":0,"durationtime":33,"fee":0.22,"id":"183c553bdb974088ac08ddcdb836873f","orderStatus":4,"orderno":"QOJ1513735086466","paytype":2,"price":222,"remake":"11","sellstatus":"CANCEL","selluserid":"40c3504c46c14bd0bd173799f8a3b46a","transactiontime":1513758090527,"type":2,"username":"13923417234"}]
         * pageNum : 1
         * pageSize : 20
         * totalPage : 1
         * totalSize : 2
         */

        private int pageNum;
        private int pageSize;
        private int totalPage;
        private int totalSize;
        private List<ListBean> list;

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(int totalSize) {
            this.totalSize = totalSize;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Serializable {
            /**
             * account : 100.0
             * bailmoney : 2.0
             * buystatus : CANCEL
             * buyuserid : 40c3504c46c14bd0bd173799f8a3b46a
             * createtime : 1513754349794
             * deleted : 0
             * durationtime : 50
             * fee : 1.0
             * id : ffeb72059314402cbecceb6a427ee155
             * orderStatus : 4
             * orderno : YBT1513754349794
             * paytype : 1
             * price : 3.2
             * sellstatus : CANCEL
             * selluserid : 432437b1ba434ed0a7dd52d244c5ce77
             * transactiontime : 1513757279685
             * type : 1
             * username : 18085195871
             * remake : 11
             */

            private double account;
            private double bailmoney;
            private String buystatus;
            private String buyuserid;
            private long createtime;
            private int deleted;
            private int durationtime;
            private long endtime;
            private double fee;
            private String id;
            private int orderStatus;
            private String orderno;
            private String paytype;
            private double price;
            private String sellstatus;
            private String selluserid;
            private long transactiontime;
            private int type;
            private String buyname;
            private String sellname;
            private String remake;
            private String phone;
            private String alipayaccount;
            private String alipayname;
            private String wechatid;
            private String bankcardid;
            private String bankname;
            private String bankrealname;
            private String realname;
            private String symbol;
            private String total;
            private String orderid;

            public String getOrderid() {
                return orderid;
            }

            public void setOrderid(String orderid) {
                this.orderid = orderid;
            }

            public String getTotal() {
                return total;
            }

            public void setTotal(String total) {
                this.total = total;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
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

            public String getBankrealname() {
                return bankrealname;
            }

            public void setBankrealname(String bankrealname) {
                this.bankrealname = bankrealname;
            }

            public String getRealname() {
                return realname;
            }

            public void setRealname(String realname) {
                this.realname = realname;
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

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public long getEndtime() {
                return endtime;
            }

            public void setEndtime(long endtime) {
                this.endtime = endtime;
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

            public long getTransactiontime() {
                return transactiontime;
            }

            public void setTransactiontime(long transactiontime) {
                this.transactiontime = transactiontime;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getRemake() {
                return remake;
            }

            public void setRemake(String remake) {
                this.remake = remake;
            }
        }
    }
}
