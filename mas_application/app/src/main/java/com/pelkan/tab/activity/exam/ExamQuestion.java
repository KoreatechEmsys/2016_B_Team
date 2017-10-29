package com.pelkan.tab;

/**
 * Created by admin on 2016-09-20.
 */
public class ExamQuestion {
    private String qid;
    private String img_url;
    private String title;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQid() {
        return qid;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getTitle() {
        return title;
    }
}
