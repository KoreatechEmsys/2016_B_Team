package com.pelkan.tab;

/**
 * Created by admin on 2016-09-20.
 */
public class ExamResponse {
    private String eid;     //시험번호
    private String qid;     //어느 문제에 대한 풀이인가
    private String m_id;    //풀이 등록자
    private String content; //풀이 내용
    private String img_url; //풀이 이미지 url
    private String ori_qid;
    private int is_correct;

    public int getIs_correct() {
        return is_correct;
    }

    public void setIs_correct(int is_correct) {
        this.is_correct = is_correct;
    }

    public void setOri_qid(String ori_qid) {
        this.ori_qid = ori_qid;
    }

    public String getOri_qid() {
        return ori_qid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getEid() {
        return eid;
    }

    public String getQid() {
        return qid;
    }

    public String getM_id() {
        return m_id;
    }

    public String getContent() {
        return content;
    }

    public String getImg_url() {
        return img_url;
    }
}
