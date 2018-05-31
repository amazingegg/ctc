package com.atkj.ctc.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class MyIntegralBean {
    private String remake;
    private String content;
    private String point;
    private int status;//状态，1-添加积分，2-减少积分，3-失效积分


    public String getRemake() {
        return remake;
    }

    public void setRemake(String remake) {
        this.remake = remake;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
