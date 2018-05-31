package com.atkj.ctc.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

public class UserBean {


    /**
     * msg : 登陆成功
     * obj : {"account":10000,"areacode":"86","bailmoney":0,"createtime":1513326105092,"id":"40c3504c46c14bd0bd173799f8a3b46a","lasttime":1513646425599,"passsalt":"1287","password":"3423e10735e43221835b567a0ffa9534","phone":"13923417234","status":0,"unaccount":0,"updatetime":1513388534136}
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
         * account : 10000.0
         * areacode : 86
         * bailmoney : 0.0
         * createtime : 1513326105092
         * id : 40c3504c46c14bd0bd173799f8a3b46a
         * lasttime : 1513646425599
         * passsalt : 1287
         * password : 3423e10735e43221835b567a0ffa9534
         * phone : 13923417234
         * status : 0
         * unaccount : 0.0
         * updatetime : 1513388534136
         */

        private double account;
        private String areacode;
        private double bailmoney;
        private long createtime;
        private String id;
        private long lasttime;
        private String passsalt;
        private String password;
        private String paypassword;
        private String phone;
        private int status;
        private double unaccount;
        private long updatetime;
        private String username;
        private String uid;
        private String alipayaccount;
        private String wechatid;
        private String alipayname;
        private String realname;
        private int vipstatus;//1普通会员 2vip
        private long vipendtime;//vip结束时间，普通会员这个字段是空
        private List<BankcardBean> bankcard;
        private String token = "";


        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getVipstatus() {
            return vipstatus;
        }

        public void setVipstatus(int vipstatus) {
            this.vipstatus = vipstatus;
        }

        public long getVipendtime() {
            return vipendtime;
        }

        public void setVipendtime(long vipendtime) {
            this.vipendtime = vipendtime;
        }

        public List<BankcardBean> getBankcard() {
            return bankcard;
        }

        public void setBankcard(List<BankcardBean> bankcard) {
            this.bankcard = bankcard;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getAlipayaccount() {
            return alipayaccount;
        }

        public void setAlipayaccount(String alipayaccount) {
            this.alipayaccount = alipayaccount;
        }

        public String getWechatid() {
            return wechatid;
        }

        public void setWechatid(String wechatid) {
            this.wechatid = wechatid;
        }

        public String getAlipayname() {
            return alipayname;
        }

        public void setAlipayname(String alipayname) {
            this.alipayname = alipayname;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPaypassword() {
            return paypassword;
        }

        public void setPaypassword(String paypassword) {
            this.paypassword = paypassword;
        }

        public double getAccount() {
            return account;
        }

        public void setAccount(double account) {
            this.account = account;
        }

        public String getAreacode() {
            return areacode;
        }

        public void setAreacode(String areacode) {
            this.areacode = areacode;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getLasttime() {
            return lasttime;
        }

        public void setLasttime(long lasttime) {
            this.lasttime = lasttime;
        }

        public String getPasssalt() {
            return passsalt;
        }

        public void setPasssalt(String passsalt) {
            this.passsalt = passsalt;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public double getUnaccount() {
            return unaccount;
        }

        public void setUnaccount(double unaccount) {
            this.unaccount = unaccount;
        }

        public long getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(long updatetime) {
            this.updatetime = updatetime;
        }


        public static class BankcardBean{
            private String bankcardid;
            private String bankname;
            private String realname;
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

            public String getRealname() {
                return realname;
            }

            public void setRealname(String realname) {
                this.realname = realname;
            }
        }


    }
}
