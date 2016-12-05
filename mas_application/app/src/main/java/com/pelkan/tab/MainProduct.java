package com.pelkan.tab;

/**
 * Created by JangLab on 2016-07-13.
 */
public class MainProduct {
    private String q_id;
    private String keywords;
    private String level;
    private String viewCount;
    private String reponseCount;
    private String image_url;
    private String title;
    private String successCount;
    private String addDate;
    private String responseCount;

    public String getQ_id() {
        return q_id;
    }

    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public void setReponseCount(String reponseCount) {
        this.reponseCount = reponseCount;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSuccessCount(String successCount) {
        this.successCount = successCount;
    }

    public void setResponseCount(String responseCount) {
        this.responseCount = responseCount;
    }

    public String getTitle() {
        return title;
    }

    public String getSuccessCount() {
        return successCount;
    }

    public String getResponseCount() {
        return responseCount;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getKeywords() {
        return keywords;
    }

    public String getLevel() {
        return level;
    }

    public String getViewCount() {
        return viewCount;
    }

    public String getReponseCount() {
        return reponseCount;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

}
