package com.atkj.ctc.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/4/28 0028.
 */

public class AssetsDetailBean {


    /**
     * msg : 操作成功
     * obj : {"list":[{"createtime":1523260817640,"id":119,"type":2,"account":1368.19,"status":1}],"pageNum":1,"pageSize":20,"totalPage":1,"totalSize":1}
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
         * list : [{"createtime":1523260817640,"id":119,"type":2,"account":1368.19,"status":1}]
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
             * createtime : 1523260817640
             * id : 119
             * type : 2
             * account : 1368.19
             * status : 1
             */

            private long createtime;
            private int id;
            private int type;
            private double account;
            private int status;

            public long getCreatetime() {
                return createtime;
            }

            public void setCreatetime(long createtime) {
                this.createtime = createtime;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public double getAccount() {
                return account;
            }

            public void setAccount(double account) {
                this.account = account;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
