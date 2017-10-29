package com.pelkan.tab;

/**
 * Created by admin on 2016-09-20.
 */
public class ExamList {
    private String eid;
    private String startTime;
    private String endTime;
    private String eTitle;
    private String question_count;
    private String m_id;
    private String target;
    private boolean isChecked;

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTarget() {
        return target;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void seteTitle(String eTitle) {
        this.eTitle = eTitle;
    }

    public void setQuestion_count(String question_count) {
        this.question_count = question_count;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getEid() {
        return eid;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String geteTitle() {
        return eTitle;
    }

    public String getQuestion_count() {
        return question_count;
    }

    public String getM_id() {
        return m_id;
    }
}
