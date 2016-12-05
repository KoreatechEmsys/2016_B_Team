package com.pelkan.tab;

import java.util.ArrayList;

/**
 * Created by admin on 2016-09-09.
 */

public class AllMindmapsList {
    private ArrayList<Mindmap> allMapList;
    private ArrayList<Integer> allMindmapsID;
    public AllMindmapsList(ArrayList<Mindmap> allMapList, ArrayList<Integer> allMindmapsID){
        this.allMapList = allMapList;
        this.allMindmapsID = allMindmapsID;
    }

    public ArrayList<Mindmap> getAllMapList() {
        return allMapList;
    }
    public ArrayList<Integer> getAllMindmapsID(){
        return allMindmapsID;
    }
}
