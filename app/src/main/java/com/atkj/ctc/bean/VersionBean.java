package com.atkj.ctc.bean;

/**
 * Created by Administrator on 2017/12/29 0029.
 */

public class VersionBean {


    /**
     * msg : 操作成功
     * obj : {"clienttype":2,"createtime":1514530838554,"deleted":1,"filename":"app-debug.apk","id":12,"prompt":"","remake":"","status":1,"title":"订单","url":"http://120.77.83.36:8070/upload/20171229150032/app-debug.apk","version":"2"}
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
         * clienttype : 2
         * createtime : 1514530838554
         * deleted : 1
         * filename : app-debug.apk
         * id : 12
         * prompt :
         * remake :
         * status : 1
         * title : 订单
         * url : http://120.77.83.36:8070/upload/20171229150032/app-debug.apk
         * version : 2
         */

        private int clienttype;
        private long createtime;
        private int deleted;
        private String filename;
        private int id;
        private String prompt;
        private String remake;
        private int status;
        private String title;
        private String url;
        private float version;

        public int getClienttype() {
            return clienttype;
        }

        public void setClienttype(int clienttype) {
            this.clienttype = clienttype;
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

        public String getPrompt() {
            return prompt;
        }

        public void setPrompt(String prompt) {
            this.prompt = prompt;
        }

        public String getRemake() {
            return remake;
        }

        public void setRemake(String remake) {
            this.remake = remake;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public float getVersion() {
            return version;
        }

        public void setVersion(float version) {
            this.version = version;
        }
    }
}
