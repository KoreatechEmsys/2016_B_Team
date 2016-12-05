package com.pelkan.tab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 2016-09-09.
 */
public class DBManager extends SQLiteOpenHelper {
    private final String LOG_TAG = "myLogs";
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "mapBD";
    // Tables
    private static final String TABLE_MINDMAPS = "mindmaps";
    private static final String TABLE_NODES = "nodes";
    // Mindmaps Table Columns names
    private static final String MAP_ID = "id";
    private static final String MAP_NAME = "name";
    private static final String MAP_DATE = "date";
    // Nodes Table Column names
    private static final String MINDMAP_ID = "mindmap_id";
    private static final String NODE_TEXT = "text";
    private static final String NODE_FORM = "form";
    private static final String NODE_BORDER = "border";
    private static final String NODE_COLOR = "color";
    private static final String NODE_MARKER = "marker";
    private static final String NODE_NUMBER = "number";
    private static final String NODE_PARENT_NUMBER = "parent_number";
    private static final String NODE_CENTER_X = "center_X";
    private static final String NODE_CENTER_Y = "center_Y";


    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "onCreate database");

        String CREATE_MAPS_TABLE = "CREATE TABLE "
                + TABLE_MINDMAPS + "("
                + MAP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MAP_NAME + " TEXT, "
                + MAP_DATE + " TIMESTAMP" + ");";
        db.execSQL(CREATE_MAPS_TABLE);
        String CREATE_NODES_TABLE =  "CREATE TABLE "
                + TABLE_NODES + "("
                + MINDMAP_ID + " INTEGER, "
                + NODE_TEXT + " TEXT, "
                + NODE_FORM + " TEXT, "
                + NODE_BORDER + " TEXT, "
                + NODE_COLOR + " INTEGER, "
                /*+ NODE_MARKER + "TEXT"*/
                + NODE_NUMBER + " INTEGER, "
                + NODE_PARENT_NUMBER + " INTEGER, "
                + NODE_CENTER_X + " INTEGER, "
                + NODE_CENTER_Y + " INTEGER"
                + ")";
        db.execSQL(CREATE_NODES_TABLE);

        // add "Getting started" mindmap
        ArrayList<Node> startNodes = new ArrayList<Node>();

        //마인드맵 초기화 여기서 db 정보를 받아와서 추가하자
        Node node = new Node("확률", "Rectangle", "Red", 0, -1, 100, 180);
        startNodes.add(new Node("확률", "Rectangle", "Red", 0, -1, 100, 180));

        ContentValues values = new ContentValues();
        /****************/
        values.put(MINDMAP_ID, 1);
        /*id of last insert: SELECT TOP 1 id FROM table ORDER BY id DESC;*/
        values.put(NODE_TEXT, "확률"); // Node text - Mindemap name !!!
        values.put(NODE_FORM, "Rectangle"); // Node form
        values.put(NODE_BORDER, "Red"); // Node border
        values.put(NODE_COLOR, node.getColor()); // Node color
        /* values.put(NODE_MARKER, node.getText()); // Node marker*/
        values.put(NODE_NUMBER, 0); // Node number
        values.put(NODE_PARENT_NUMBER, -1); // Node parent number
        values.put(NODE_CENTER_X, 100); // Node center coordinate X
        values.put(NODE_CENTER_Y, 180); // Node center coordinate Y
        long rowID = db.insert(TABLE_NODES, null, values);
        Log.d(LOG_TAG, "nodep inserted, ID = " + rowID);
        /****************/


        Mindmap gettingStartedMindmap = new Mindmap("확률", new Date(), startNodes);
        values = new ContentValues();
        values.put(MAP_NAME, gettingStartedMindmap.getName()); // Mindmap Name
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        values.put(MAP_DATE, dateFormat.format(gettingStartedMindmap.getDate())); // Mindmap date criation
        // Inserting Row
        rowID = db.insert(TABLE_MINDMAPS, null, values);
        Log.d(LOG_TAG, "map inserted, ID = " + rowID);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLES IF EXISTS " + TABLE_MINDMAPS + ", " + TABLE_NODES);
        Log.d(LOG_TAG, "!!!!!!!!!!!000000000000 work onUpgrade 000000000!!!!!!!!!!!!!!");
        // Create tables again
        //onCreate(db);
    }
    public void addMindmap(Mindmap mindmap){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MAP_NAME, mindmap.getName()); // Mindmap Name

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        values.put(MAP_DATE, dateFormat.format(mindmap.getDate())); // Mindmap date criation
        // Inserting Row
        long rowID = db.insert(TABLE_MINDMAPS, null, values);
        Log.d(LOG_TAG, "map inserted, ID = " + rowID);
        db.close(); // Closing database connection
    }
    public void addNode(int MAP_ID, Node node) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MINDMAP_ID, MAP_ID);
        /*id of last insert: SELECT TOP 1 id FROM table ORDER BY id DESC;*/
        values.put(NODE_TEXT, node.getText()); // Node text - Mindemap name !!!
        values.put(NODE_FORM, node.getForm()); // Node form
        values.put(NODE_BORDER, node.getBorder()); // Node border
        values.put(NODE_COLOR, node.getColor()); // Node color
        /* values.put(NODE_MARKER, node.getText()); // Node marker*/
        values.put(NODE_NUMBER, node.getNumber()); // Node number
        values.put(NODE_PARENT_NUMBER, node.getParentNodeNumber()); // Node parent number
        values.put(NODE_CENTER_X, node.getCenterX()); // Node center coordinate X
        values.put(NODE_CENTER_Y, node.getCenterY()); // Node center coordinate Y
        long rowID = db.insert(TABLE_NODES, null, values);
        Log.d(LOG_TAG, "Node inserted, ID = " + rowID);
        db.close(); // Closing database connection
    }

    public AllMindmapsList getAllMindmaps(){
        //모든 마인드맵을 검색해서 리스트에 뿌려줌
        ArrayList<Mindmap> mindmapList = new ArrayList<Mindmap>();
        ArrayList<Integer> allMindmapsID = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_MINDMAPS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        String[] keywords = {"수와연산", "문자와식", "함수", "기하", "확률과통계", "해석", "대수"};

        Date date = new Date();
        for(int i = 0; i < 7; i++) {
            allMindmapsID.add(i);
            String name = keywords[i];
//            date.setTime(Date.parse(c.getString(2)));
            Mindmap mindmap = new Mindmap(name, date, new ArrayList<Node>());
            mindmapList.add(mindmap);
        }

//        allMindmapsID.add(1);
//        String name = "확률";
//        Date date = new Date();
//        //date.setTime(Date.parse(c.getString(2)));
//        Mindmap mindmap = new Mindmap(name, date, new ArrayList<Node>());
//        mindmapList.add(mindmap);
//
//        allMindmapsID.add(2);
//        name = "분포";
//        Date date1 = new Date();
//        //date.setTime(Date.parse(c.getString(2)));
//        Mindmap mindmap1 = new Mindmap(name, date, new ArrayList<Node>());
//        mindmapList.add(mindmap1);
//        if (c.moveToFirst()) {
//            do {
//                int id = Integer.parseInt(c.getString(0));
//                allMindmapsID.add(id);
//                String name = c.getString(1);
//                Date date = new Date();
//                //date.setTime(Date.parse(c.getString(2)));
//                Mindmap mindmap = new Mindmap(name, date, new ArrayList<Node>());
//                mindmapList.add(mindmap);
//            } while (c.moveToNext());
//        } else Log.d("myLogs", "Error getAllMindmaps");
        return new AllMindmapsList(mindmapList, allMindmapsID);
    }
    public int getMindmapsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_MINDMAPS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        // return count
        return cursor.getCount();
    }

    public ArrayList<Node> getAllNodes(int MAP_ID){
        ArrayList<Node> nodesList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_NODES + " WHERE " + MINDMAP_ID + " = " + MAP_ID + ";";

        System.out.println("시작");
//        if(MAP_ID == 0) {                                                   //확률
//
//            Node nod = new Node("그래프");                        //노드 이름
//            nod.setForm("Rectangle");                              //모양
//            nod.setBorder("Red");                                  //경계선, 썡~
//            nod.setColor(Color.parseColor("#45ACCD"));             //색, 레벨에 따라 다르게 하자
//            nod.setNumber(1);                                      //노드 번호, root가 0번
//            nod.setParentNodeNumber(0);                           //부모노드, root면 -1
//            nod.setCenterX(550);                                   //x축
//            nod.setCenterY(100);                                   //y축
//            System.out.println("시작2");
//            nodesList.add(nod);
//
//            Node node1 = new Node("그래프의 정의");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.parseColor("#ff7f00"));
//            node1.setNumber(2);
//            node1.setParentNodeNumber(1);
//            node1.setCenterX(190);
//            node1.setCenterY(200);
//
//            nodesList.add(node1);
//
//            node1 = new Node("수형도");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.parseColor("#45ACCD"));
//            node1.setNumber(3);
//            node1.setParentNodeNumber(1);
//            node1.setCenterX(550);
//            node1.setCenterY(200);
//
//            nodesList.add(node1);
//        }

//        int startLine = 0;
//        int multiple = 0;
//        int flag = 0;
//        int temp_variable;
//        if (MAP_ID == 0) {
//            temp_variable = 0;
//        } else {
//            temp_variable = Start.type_size.get(MAP_ID -1);
//        }
//        for(int i = temp_variable; i < Start.type_size.get(MAP_ID); i++) {      //각 타입별 노드 수만큼 반복
//            switch (Start.maps.get(i).getWidth()) {
//                case 1:
//                    multiple = 0;
//                    startLine = 550;
//                    break;
//                case 2:
//                    multiple = 800;
//                    startLine = 100;
//                    break;
//                case 3:
//                    multiple = 300;
//                    startLine = 100;
//                    break;
//                case 4:
//                    multiple = 266;
//                    startLine = 100;
//                    break;
//                case 5:
//                    multiple = 200;
//                    startLine = 100;
//                    break;
//            }
//            if(Start.maps.get(i).getWidth() != 1 && Start.maps.get(i).getWidth() != Start.maps.get(i-1).getWidth())
//                flag = 0;
//            for (int j = 0; j < Start.maps.get(i).getParentNode().size(); j++) {
//                String temp;
//                temp = "";
//                Node node;
//                if(Start.maps.get(i).getName().length() > 6) {
//                    temp = Start.maps.get(i).getName().substring(0, 5);
//                    temp += "\n" + Start.maps.get(i).getName().substring(5, Start.maps.get(i).getName().length());
//                    node = new Node(temp);                        //노드 이름
//                } else {
//                    node = new Node(Start.maps.get(i).getName());                        //노드 이름
//                }
//                node.setForm("Rectangle");                              //모양
//                node.setBorder("Red");                                  //경계선, 썡~
//                node.setColor(Color.parseColor("#45ACCD"));             //색, 레벨에 따라 다르게 하자
//                node.setNumber(Start.maps.get(i).getId()-1);
//                node.setParentNodeNumber(Start.maps.get(i).getParentNode().get(j) - 1);
//                System.out.println("노드 이름 " + Start.maps.get(i).getName());
//                System.out.println("부모노드는 " + Start.maps.get(i).getParentNode().get(j));
//                System.out.println("길이는 " + startLine + " "+  multiple+ " "+  flag);
//                node.setCenterX(startLine + multiple * flag);
//                node.setCenterY(Start.maps.get(i).getDepth() * 100);                                   //y축
//                System.out.println("시작2");
//                nodesList.add(node);
//
//            }
//            flag++;
//        }

        if(MAP_ID == 0) {                                          //수와 연산

            Node node1 = new Node("소인수분해");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(99);
            node1.setParentNodeNumber(2);
            node1.setCenterX(550);
            node1.setCenterY(400);

            nodesList.add(node1);

            node1 = new Node("소인수분해");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(98);
            node1.setParentNodeNumber(3);
            node1.setCenterX(550);
            node1.setCenterY(400);

            nodesList.add(node1);
            Node node = new Node("수와연산");                        //노드 이름
            node.setForm("Rectangle");                              //모양
            node.setBorder("Red");                                  //경계선, 썡~
            node.setColor(Color.parseColor("#676768"));             //색, 레벨에 따라 다르게 하자
            node.setNumber(0);                                      //노드 번호, root가 0번
            node.setParentNodeNumber(-1);                           //부모노드, root면 -1
            node.setCenterX(550);                                   //x축
            node.setCenterY(100);                                   //y축
            System.out.println("시작2");
            nodesList.add(node);

            node1 = new Node("수의체계");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(1);
            node1.setParentNodeNumber(0);
            node1.setCenterX(550);
            node1.setCenterY(200);

            nodesList.add(node1);

            node1 = new Node("정수와유리수");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(2);
            node1.setParentNodeNumber(1);
            node1.setCenterX(150);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("유리수와순환소수");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(3);
            node1.setParentNodeNumber(1);
            node1.setCenterX(550);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("제곱근과실수");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(4);
            node1.setParentNodeNumber(1);
            node1.setCenterX(950);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("소인수분해");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(5);
            node1.setParentNodeNumber(4);
            node1.setCenterX(550);
            node1.setCenterY(400);

            nodesList.add(node1);

            node1 = new Node("집합과명제");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(6);
            node1.setParentNodeNumber(5);
            node1.setCenterX(550);
            node1.setCenterY(500);

            nodesList.add(node1);

            node1 = new Node("집합");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(7);
            node1.setParentNodeNumber(6);
            node1.setCenterX(150);
            node1.setCenterY(600);

            nodesList.add(node1);

            node1 = new Node("명제");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(8);
            node1.setParentNodeNumber(6);
            node1.setCenterX(950);
            node1.setCenterY(600);

            nodesList.add(node1);

            for(int i = 2; i < 11; i++) {
                float decisionColor = Start.maps.get(i -2).getGrade();
                String color;
                if(decisionColor > 7) {
                    color = "#45ACCD";
                } else if(decisionColor > 4) {
                    color = "#ff7f00";
                }else if(decisionColor > 0) {
                    color = "#ff3232";
                } else {
                    continue;
                }
                nodesList.get(i).setColor(Color.parseColor(color));
            }
        }

        if(MAP_ID == 1) {                                          //문자와식

            Node node1 = new Node("다항식의연산");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(99);
            node1.setParentNodeNumber(3);
            node1.setCenterX(350);
            node1.setCenterY(400);

            nodesList.add(node1);

            node1 = new Node("복소수와\n이차방정식");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(100);
            node1.setParentNodeNumber(7);
            node1.setCenterX(850);
            node1.setCenterY(500);

            nodesList.add(node1);

            node1 = new Node("다항식의연산");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(98);
            node1.setParentNodeNumber(4);
            node1.setCenterX(350);
            node1.setCenterY(400);

            nodesList.add(node1);

            Node node = new Node("문자와식");                        //노드 이름
            node.setForm("Rectangle");                              //모양
            node.setBorder("Red");                                  //경계선, 썡~
            node.setColor(Color.parseColor("#676768"));             //색, 레벨에 따라 다르게 하자
            node.setNumber(0);                                      //노드 번호, root가 0번
            node.setParentNodeNumber(-1);                           //부모노드, root면 -1
            node.setCenterX(550);                                   //x축
            node.setCenterY(100);                                   //y축
            nodesList.add(node);

            node1 = new Node("다항식");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(1);
            node1.setParentNodeNumber(0);
            node1.setCenterX(350);
            node1.setCenterY(200);

            nodesList.add(node1);

            node1 = new Node("방정식과부등식");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(2);
            node1.setParentNodeNumber(0);
            node1.setCenterX(850);
            node1.setCenterY(200);

            nodesList.add(node1);

            node1 = new Node("문자의사용식");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(3);
            node1.setParentNodeNumber(1);
            node1.setCenterX(150);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("식의계산");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(4);
            node1.setParentNodeNumber(1);
            node1.setCenterX(350);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("다항식의곱셈과\n인수분해");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(5);
            node1.setParentNodeNumber(1);
            node1.setCenterX(550);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("일차방정식");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(6);
            node1.setParentNodeNumber(2);
            node1.setCenterX(750);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("이차방정식");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(7);
            node1.setParentNodeNumber(2);
            node1.setCenterX(950);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("다항식의연산");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(8);
            node1.setParentNodeNumber(5);
            node1.setCenterX(350);
            node1.setCenterY(400);

            nodesList.add(node1);

            node1 = new Node("연립일차방정식");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(9);
            node1.setParentNodeNumber(6);
            node1.setCenterX(750);
            node1.setCenterY(400);

            nodesList.add(node1);

            node1 = new Node("나머지정리");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(10);
            node1.setParentNodeNumber(8);
            node1.setCenterX(350);
            node1.setCenterY(500);

            nodesList.add(node1);

            node1 = new Node("복소수와\n이차방정식");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(11);
            node1.setParentNodeNumber(9);
            node1.setCenterX(850);
            node1.setCenterY(500);

            nodesList.add(node1);

            node1 = new Node("인수분해");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(12);
            node1.setParentNodeNumber(10);
            node1.setCenterX(850);
            node1.setCenterY(600);

            nodesList.add(node1);

            node1 = new Node("이차방정식과\n이차함수");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(13);
            node1.setParentNodeNumber(11);
            node1.setCenterX(850);
            node1.setCenterY(600);

            nodesList.add(node1);

            for(int i = 3; i < 17; i++) {               //14개인데 3개가 무용지물
                float decisionColor = Start.maps.get(i + 9 - 3).getGrade();
                String color;
                if(decisionColor > 7) {
                    color = "#45ACCD";
                } else if(decisionColor > 4) {
                    color = "#ff7f00";
                }else if(decisionColor > 0) {
                    color = "#ff3232";
                } else {
                    continue;
                }
                nodesList.get(i).setColor(Color.parseColor(color));
            }

        }

        if(MAP_ID == 2) {                                          //함수
            Node node1 = new Node("유리함수와\n무리함수");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(69);
            node1.setParentNodeNumber(2);
            node1.setCenterX(550);
            node1.setCenterY(600);

            nodesList.add(node1);

            node1 = new Node("유리함수와\n무리함수");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(99);
            node1.setParentNodeNumber(5);
            node1.setCenterX(550);
            node1.setCenterY(600);

            nodesList.add(node1);
            Node node = new Node("함수");                        //노드 이름
            node.setForm("Rectangle");                              //모양
            node.setBorder("Red");                                  //경계선, 썡~
            node.setColor(Color.parseColor("#676768"));             //색, 레벨에 따라 다르게 하자
            node.setNumber(0);                                      //노드 번호, root가 0번
            node.setParentNodeNumber(-1);                           //부모노드, root면 -1
            node.setCenterX(550);                                   //x축
            node.setCenterY(100);                                   //y축
            System.out.println("시작2");
            nodesList.add(node);

            node1 = new Node("함수와그래프");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(1);
            node1.setParentNodeNumber(0);
            node1.setCenterX(550);
            node1.setCenterY(200);

            nodesList.add(node1);

            node1 = new Node("좌표평면과\n그래프");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(2);
            node1.setParentNodeNumber(1);
            node1.setCenterX(150);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("일차함수와\n그래프");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(3);
            node1.setParentNodeNumber(1);
            node1.setCenterX(550);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("이차함수와\n그래프");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(4);
            node1.setParentNodeNumber(1);
            node1.setCenterX(950);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("일차함수와\n일차방정식의관계");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(5);
            node1.setParentNodeNumber(3);
            node1.setCenterX(550);
            node1.setCenterY(400);

            nodesList.add(node1);

            node1 = new Node("유리함수와\n무리함수");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(6);
            node1.setParentNodeNumber(4);
            node1.setCenterX(550);
            node1.setCenterY(600);

            nodesList.add(node1);

            for(int i = 2; i < 9; i++) {               //14개인데 3개가 무용지물
                float decisionColor = Start.maps.get(i + 23 - 2).getGrade();
                String color;
                if(decisionColor > 7) {
                    color = "#45ACCD";
                } else if(decisionColor > 4) {
                    color = "#ff7f00";
                }else if(decisionColor > 0) {
                    color = "#ff3232";
                } else {
                    continue;
                }
                nodesList.get(i).setColor(Color.parseColor(color));
            }
        }

        if(MAP_ID == 3) {                                          //기하
            Node node1 = new Node("도형의방정식");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(99);
            node1.setParentNodeNumber(3);
            node1.setCenterX(550);
            node1.setCenterY(700);

            nodesList.add(node1);

            node1 = new Node("도형의방정식");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(98);
            node1.setParentNodeNumber(8);
            node1.setCenterX(550);
            node1.setCenterY(700);

            nodesList.add(node1);

            node1 = new Node("도형의방정식");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(97);
            node1.setParentNodeNumber(9);
            node1.setCenterX(550);
            node1.setCenterY(700);

            nodesList.add(node1);

            node1 = new Node("");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(77);
            node1.setParentNodeNumber(11);
            node1.setCenterX(550);
            node1.setCenterY(900);

            nodesList.add(node1);

            node1 = new Node("");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(76);
            node1.setParentNodeNumber(12);
            node1.setCenterX(550);
            node1.setCenterY(900);

            nodesList.add(node1);

            node1 = new Node("");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(75);
            node1.setParentNodeNumber(13);
            node1.setCenterX(550);
            node1.setCenterY(900);

            nodesList.add(node1);

            node1 = new Node("");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(74);
            node1.setParentNodeNumber(14);
            node1.setCenterX(550);
            node1.setCenterY(900);

            nodesList.add(node1);

            Node node = new Node("기하");                        //노드 이름
            node.setForm("Rectangle");                              //모양
            node.setBorder("Red");                                  //경계선, 썡~
            node.setColor(Color.parseColor("#676768"));             //색, 레벨에 따라 다르게 하자
            node.setNumber(0);                                      //노드 번호, root가 0번
            node.setParentNodeNumber(-1);                           //부모노드, root면 -1
            node.setCenterX(550);                                   //x축
            node.setCenterY(100);                                   //y축
            System.out.println("시작2");
            nodesList.add(node);

            node1 = new Node("평면도형");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(1);
            node1.setParentNodeNumber(0);
            node1.setCenterX(150);
            node1.setCenterY(200);

            nodesList.add(node1);

            node1 = new Node("입체도형");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(2);
            node1.setParentNodeNumber(0);
            node1.setCenterX(950);
            node1.setCenterY(200);

            nodesList.add(node1);

            node1 = new Node("기본도형");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(3);
            node1.setParentNodeNumber(1);
            node1.setCenterX(150);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("작도와합동");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(4);
            node1.setParentNodeNumber(1);
            node1.setCenterX(416);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("평면도형의\n성질");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(5);
            node1.setParentNodeNumber(1);
            node1.setCenterX(682);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("입체도형의성질");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(6);
            node1.setParentNodeNumber(2);
            node1.setCenterX(950);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("도형의닮음");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(7);
            node1.setParentNodeNumber(4);
            node1.setCenterX(416);
            node1.setCenterY(400);

            nodesList.add(node1);

            node1 = new Node("삼각형과사각형의\n성질");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(8);
            node1.setParentNodeNumber(5);
            node1.setCenterX(682);
            node1.setCenterY(400);

            nodesList.add(node1);

            node1 = new Node("피타고라스정리");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(9);
            node1.setParentNodeNumber(7);
            node1.setCenterX(416);
            node1.setCenterY(500);

            nodesList.add(node1);

            node1 = new Node("도형의방정식");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(10);
            node1.setParentNodeNumber(6);
            node1.setCenterX(550);
            node1.setCenterY(700);

            nodesList.add(node1);

            node1 = new Node("평면좌표");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(11);
            node1.setParentNodeNumber(10);
            node1.setCenterX(150);
            node1.setCenterY(800);

            nodesList.add(node1);

            node1 = new Node("직선의방정식");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(12);
            node1.setParentNodeNumber(10);
            node1.setCenterX(416);
            node1.setCenterY(800);

            nodesList.add(node1);

            node1 = new Node("원의방정식");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(13);
            node1.setParentNodeNumber(10);
            node1.setCenterX(682);
            node1.setCenterY(800);

            nodesList.add(node1);

            node1 = new Node("도형의이동");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(14);
            node1.setParentNodeNumber(10);
            node1.setCenterX(950);
            node1.setCenterY(800);

            nodesList.add(node1);

            node1 = new Node("이차곡선");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(15);
            node1.setParentNodeNumber(74);
            node1.setCenterX(150);
            node1.setCenterY(1000);

            nodesList.add(node1);

            node1 = new Node("평면벡터");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(16);
            node1.setParentNodeNumber(74);
            node1.setCenterX(550);
            node1.setCenterY(1000);

            nodesList.add(node1);

            node1 = new Node("공간도형과\n공간좌표");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(17);
            node1.setParentNodeNumber(74);
            node1.setCenterX(950);
            node1.setCenterY(1000);

            nodesList.add(node1);

            node1 = new Node("벡터의연산");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(18);
            node1.setParentNodeNumber(16);
            node1.setCenterX(550);
            node1.setCenterY(1100);

            nodesList.add(node1);

            node1 = new Node("직선과평면");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(19);
            node1.setParentNodeNumber(17);
            node1.setCenterX(950);
            node1.setCenterY(1100);

            nodesList.add(node1);

            node1 = new Node("평면벡터의\n성분과내적");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(20);
            node1.setParentNodeNumber(18);
            node1.setCenterX(550);
            node1.setCenterY(1200);

            nodesList.add(node1);

            node1 = new Node("정사영");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(21);
            node1.setParentNodeNumber(19);
            node1.setCenterX(950);
            node1.setCenterY(1200);

            nodesList.add(node1);

            node1 = new Node("공간좌표");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(22);
            node1.setParentNodeNumber(21);
            node1.setCenterX(950);
            node1.setCenterY(1300);

            nodesList.add(node1);

            for(int i = 7; i < 30; i++) {               //14개인데 3개가 무용지물
                float decisionColor = Start.maps.get(i + 30 - 7).getGrade();
                String color;
                if(decisionColor > 7) {
                    color = "#45ACCD";
                } else if(decisionColor > 4) {
                    color = "#ff7f00";
                }else if(decisionColor > 0) {
                    color = "#ff3232";
                } else {
                    continue;
                }
                nodesList.get(i).setColor(Color.parseColor(color));
            }

        }

        if(MAP_ID == 4) {                                          //확률과통계
            Node node1 = new Node("경우의수");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(99);
            node1.setParentNodeNumber(7);
            node1.setCenterX(950);
            node1.setCenterY(500);

            nodesList.add(node1);

            Node node = new Node("확률과 통계");                        //노드 이름
            node.setForm("Rectangle");                              //모양
            node.setBorder("Red");                                  //경계선, 썡~
            node.setColor(Color.parseColor("#676768"));             //색, 레벨에 따라 다르게 하자
            node.setNumber(0);                                      //노드 번호, root가 0번
            node.setParentNodeNumber(-1);                           //부모노드, root면 -1
            node.setCenterX(550);                                   //x축
            node.setCenterY(100);                                   //y축
            System.out.println("시작2");
            nodesList.add(node);

            node1 = new Node("확률");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(1);
            node1.setParentNodeNumber(0);
            node1.setCenterX(150);
            node1.setCenterY(200);

            nodesList.add(node1);

            node1 = new Node("통계");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(2);
            node1.setParentNodeNumber(0);
            node1.setCenterX(950);
            node1.setCenterY(200);

            nodesList.add(node1);

            node1 = new Node("확률과기본성질");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(3);
            node1.setParentNodeNumber(1);
            node1.setCenterX(150);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("자료의정리와\n해석");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(4);
            node1.setParentNodeNumber(2);
            node1.setCenterX(950);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("조건부확률");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(5);
            node1.setParentNodeNumber(3);
            node1.setCenterX(150);
            node1.setCenterY(400);

            nodesList.add(node1);

            node1 = new Node("대푯값과산포도");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(6);
            node1.setParentNodeNumber(4);
            node1.setCenterX(750);
            node1.setCenterY(400);

            nodesList.add(node1);

            node1 = new Node("상관관계");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(7);
            node1.setParentNodeNumber(4);
            node1.setCenterX(950);
            node1.setCenterY(400);

            nodesList.add(node1);

            node1 = new Node("경우의수");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(8);
            node1.setParentNodeNumber(6);
            node1.setCenterX(950);
            node1.setCenterY(500);

            nodesList.add(node1);

            node1 = new Node("순열과조합");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(9);
            node1.setParentNodeNumber(8);
            node1.setCenterX(950);
            node1.setCenterY(600);

            nodesList.add(node1);

            node1 = new Node("확률분포");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(10);
            node1.setParentNodeNumber(9);
            node1.setCenterX(950);
            node1.setCenterY(700);

            nodesList.add(node1);

            node1 = new Node("통계적추정");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(11);
            node1.setParentNodeNumber(10);
            node1.setCenterX(950);
            node1.setCenterY(800);

            nodesList.add(node1);

            for(int i = 1; i < 13; i++) {               //14개인데 3개가 무용지물
                float decisionColor = Start.maps.get(i + 53 - 1).getGrade();
                String color;
                if(decisionColor > 7) {
                    color = "#45ACCD";
                } else if(decisionColor > 4) {
                    color = "#ff7f00";
                }else if(decisionColor > 0) {
                    color = "#ff3232";
                } else {
                    continue;
                }
                nodesList.get(i).setColor(Color.parseColor(color));
            }
        }

        if(MAP_ID == 5) {                                          //해석
            Node node1 = new Node("");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(74);
            node1.setParentNodeNumber(2);
            node1.setCenterX(550);
            node1.setCenterY(400);

            nodesList.add(node1);

            node1 = new Node("");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(75);
            node1.setParentNodeNumber(3);
            node1.setCenterX(550);
            node1.setCenterY(400);

            nodesList.add(node1);

            Node node = new Node("해석");                        //노드 이름
            node.setForm("Rectangle");                              //모양
            node.setBorder("Red");                                  //경계선, 썡~
            node.setColor(Color.parseColor("#676768"));             //색, 레벨에 따라 다르게 하자
            node.setNumber(0);                                      //노드 번호, root가 0번
            node.setParentNodeNumber(-1);                           //부모노드, root면 -1
            node.setCenterX(550);                                   //x축
            node.setCenterY(100);                                   //y축
            System.out.println("시작2");
            nodesList.add(node);

            node1 = new Node("지수와로그");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(1);
            node1.setParentNodeNumber(0);
            node1.setCenterX(150);
            node1.setCenterY(200);

            nodesList.add(node1);

            node1 = new Node("삼각함수");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(2);
            node1.setParentNodeNumber(0);
            node1.setCenterX(950);
            node1.setCenterY(200);

            nodesList.add(node1);

            node1 = new Node("지수함수와\n로그함수");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(3);
            node1.setParentNodeNumber(1);
            node1.setCenterX(150);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("함수의극한과\n연속");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(4);
            node1.setParentNodeNumber(74);
            node1.setCenterX(150);
            node1.setCenterY(500);

            nodesList.add(node1);

            node1 = new Node("미분");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(5);
            node1.setParentNodeNumber(74);
            node1.setCenterX(550);
            node1.setCenterY(500);

            nodesList.add(node1);

            node1 = new Node("적분");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(6);
            node1.setParentNodeNumber(74);
            node1.setCenterX(950);
            node1.setCenterY(500);

            nodesList.add(node1);

            node1 = new Node("미분계수");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(7);
            node1.setParentNodeNumber(5);
            node1.setCenterX(150);
            node1.setCenterY(600);

            nodesList.add(node1);

            node1 = new Node("도함수");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(8);
            node1.setParentNodeNumber(5);
            node1.setCenterX(416);
            node1.setCenterY(600);

            nodesList.add(node1);

            node1 = new Node("부정적분");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(9);
            node1.setParentNodeNumber(6);
            node1.setCenterX(682);
            node1.setCenterY(600);

            nodesList.add(node1);

            node1 = new Node("정적분");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(10);
            node1.setParentNodeNumber(6);
            node1.setCenterX(950);
            node1.setCenterY(600);

            nodesList.add(node1);

            node1 = new Node("도함수의활용");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(11);
            node1.setParentNodeNumber(8);
            node1.setCenterX(416);
            node1.setCenterY(700);

            nodesList.add(node1);

            node1 = new Node("정적분의활용");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(12);
            node1.setParentNodeNumber(10);
            node1.setCenterX(950);
            node1.setCenterY(700);

            nodesList.add(node1);

            for(int i = 2; i < 15; i++) {               //14개인데 3개가 무용지물
                float decisionColor = Start.maps.get(i + 65 - 2).getGrade();
                String color;
                if(decisionColor > 7) {
                    color = "#45ACCD";
                } else if(decisionColor > 4) {
                    color = "#ff7f00";
                }else if(decisionColor > 0) {
                    color = "#ff3232";
                } else {
                    continue;
                }
                nodesList.get(i).setColor(Color.parseColor(color));
            }
        }

        if(MAP_ID == 6) {                                          //대수
            Node node = new Node("대수");                        //노드 이름
            node.setForm("Rectangle");                              //모양
            node.setBorder("Red");                                  //경계선, 썡~
            node.setColor(Color.parseColor("#676768"));             //색, 레벨에 따라 다르게 하자
            node.setNumber(0);                                      //노드 번호, root가 0번
            node.setParentNodeNumber(-1);                           //부모노드, root면 -1
            node.setCenterX(550);                                   //x축
            node.setCenterY(100);                                   //y축
            System.out.println("시작2");
            nodesList.add(node);

            Node node1 = new Node("수열");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(1);
            node1.setParentNodeNumber(0);
            node1.setCenterX(550);
            node1.setCenterY(200);

            nodesList.add(node1);

            node1 = new Node("등차수열과\n등비수열");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(2);
            node1.setParentNodeNumber(1);
            node1.setCenterX(550);
            node1.setCenterY(300);

            nodesList.add(node1);

            node1 = new Node("수열의합");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(3);
            node1.setParentNodeNumber(2);
            node1.setCenterX(550);
            node1.setCenterY(400);

            nodesList.add(node1);

            node1 = new Node("수학적귀납법");
            node1.setForm("Rectangle");
            node1.setBorder("Red");
            node1.setColor(Color.parseColor("#676768"));
            node1.setNumber(4);
            node1.setParentNodeNumber(3);
            node1.setCenterX(550);
            node1.setCenterY(500);

            nodesList.add(node1);

            for(int i = 0; i < 5; i++) {               //14개인데 3개가 무용지물
                float decisionColor = Start.maps.get(i + 78).getGrade();
                String color;
                if(decisionColor > 7) {
                    color = "#45ACCD";
                } else if(decisionColor > 4) {
                    color = "#ff7f00";
                }else if(decisionColor > 0) {
                    color = "#ff3232";
                } else {
                    continue;
                }
                nodesList.get(i).setColor(Color.parseColor(color));
            }
        }
//        if(MAP_ID == 1) {
//            Node node = new Node("그래프");                        //노드 이름
//            node.setForm("Rectangle");                              //모양
//            node.setBorder("Red");                                  //경계선, 썡~
//            node.setColor(Color.parseColor("#45ACCD"));             //색, 레벨에 따라 다르게 하자
//            node.setNumber(0);                                      //노드 번호, root가 0번
//            node.setParentNodeNumber(-1);                           //부모노드, root면 -1
//            node.setCenterX(550);                                   //x축
//            node.setCenterY(100);                                   //y축
//            System.out.println("시작2");
//            nodesList.add(node);
//
//            Node node1 = new Node("그래프의 정의");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.parseColor("#ff7f00"));
//            node1.setNumber(1);
//            node1.setParentNodeNumber(0);
//            node1.setCenterX(190);
//            node1.setCenterY(200);
//
//            nodesList.add(node1);
//
//            node1 = new Node("수형도");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.parseColor("#45ACCD"));
//            node1.setNumber(2);
//            node1.setParentNodeNumber(0);
//            node1.setCenterX(550);
//            node1.setCenterY(200);
//
//            nodesList.add(node1);
//
//            node1 = new Node("여러가지 회로");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.parseColor("#45ACCD"));
//            node1.setNumber(3);
//            node1.setParentNodeNumber(0);
//            node1.setCenterX(910);
//            node1.setCenterY(200);
//
//            nodesList.add(node1);
//
//            node1 = new Node("그래프의 활용");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.parseColor("#ff7f00"));
//            node1.setNumber(4);
//            node1.setParentNodeNumber(1);
//            node1.setCenterX(190);
//            node1.setCenterY(300);
//
//            nodesList.add(node1);
//
//            node1 = new Node("여러가지 수형도");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.parseColor("#ff7f00"));
//            node1.setNumber(5);
//            node1.setParentNodeNumber(2);
//            node1.setCenterX(550);
//            node1.setCenterY(300);
//
//            nodesList.add(node1);
//
//            node1 = new Node("헤밀턴 회로");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.parseColor("#45ACCD"));
//            node1.setNumber(6);
//            node1.setParentNodeNumber(3);
//            node1.setCenterX(910);
//            node1.setCenterY(300);
//
//            nodesList.add(node1);
//
//            node1 = new Node("여러가지 그래프");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.parseColor("#ff3232"));
//            node1.setNumber(7);
//            node1.setParentNodeNumber(4);
//            node1.setCenterX(190);
//            node1.setCenterY(400);
//
//            nodesList.add(node1);
//
//            node1 = new Node("행렬의 뜻");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.parseColor("#676768"));
//            node1.setNumber(8);
//            node1.setParentNodeNumber(4);
//            node1.setCenterX(430);
//            node1.setCenterY(400);
//
//            nodesList.add(node1);
//
//            node1 = new Node("생성수형도");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.parseColor("#ff3232"));
//            node1.setNumber(9);
//            node1.setParentNodeNumber(5);
//            node1.setCenterX(670);
//            node1.setCenterY(400);
//
//            nodesList.add(node1);
//
//            node1 = new Node("오일러 회로");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.parseColor("#ff7f00"));
//            node1.setNumber(10);
//            node1.setParentNodeNumber(6);
//            node1.setCenterX(910);
//            node1.setCenterY(400);
//
//            nodesList.add(node1);
//
//            node1 = new Node("그래프와 행렬");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.parseColor("#676768"));
//            node1.setNumber(11);
//            node1.setParentNodeNumber(8);
//            node1.setCenterX(430);
//            node1.setCenterY(500);
//
//            nodesList.add(node1);
//
//            node1 = new Node("그래프와 행렬");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.parseColor("#676768"));
//            node1.setNumber(12);
//            node1.setParentNodeNumber(7);
//            node1.setCenterX(430);
//            node1.setCenterY(500);
//
//            nodesList.add(node1);
//        }
//
//        if(MAP_ID == 2) {
//            Node node = new Node("1");
//            node.setForm("Rectangle");
//            node.setBorder("Red");
//            node.setColor(Color.YELLOW);
//            node.setNumber(0);
//            node.setParentNodeNumber(-1);
//            node.setCenterX(200);
//            node.setCenterY(180);
//            System.out.println("시작3");
//            nodesList.add(node);
//
//            Node node1 = new Node("2");
//            node1.setForm("Rectangle");
//            node1.setBorder("Red");
//            node1.setColor(Color.YELLOW);
//            node1.setNumber(1);
//            node1.setParentNodeNumber(0);
//            node1.setCenterX(400);
//            node1.setCenterY(180);
//
//            nodesList.add(node1);
//        }

//        if (c.moveToFirst()) {
//            do {
//                int id = Integer.parseInt(c.getString(0));
//                String text = c.getString(1);
//                String form = c.getString(2);
//                String border = c.getString(3);
//                int color = Integer.parseInt(c.getString(4));
//                int number = Integer.parseInt(c.getString(5));
//                int parentNumber = Integer.parseInt(c.getString(6));
//                int x = Integer.parseInt(c.getString(7));
//                int y = Integer.parseInt(c.getString(8));
//
//                Node node = new Node(text);
//                node.setForm(form);
//                node.setBorder(border);
//                node.setColor(color);
//                node.setNumber(number);
//                node.setParentNodeNumber(parentNumber);
//                node.setCenterX(x);
//                node.setCenterY(y);
//
//                nodesList.add(node);
//            } while (c.moveToNext());
//        } else Log.d("myLogs", "Error getAllNodes");

        return nodesList;
    }

    public String getMindmapById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] selectionArgs = new String[]{String.valueOf(id)};
        Cursor cursor = db.query(TABLE_MINDMAPS,
                null,
                MAP_ID + " = ?",
                selectionArgs,
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Log.d(LOG_TAG, "!!!!!" + cursor.getCount());
        String mindmapName = cursor.getString(1);
        return mindmapName;
    }

    public int updateMindmap(Mindmap mindmap){
        return 0;
    }
    public int updateNode(int MAP_ID, Node node){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MINDMAP_ID, MAP_ID);
        values.put(NODE_TEXT, node.getText());
        values.put(NODE_FORM, node.getForm());
        values.put(NODE_BORDER, node.getBorder());
        values.put(NODE_COLOR, node.getColor()); // Node color
        /*values.put(NODE_MARKER, node.getText()); // Node marker*/
        values.put(NODE_NUMBER, node.getNumber());
        values.put(NODE_PARENT_NUMBER, node.getParentNodeNumber());
        values.put(NODE_CENTER_X, node.getCenterX());
        values.put(NODE_CENTER_Y, node.getCenterY());

        return db.update(TABLE_NODES, values, MINDMAP_ID  + " = ? AND " + NODE_NUMBER + " = ?" ,
                new String[] {String.valueOf(MAP_ID), String.valueOf(node.getNumber())});
    }

    public void deleteNode(Node node, int idMindmap){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NODES, MINDMAP_ID + " = " + idMindmap + " AND " + NODE_NUMBER + " = ?", new String[] { String.valueOf(node.getNumber()) });
        db.close();
    }
    public void deleteMindmap(int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Node> allNodes = getAllNodes(ID);
        for(int i = 0; i < allNodes.size(); i++){
            db.delete(TABLE_NODES, MINDMAP_ID + " = ?", new String[] { String.valueOf(ID) });
        }
        db.delete(TABLE_MINDMAPS, MAP_ID + " = ?", new String[] { String.valueOf(ID) });
        db.close();
    }
}
