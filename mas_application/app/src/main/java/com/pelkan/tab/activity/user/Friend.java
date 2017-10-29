package com.pelkan.tab;

/**
 * Created by JangLab on 2016-09-18.
 */
public class Friend {
    private String id;
    private String img_url;
    private boolean isChecked;

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getId() {
        return id;
    }

    public String getImg_url() {
        return img_url;
    }
}
