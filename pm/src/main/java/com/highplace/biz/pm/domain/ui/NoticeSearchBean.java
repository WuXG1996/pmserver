package com.highplace.biz.pm.domain.ui;

import java.util.Date;

public class NoticeSearchBean extends PageBean {

    private String title;

    private String type;

    private Date publishDateFrom;

    private Date publishDateTo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getPublishDateFrom() {
        return publishDateFrom;
    }

    public void setPublishDateFrom(Date publishDateFrom) {
        this.publishDateFrom = publishDateFrom;
    }

    public Date getPublishDateTo() {
        return publishDateTo;
    }

    public void setPublishDateTo(Date publishDateTo) {
        this.publishDateTo = publishDateTo;
    }

    @Override
    public String toString() {
        return "NoticeSearchBean{" +
                "title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", publishDateFrom=" + publishDateFrom +
                ", publishDateTo=" + publishDateTo +
                '}' +
                super.toString();
    }
}
