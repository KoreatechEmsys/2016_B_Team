package com.pelkan.tab;

import java.util.ArrayList;

/**
 * Created by JangLab on 2016-09-20.
 */
public class KnowledgeMap {
    private String name;                            //키워드 이름
    private int id;                                 //노드번호, 타입마다 1번씩 새롭게 세는 번호임
    private ArrayList<Integer> parentNode;          //부모 노드
    private ArrayList<Integer> childNode;           //자식노드
    private String type;                            //타입이름
    private int typeNum;                            //타입 번호
    private int depth;
    private int width;
    private float grade;

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setParentNode(ArrayList<Integer> parentNode) {
        this.parentNode = parentNode;
    }

    public void setChildNode(ArrayList<Integer> childNode) {
        this.childNode = childNode;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTypeNum(int typeNum) {
        this.typeNum = typeNum;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getParentNode() {
        return parentNode;
    }

    public ArrayList<Integer> getChildNode() {
        return childNode;
    }

    public String getType() {
        return type;
    }

    public int getTypeNum() {
        return typeNum;
    }
}
