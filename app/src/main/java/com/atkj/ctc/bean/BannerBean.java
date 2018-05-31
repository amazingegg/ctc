package com.atkj.ctc.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/2 0002.
 */

public class BannerBean {

    /**
     * msg : 操作成功
     * obj : [{"filename":"12.jpg","id":5,"url":"http://120.77.83.36:8060/upload/20180102094908/12.jpg"},{"filename":"5c.jpg","id":4,"url":"http://120.77.83.36:8060/upload/20180102094839/5c.jpg"}]
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
         * filename : 12.jpg
         * id : 5
         * url : http://120.77.83.36:8060/upload/20180102094908/12.jpg
         */

        private String filename;
        private int id;
        private String url;
        private String path;


        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
