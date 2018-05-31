package com.atkj.ctc.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class VipBean {

    /**
     * msg : 操作成功
     * obj : [{"vipstatus":1,"level":"VIP","inviteno":0,"poorno":1}]
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
         * vipstatus : 1
         * level : VIP
         * inviteno : 0
         * poorno : 1
         */

        private int vipstatus;
        private String level;
        private int inviteno;
        private int poorno;
        private long vipendtime;

        public long getVipendtime() {
            return vipendtime;
        }

        public void setVipendtime(long vipendtime) {
            this.vipendtime = vipendtime;
        }

        public int getVipstatus() {
            return vipstatus;
        }

        public void setVipstatus(int vipstatus) {
            this.vipstatus = vipstatus;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public int getInviteno() {
            return inviteno;
        }

        public void setInviteno(int inviteno) {
            this.inviteno = inviteno;
        }

        public int getPoorno() {
            return poorno;
        }

        public void setPoorno(int poorno) {
            this.poorno = poorno;
        }
    }
}
