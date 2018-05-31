package com.atkj.ctc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/16 0016.
 */

public class SellBean {


    /**
     * msg : 操作成功
     * obj : {"list":[{"durationtime":50,"orderid":"08d0035711754d3d9a893f449b8e14ee","phone":"12345678911","price":2,"name":"yyyy","paytype":"2","account":2}],"pageNum":1,"pageSize":20,"totalPage":1,"totalSize":1}
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
         * list : [{"durationtime":50,"orderid":"08d0035711754d3d9a893f449b8e14ee","phone":"12345678911","price":2,"name":"yyyy","paytype":"2","account":2}]
         * pageNum : 1
         * pageSize : 20
         * totalPage : 1
         * totalSize : 1
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

        public static class ListBean {
            /**
             * durationtime : 50
             * orderid : 08d0035711754d3d9a893f449b8e14ee
             * phone : 12345678911
             * price : 2.0
             * name : yyyy
             * paytype : 2
             * account : 2.0
             */

            private int durationtime;
            private String orderid;
            private String phone;
            private double price;
            private String name;
            private String paytype;
            private double account;

            public int getDurationtime() {
                return durationtime;
            }

            public void setDurationtime(int durationtime) {
                this.durationtime = durationtime;
            }

            public String getOrderid() {
                return orderid;
            }

            public void setOrderid(String orderid) {
                this.orderid = orderid;
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
        }
    }
}
