package com.atkj.ctc.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class CurrencyBean {

    /**
     * msg : 操作成功
     * obj : [{"symbol":"π/usd","ctid":1},{"symbol":"BTC/usd","ctid":2}]
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
         * symbol : π/usd
         * ctid : 1
         */

        private String symbol;
        private int ctid;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public int getCtid() {
            return ctid;
        }

        public void setCtid(int ctid) {
            this.ctid = ctid;
        }
    }
}
