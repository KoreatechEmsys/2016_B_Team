package com.pelkan.tab;

/**
 * Created by JangLab on 2016-07-30.
 */
public class QuestionList {
    private String keywords;
    private String responseCount;
    private String viewCount;
    private String addDate;
    private String qid;
    private String m_id;
    private String title;
    private String success_count;
    private boolean isChecked;

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSuccess_count(String success_count) {
        this.success_count = success_count;
    }

    public String getM_id() {
        return m_id;
    }

    public String getTitle() {
        return title;
    }

    public String getSuccess_count() {
        return success_count;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setResponseCount(String responseCount) {
        this.responseCount = responseCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getResponseCount() {
        return responseCount;
    }

    public String getViewCount() {
        return viewCount;
    }

    public String getAddDate() {
        return addDate;
    }
}
