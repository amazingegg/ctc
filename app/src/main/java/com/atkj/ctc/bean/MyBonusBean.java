package com.atkj.ctc.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class MyBonusBean {


    /**
     * msg : 操作成功
     * obj : {"list":[{"createtime":1523432929184,"type":"幸运奖","account":0.66,"username":"幸运奖","status":1},{"createtime":1522823328516,"type":"幸运奖","account":0.66,"username":"幸运奖","status":1},{"createtime":1522823185085,"type":"幸运奖","account":0.66,"username":"幸运奖","status":1}],"pageNum":1,"pageSize":20,"totalPage":1,"totalSize":3}
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
         * list : [{"createtime":1523432929184,"type":"幸运奖","account":0.66,"username":"幸运奖","status":1},{"createtime":1522823328516,"type":"幸运奖","account":0.66,"username":"幸运奖","status":1},{"createtime":1522823185085,"type":"幸运奖","account":0.66,"username":"幸运奖","status":1}]
         * pageNum : 1
         * pageSize : 20
         * totalPage : 1
         * totalSize : 3
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
             * createtime : 1523432929184
             * type : 幸运奖
             * account : 0.66
             * username : 幸运奖
             * status : 1
             */

            private long createtime;
            private String type;
            private double account;
            private String username;
            private int status;

            public long getCreatetime() {
                return createtime;
            }

            public void setCreatetime(long createtime) {
                this.createtime = createtime;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public double getAccount() {
                return account;
            }

            public void setAccount(double account) {
                this.account = account;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
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
