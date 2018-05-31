package com.atkj.ctc.bean;

/**
 * Created by Administrator on 2018/4/27 0027.
 */

public class CurrencyIntroductionBean {


    /**
     * msg : 操作成功
     * obj : {"blockurl":"https://blockchain.i","circulationaccount":"200,000,00","createtime":0,"currencyid":23,"id":1,"issuedaccount":"120,000,000","issuedtime":"2008-06-01","price":"1USD","remake":"<p><strong><span style=\"color: rgb(14, 23, 38); font-family: &quot;PingFang SC&quot;, -apple-system, BlinkMacSystemFont, Roboto, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Hiragino Sans GB&quot;, &quot;Source Han Sans&quot;, &quot;Noto Sans CJK Sc&quot;, &quot;Microsoft YaHei&quot;, &quot;Microsoft Jhenghei&quot;, sans-serif; font-size: 14px; background-color: rgb(244, 247, 253);\">比特币（Bitcoin，简称BTC）<\/span><\/strong><span style=\"color: rgb(14, 23, 38); font-family: &quot;PingFang SC&quot;, -apple-system, BlinkMacSystemFont, Roboto, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Hiragino Sans GB&quot;, &quot;Source Han Sans&quot;, &quot;Noto Sans CJK Sc&quot;, &quot;Microsoft YaHei&quot;, &quot;Microsoft Jhenghei&quot;, sans-serif; font-size: 14px; background-color: rgb(244, 247, 253);\">是目前使用最为广泛的一 种数字货币，它诞生于2009年1月3日，是一种点对点 （P2P）传输的数字加密货币，总量2100万枚。比特币 网络每10分钟释放出一定数量币，预计在2014年达到极 限。比特币被投资者称为\u201c数字黄金\u201d。比特币依据特定 算法，通过大量的计算产生，不依靠特定货币机构发行 其使用整个P2P网络中众多节点构成的分布式数据库来 确认并记录所有的交易行为，并使用密码学设计确保货 币流通各个环节安全性，可确保无法通过大量制造比特 币来人为操控币值。基于密码学的设计可以使比特币只 能被真实拥有者转移、支付及兑现。同样确保了货币所 有权与流通交易的匿名性。<\/span><\/p>","title":"\u200b比特币（Bitcoin，简称BTC）","url":"https://bitcoin.org/"}
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
         * blockurl : https://blockchain.i
         * circulationaccount : 200,000,00
         * createtime : 0
         * currencyid : 23
         * id : 1
         * issuedaccount : 120,000,000
         * issuedtime : 2008-06-01
         * price : 1USD
         * remake : <p><strong><span style="color: rgb(14, 23, 38); font-family: &quot;PingFang SC&quot;, -apple-system, BlinkMacSystemFont, Roboto, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Hiragino Sans GB&quot;, &quot;Source Han Sans&quot;, &quot;Noto Sans CJK Sc&quot;, &quot;Microsoft YaHei&quot;, &quot;Microsoft Jhenghei&quot;, sans-serif; font-size: 14px; background-color: rgb(244, 247, 253);">比特币（Bitcoin，简称BTC）</span></strong><span style="color: rgb(14, 23, 38); font-family: &quot;PingFang SC&quot;, -apple-system, BlinkMacSystemFont, Roboto, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Hiragino Sans GB&quot;, &quot;Source Han Sans&quot;, &quot;Noto Sans CJK Sc&quot;, &quot;Microsoft YaHei&quot;, &quot;Microsoft Jhenghei&quot;, sans-serif; font-size: 14px; background-color: rgb(244, 247, 253);">是目前使用最为广泛的一 种数字货币，它诞生于2009年1月3日，是一种点对点 （P2P）传输的数字加密货币，总量2100万枚。比特币 网络每10分钟释放出一定数量币，预计在2014年达到极 限。比特币被投资者称为“数字黄金”。比特币依据特定 算法，通过大量的计算产生，不依靠特定货币机构发行 其使用整个P2P网络中众多节点构成的分布式数据库来 确认并记录所有的交易行为，并使用密码学设计确保货 币流通各个环节安全性，可确保无法通过大量制造比特 币来人为操控币值。基于密码学的设计可以使比特币只 能被真实拥有者转移、支付及兑现。同样确保了货币所 有权与流通交易的匿名性。</span></p>
         * title : ​比特币（Bitcoin，简称BTC）
         * url : https://bitcoin.org/
         */

        private String blockurl;
        private String circulationaccount;
        private String createtime;
        private int currencyid;
        private int id;
        private String issuedaccount;
        private String issuedtime;
        private String price;
        private String remake;
        private String title;
        private String url;

        public String getBlockurl() {
            return blockurl;
        }

        public void setBlockurl(String blockurl) {
            this.blockurl = blockurl;
        }

        public String getCirculationaccount() {
            return circulationaccount;
        }

        public void setCirculationaccount(String circulationaccount) {
            this.circulationaccount = circulationaccount;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public int getCurrencyid() {
            return currencyid;
        }

        public void setCurrencyid(int currencyid) {
            this.currencyid = currencyid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIssuedaccount() {
            return issuedaccount;
        }

        public void setIssuedaccount(String issuedaccount) {
            this.issuedaccount = issuedaccount;
        }

        public String getIssuedtime() {
            return issuedtime;
        }

        public void setIssuedtime(String issuedtime) {
            this.issuedtime = issuedtime;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getRemake() {
            return remake;
        }

        public void setRemake(String remake) {
            this.remake = remake;
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
    }
}
