package com.atkj.ctc.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/4/27 0027.
 */

public class AllCurrencyBean implements Serializable{


    /**
     * msg : 操作成功
     * obj : [{"symbol":"USD","list":[{"symbol":"ETH/USD","ctid":20},{"symbol":"CTS/USD","ctid":22},{"symbol":"BTC/USD","ctid":19},{"symbol":"LTC/USD","ctid":21}]},{"symbol":"BTC","list":[{"symbol":"ETH/BTC","ctid":38}]},{"symbol":"ETH","list":[{"symbol":"BTC/ETH","ctid":23}]},{"symbol":"LTC","list":[{"symbol":"ETH/LTC","ctid":24},{"symbol":"BTC/LTC","ctid":25}]},{"symbol":"TEST002","list":[{"symbol":"TEST/TEST002","ctid":35}]}]
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
         * symbol : USD
         * list : [{"symbol":"ETH/USD","ctid":20},{"symbol":"CTS/USD","ctid":22},{"symbol":"BTC/USD","ctid":19},{"symbol":"LTC/USD","ctid":21}]
         */

        private String symbol;
        private List<ListBean> list;

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean implements Serializable{
            /**
             * symbol : ETH/USD
             * ctid : 20
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
}
