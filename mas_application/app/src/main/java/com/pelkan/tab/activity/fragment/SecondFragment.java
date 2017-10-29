package com.pelkan.tab;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by admin on 2016-09-09.
 */
//마인드맵 뿌리기
public class SecondFragment extends Fragment implements View.OnTouchListener {
    private static final int REQUEST_CODE_UPDATE_TEXT = 1;
    private static final int REQUEST_CODE_NEW_NODE = 2;
    private static final int REQUEST_CODE_COLOR = 3;
    private static final int REQUEST_CODE_FORM = 4;

    private final String[] listPopup = {"+ new", "Form", "Border"};
    private final String LOG_TAG = "myLogs";
    private String mName;
    private RelativeLayout myCanvas;
    private ImageButton imgBtn;
    private View mainView;
    private View selected_item = null;
    private int offset_x = 0;
    private int offset_y = 0;
    private Boolean touchFlag = false;
    private int idMindmap;
    private ArrayList<Node> allNodeForMindmap;
    private ArrayList<NodeView> listNodeView;
    private NodeView nodePaint;
    private DrawLine newLine;
    private ArrayList<DrawLine> listLines;
    private DBManager helper;
    private HashSet<Node> listNodesToDelete;
    private int tempCounter = 0;
    private PopupMenu popupMenu;
    private ArrayList<Integer> maxHeight = new ArrayList<Integer>();

    protected HorizontalBarChart mChart;
    protected Typeface mTfLight;


    BarDataSet dataset = null;
    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<String>();

    public SecondFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //get mindmap name from from firstfragment list or from dialog
        Bundle extras = getArguments();
        if (extras != null) {
            idMindmap = extras.getInt("idMindmap");
        } else idMindmap = -1;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.secondfragment, null);
        //set mindmapname
        myCanvas = (RelativeLayout) mainView.findViewById(R.id.canvas);
        //myCanvas.setOnTouchListener(m_onTouchListener);                       //터치 리스너
        // DB
        helper = new DBManager(getContext());
        maxHeight.clear();
        maxHeight.add(2);
        maxHeight.add(2);
        maxHeight.add(5);
        maxHeight.add(2);
        maxHeight.add(4);
        maxHeight.add(4);
        maxHeight.add(1);
        maxHeight.add(2);
        maxHeight.add(3);
        maxHeight.add(3);
        maxHeight.add(5);
        maxHeight.add(5);
        maxHeight.add(5);
        maxHeight.add(3);
        maxHeight.add(6);
        maxHeight.add(4);

        allNodeForMindmap = helper.getAllNodes(idMindmap);
        for (Node node : allNodeForMindmap) {
            Log.d(LOG_TAG, "allNodeForMindmap: " + node.getText() + ", ");
        }
        mName = String.valueOf(idMindmap);
        //mName = helper.getMindmapById(idMindmap);         임시
        listNodesToDelete = new HashSet<>();
        paintAllNode(allNodeForMindmap);                                            //노드와 라인 draw
        mChart = (HorizontalBarChart) mainView.findViewById(R.id.chart1);
        int temp = 0;
        temp = 2000;
        switch (idMindmap) {
            case 0:
                temp = 750;
                break;
            case 1:
                temp = 850;
                break;
            case 2:
                temp = 850;
                break;
            case 3:
                temp = 1450;
                break;
            case 4:
                temp = 950;
                break;
            case 5:
                temp = 850;
                break;
            case 6:
                temp = 750;
                break;
        }
        myCanvas.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, temp));

        BarData data = new BarData(getXAxisValues(), getDataSet());
        mChart.setData(data);
        mChart.animateXY(2000, 2000);
        mChart.invalidate();
        mChart.setDescription("");


//        mChart.setDrawBarShadow(false);
//
//        mChart.setDrawValueAboveBar(true);
//
//        mChart.setDescription("");
//
//        // if more than 60 entries are displayed in the chart, no values will be
//        // drawn
//        mChart.setMaxVisibleValueCount(60);
//
//        // scaling can now only be done on x- and y-axis separately
//        mChart.setPinchZoom(false);
//
//        // draw shadows for each bar that show the maximum value
//        // mChart.setDrawBarShadow(true);
//
//        mChart.setDrawGridBackground(false);
//
//        XAxis xl = mChart.getXAxis();
//        xl.setPosition(XAxisPosition.BOTTOM);
//        xl.setTypeface(mTfLight);
//        xl.setDrawAxisLine(true);
//        xl.setDrawGridLines(false);
//        xl.setGranularity(10f);
//
//        YAxis yl = mChart.getAxisLeft();
//        yl.setTypeface(mTfLight);
//        yl.setDrawAxisLine(true);
//        yl.setDrawGridLines(true);
//        yl.setAxisMinValue(0f);
////        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
////        yl.setInverted(true);
//
//        YAxis yr = mChart.getAxisRight();
//        yr.setTypeface(mTfLight);
//        yr.setDrawAxisLine(true);
//        yr.setDrawGridLines(false);
//        yr.setAxisMinValue(0f);
////        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
////        yr.setInverted(true);
//
//        setData(10, 100);
//        mChart.setFitBars(true);
//        mChart.animateY(2500);
//        Legend l = mChart.getLegend();
//        l.setPosition(LegendPosition.BELOW_CHART_LEFT);
//        l.setFormSize(8f);
//        l.setXEntrySpace(4f);


        return mainView;
    }

    private ArrayList<IBarDataSet> getDataSet() {
        ArrayList<IBarDataSet> dataSets = null;
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        if(idMindmap == 0) {
            for(int i = 2; i < allNodeForMindmap.size(); i++) {
                colors.add(allNodeForMindmap.get(i).getColor());
                BarEntry v1e1 = new BarEntry(Start.maps.get(i - 2).getGrade(), i - 2); // Jan
                valueSet1.add(v1e1);
            }
        } else if(idMindmap == 1) {
            for(int i = 3; i < allNodeForMindmap.size(); i++) {
                colors.add(allNodeForMindmap.get(i).getColor());
                BarEntry v1e1 = new BarEntry(Start.maps.get(i + 9 - 3).getGrade(), i - 3); // Jan
                valueSet1.add(v1e1);
            }
        } else if(idMindmap == 2) {
            for(int i = 2; i < allNodeForMindmap.size(); i++) {
                colors.add(allNodeForMindmap.get(i).getColor());
                BarEntry v1e1 = new BarEntry(Start.maps.get(i + 23 - 2).getGrade(), i - 2); // Jan
                valueSet1.add(v1e1);
            }
        }else if(idMindmap == 3) {
            for(int i = 7; i < allNodeForMindmap.size(); i++) {
                colors.add(allNodeForMindmap.get(i).getColor());
                BarEntry v1e1 = new BarEntry(Start.maps.get(i + 30 - 7).getGrade(), i - 7); // Jan
                valueSet1.add(v1e1);
            }
        }else if(idMindmap == 4) {
            for(int i = 1; i < allNodeForMindmap.size(); i++) {
                colors.add(allNodeForMindmap.get(i).getColor());
                BarEntry v1e1 = new BarEntry(Start.maps.get(i + 53 - 1).getGrade(), i - 1); // Jan
                valueSet1.add(v1e1);
            }
        }else if(idMindmap == 5) {
            for(int i = 2; i < allNodeForMindmap.size(); i++) {
                colors.add(allNodeForMindmap.get(i).getColor());
                BarEntry v1e1 = new BarEntry(Start.maps.get(i + 65 - 2).getGrade(), i - 2); // Jan
                valueSet1.add(v1e1);
            }
        }else if(idMindmap == 6) {
            for(int i = 0; i < allNodeForMindmap.size(); i++) {
                colors.add(allNodeForMindmap.get(i).getColor());
                BarEntry v1e1 = new BarEntry(Start.maps.get(i + 78).getGrade(), i); // Jan
                valueSet1.add(v1e1);
            }
        }
//        BarEntry v1e1 = new BarEntry(10, 0); // Jan
//        valueSet1.add(v1e1);
//        BarEntry v1e2 = new BarEntry(7, 1); // Feb
//        valueSet1.add(v1e2);
//        BarEntry v1e3 = new BarEntry(10, 2); // Mar
//        valueSet1.add(v1e3);
//        BarEntry v1e4 = new BarEntry(10, 3); // Apr
//        valueSet1.add(v1e4);
//        BarEntry v1e5 = new BarEntry(6, 4); // May
//        valueSet1.add(v1e5);
//        BarEntry v1e6 = new BarEntry(6, 5); // Jun
//        valueSet1.add(v1e6);
//        BarEntry v1e7 = new BarEntry(10, 6); // Jun
//        valueSet1.add(v1e7);
//        BarEntry v1e8 = new BarEntry(2, 7); // Jun
//        valueSet1.add(v1e8);
//        BarEntry v1e9 = new BarEntry(0, 8); // Jun
//        valueSet1.add(v1e9);
//        BarEntry v1e10 = new BarEntry(3, 9); // Jun
//        valueSet1.add(v1e10);
//        BarEntry v1e11 = new BarEntry(5, 10); // Jun
//        valueSet1.add(v1e11);
//        BarEntry v1e12 = new BarEntry(0, 11); // Jun
//        valueSet1.add(v1e12);


        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Level");
//        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Brand 1");
        barDataSet1.setColors(colors);                      //색 배열 넣기
//        barDataSet1.setColor(Color.rgb(0, 155, 0));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();

        if(idMindmap == 0) {
            for(int i = 2; i < allNodeForMindmap.size(); i++) {
                xAxis.add(allNodeForMindmap.get(i).getText());
            }
        } else if(idMindmap == 1) {
            for(int i = 3; i < allNodeForMindmap.size(); i++) {
                xAxis.add(allNodeForMindmap.get(i).getText());
            }
        } else if(idMindmap == 2) {
            for(int i = 2; i < allNodeForMindmap.size(); i++) {
                xAxis.add(allNodeForMindmap.get(i).getText());
            }
        }else if(idMindmap == 3) {
            for(int i = 7; i < allNodeForMindmap.size(); i++) {
                xAxis.add(allNodeForMindmap.get(i).getText());
            }
        }else if(idMindmap == 4) {
            for(int i = 1; i < allNodeForMindmap.size(); i++) {
                xAxis.add(allNodeForMindmap.get(i).getText());
            }
        }else if(idMindmap == 5) {
            for(int i = 2; i < allNodeForMindmap.size(); i++) {
                xAxis.add(allNodeForMindmap.get(i).getText());
            }
        }else if(idMindmap == 6) {
            for(int i = 0; i < allNodeForMindmap.size(); i++) {
                xAxis.add(allNodeForMindmap.get(i).getText());
            }
        }

        return xAxis;
    }

//    private void setData(int count, float range) {                  //데이터 삽입        count = y축, 키워드이름, range = 최대값
//
//        float barWidth = 9f;
//        float spaceForBar = 50f;                                                //바 간의 간격, 키워드간 간격
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//
//        for (int i = 0; i < count; i++) {
//            float val = (float) (Math.random() * range);                    //val 키워드에 해당하는 점수
//            yVals1.add(new BarEntry(i * spaceForBar, val));
//        }
//        BarDataSet set1;
//
//        if (mChart.getData() != null &&
//                mChart.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet)mChart.getData().getDataSetByIndex(0);
//            set1.setValues(yVals1);
//            mChart.getData().notifyDataChanged();
//            mChart.notifyDataSetChanged();
//        } else {
//            set1 = new BarDataSet(yVals1, "DataSet 1");
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//            dataSets.add(set1);
//
//            BarData data = new BarData(dataSets);
//            data.setValueTextSize(10f);
//            data.setValueTypeface(mTfLight);
//            data.setValueTypeface(mTfLight);
//            data.setBarWidth(barWidth);
//            mChart.setData(data);
//        }
//    }

    public void paintViewNode(Node node){
        nodePaint = new NodeView(getContext(), node);
        nodePaint.setId(node.getNumber());
        nodePaint.setOnTouchListener(this);
        listNodeView.add(nodePaint);
        myCanvas.addView(nodePaint);
        //margins view
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        lp.setMargins(node.getCenterX() - nodePaint.side/2, node.getCenterY() - nodePaint.lineHight/2, 0, 0);
        nodePaint.setLayoutParams(lp);
//        myCanvas.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        myCanvas.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, temp));
    }

    public void paintNodeWithLine(Node node){
        paintViewNode(node);
        //draw the lines
        if (node.getNumber() != 0) {
            newLine = drawLineToParentNode(getContext(), node.getNumber());
            newLine.setId(nodePaint.getId());
            myCanvas.addView(newLine);
            listLines.add(newLine);
        }
    }

    public void paintAllNode(ArrayList<Node> allNodeForMindmap) {
        listNodeView = new ArrayList<>();
        listLines = new ArrayList<>();
        for(Node node : allNodeForMindmap) {                                        //노드간 라인 그리기
            if (node.getNumber() != 0) {
                newLine = drawLineToParentNode(getContext(), node.getNumber());
                newLine.setId(node.getNumber());
                myCanvas.addView(newLine);
                listLines.add(newLine);
            }
        }
        for(Node node : allNodeForMindmap) {                                        //모든 노드 그리기
            if(!(73 < node.getNumber() && node.getNumber() < 78)) {            //빈 노드일경우가 아니면
                paintViewNode(node);
            }
        }
    }
//
//    View.OnTouchListener m_onTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            Log.d(LOG_TAG, "m_onTouchListener working");
//            if (touchFlag) {
//                switch (event.getActionMasked()) {
//                    case MotionEvent.ACTION_DOWN:
//                        /*popupMenu = new PopupMenu(getContext(), selected_item);
//                        //popupMenu.setOnMenuItemClickListener(getContext());
//                        popupMenu.inflate(R.menu.popup_menu);
//                        popupMenu.show();*/
//                        //showPopupMenu(selected_item);     노드 누르고있으면
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//                        int selectedViewId = selected_item.getId();
//                        float evX = event.getX();
//                        float evY = event.getY();
//                        int x = (int) evX - offset_x;
//                        int y = (int) evY - offset_y;
//
//                        //save new x,y to Node in nodesList
//                        updateXandYInAllNodesList(allNodeForMindmap, selectedViewId, x, y);
//                        //move line from childNode to parentNode
//                        if (selectedViewId != 0){
//                            myCanvas.removeView(listLines.get(selectedViewId-1));
//                            listLines.set(selectedViewId - 1, newLine = drawLineToParentNode(getContext(), selectedViewId));
//                            myCanvas.addView(newLine);
//                        }
//                        //move line from parentNode to childNode
//                        for (int nodeNumber : findIdAllChildNodes(selectedViewId)) {
//                            myCanvas.removeView(listLines.get(nodeNumber-1));// nodeNumber ?????
//                            listLines.set(nodeNumber-1, newLine = drawLineToParentNode(getContext(), nodeNumber));
//                            myCanvas.addView(newLine);
//                        }
//                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                                new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//                        lp.setMargins(x, y, 0, 0);
//                        selected_item.setLayoutParams(lp);
//                        selected_item.invalidate();
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        Handler handler = new Handler();
//                        //
//                        handler.postDelayed(new Runnable() {
//                            public void run() {
//                                popupMenu.dismiss();
//                            }}, 3000);
//                        touchFlag = false;
//                        break;
//                    default:
//                        break;
//                }
//            }
//            return true;
//        }
//
//    };

    private void addView() {
        /*TextView tv = new TextView(getContext());
        tv.setText("Drug me !!!");
        tv.setOnTouchListener(this);
        canvas.addView(tv);*/

        /*NodeView zeroNode = new NodeView(getContext(), mName);
        zeroNode.setOnTouchListener(this);
        canvas.addView(zeroNode);*/

    }
    public boolean onTouch(View v, MotionEvent event) {
        Log.d(LOG_TAG, "method onTouch working");
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(LOG_TAG, "ACTION_DOWN !!!!!");
                touchFlag = true;
                offset_x = (int) event.getX();
                offset_y = (int) event.getY();
                selected_item = v;
                break;
            case MotionEvent.ACTION_UP:
                Log.d(LOG_TAG, "ACTION_UP !!!!!");
                selected_item = null;
                touchFlag = false;
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // save all  nodes changes
        for(Node node : allNodeForMindmap) {
            helper.updateNode(idMindmap, node);
        }
        Log.d(LOG_TAG, "onDestroy !!");
        Log.d(LOG_TAG, "Count onStart method = " + tempCounter);
    }

    public Node findParentNode(Node nodeChild){
        for(Node node : allNodeForMindmap) {
            if(nodeChild.getParentNodeNumber() == node.getNumber()) {
                return node;
            }
        }
        return null;
    }
    public ArrayList<Integer> findIdAllChildNodes(int parentNodeNumber){
        ArrayList<Integer> nodesId = new ArrayList<>();
        for(Node node : allNodeForMindmap) {
            if(node.getParentNodeNumber() == parentNodeNumber) {
                nodesId.add(node.getNumber());
            }
        }
        return nodesId;
    }
    public ArrayList<Node> findAllChildNodes(Node parentNode){
        ArrayList<Node> childNodesList = new ArrayList<>();
        for(Node node : allNodeForMindmap) {
            if(node.getParentNodeNumber() == parentNode.getNumber()) {
                childNodesList.add(node);
            }
        }
        return childNodesList;
    }
    public Node findNodeByNodeNumber(int nodeNumber) {
        for(Node node : allNodeForMindmap) {
            if(nodeNumber == node.getNumber()) {
                return node;
            }
        }
        return null;
    }
    public DrawLine drawLineToParentNode(Context context, int nodeViewNumber) {
        Node node1 = findNodeByNodeNumber(nodeViewNumber);
        Node node2 = findParentNode(node1);
        Log.d(LOG_TAG, "x = " + node1.getCenterX() + ", " + "y = " + node1.getCenterY());
        DrawLine newLine = new DrawLine(context, node1, node2);
        return newLine;
    }

    @Override
    public void onStart() {
        tempCounter++;
        super.onStart();
    }
//
//    public void updateXandYInAllNodesList(ArrayList<Node> allNodeForMindmap, int nodeNumber, int x, int y) {
//        for (int i = 0; i < allNodeForMindmap.size(); i++) {
//            if (allNodeForMindmap.get(i).getNumber() == nodeNumber) {
//                allNodeForMindmap.get(i).setCenterX(x);
//                allNodeForMindmap.get(i).setCenterY(y);
//            }
//        }
//    }
//    public void updateTextNode(ArrayList<Node> allNodeForMindmap, int nodeNumber, String newText) {
//        for (int i = 0; i < allNodeForMindmap.size(); i++) {
//            if (allNodeForMindmap.get(i).getNumber() == nodeNumber) {
//                allNodeForMindmap.get(i).setText(newText);
//            }
//        }
//    }
//    public void updateNodeColor(ArrayList<Node> allNodeForMindmap, int nodeNumber, int newBackgroundColorFromPicker){
//        for (int i = 0; i < allNodeForMindmap.size(); i++) {
//            if (allNodeForMindmap.get(i).getNumber() == nodeNumber) {
//                allNodeForMindmap.get(i).setColor(newBackgroundColorFromPicker);
//            }
//        }
//    }
//    public void updateNodeForm(ArrayList<Node> allNodeForMindmap, int nodeNumber, String newForm){
//        for (int i = 0; i < allNodeForMindmap.size(); i++) {
//            if (allNodeForMindmap.get(i).getNumber() == nodeNumber) {
//                allNodeForMindmap.get(i).setForm(newForm);
//            }
//        }
//    }
//    public void getListNodesToDelete(ArrayList<Node> allNodeForMindmap, int nodeNumberToDelete){
//        if(nodeNumberToDelete == 0){
//            Toast.makeText(getContext(), "Can't delete !!",
//                    Toast.LENGTH_SHORT).show();
//        } else {
//            for(Node node : allNodeForMindmap){
//                if(node.getNumber() == nodeNumberToDelete) {
//                    ArrayList<Node> childNodesList = findAllChildNodes(node);
//                    if(childNodesList.size() != 0) {
//                        for (Node childNode : childNodesList){
//                            listNodesToDelete.add(childNode);
//                            getListNodesToDelete(allNodeForMindmap, childNode.getNumber());
//                        }
//                    }
//                    listNodesToDelete.add(node);
//                }
//            }
//        }
//    }
//    private void deleteNodes(ArrayList<Node> allNodeForMindmap, HashSet<Node> listNodesToDelete){
//        Iterator<Node> itr = listNodesToDelete.iterator();
//        ArrayList<NodeView> viewListToDelete = new ArrayList();
//        while (itr.hasNext()) {
//            Node nodeToDelete = itr.next();
//            helper.deleteNode(nodeToDelete, idMindmap);
//            allNodeForMindmap.remove(nodeToDelete);
//            for(NodeView nodePaint : listNodeView) {
//                if(nodePaint.getId() == nodeToDelete.getNumber())
//                {viewListToDelete.add(nodePaint);}
//            }
//        }
//        for(NodeView nv: viewListToDelete){
//            listNodeView.remove(nv);
//        }
//    }
//    public ArrayList<Node> addNewNodeInList(Node node){
//        allNodeForMindmap.add(node);
//        return allNodeForMindmap;
//    }
//
//    public void updateViewNode(int nodeNumber){
//        int viewNumber = nodeNumber;
//        for (int i = 0; i < listNodeView.size(); i++) {
//            if (listNodeView.get(i).getId() == viewNumber) {
//                myCanvas.removeView(listNodeView.get(viewNumber));
//                Node nodeForNewView = findNodeByNodeNumber(nodeNumber);
//                NodeView newViewNode = new NodeView(getContext(), nodeForNewView);
//                newViewNode.setId(nodeForNewView.getNumber());
//                newViewNode.setOnTouchListener(this);
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                        new ViewGroup.MarginLayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//                lp.setMargins(nodeForNewView.getCenterX(), nodeForNewView.getCenterY(), 0, 0);
//                newViewNode.setLayoutParams(lp);
//                newViewNode.invalidate();
//                listNodeView.set(viewNumber, newViewNode);
//                myCanvas.addView(newViewNode);
//            }
//        }
//    }
//    private void showPopupMenu(View v) {                      노드 추가하거나 삭제하는것 누르고 있으면 이벤트
//        popupMenu = new PopupMenu(getContext(), v);
//        popupMenu.inflate(R.menu.popup_menu);
//        popupMenu
//                .setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.item1:
//                                openNewNodeDialog();
//                                //paintNewNodeView();
//                                return true;
//                            case R.id.item2:
//                                openTextDialog();
//                                return true;
//                            case R.id.item3:
//                                openFormDialog();
//                                return true;
//                            case R.id.item4:
//                                openColorPickDialog();
//                                return true;
//                            case R.id.item5:
//                                getListNodesToDelete(allNodeForMindmap, selected_item.getId());
//                                deleteNodes(allNodeForMindmap, listNodesToDelete);
//                                myCanvas.removeAllViews();
//                                paintAllNode(allNodeForMindmap);
//                                return true;
//                            default:
//                                return false;
//                        }
//                    }
//
//                });
//
//        /*popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
//
//            @Override
//            public void onDismiss(PopupMenu menu) {
//                Toast.makeText(getContext(), "onDismiss",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });*/
//        popupMenu.show();
//    }

//    public void openTextDialog() {
//        DialogFragment fragment = new DialogText();
//        fragment.setTargetFragment(this, REQUEST_CODE_UPDATE_TEXT);
//        fragment.show(getFragmentManager(), fragment.getClass().getName());
//        Bundle b = new Bundle();
//        b.putString("NodeOldText", findNodeByNodeNumber(selected_item.getId()).getText());
//        fragment.setArguments(b);
//    }
//    public void openNewNodeDialog() {
//        DialogFragment fragment = new DialogNewNode();
//        fragment.setTargetFragment(this, REQUEST_CODE_NEW_NODE);
//        fragment.show(getFragmentManager(), fragment.getClass().getName());
//    }
//    public void openColorPickDialog(){
//        DialogFragment fragment = new DialogBackgroundColor();
//        fragment.setTargetFragment(this, REQUEST_CODE_COLOR);
//        fragment.show(getFragmentManager(), fragment.getClass().getName());
//    }
//    public void openFormDialog(){
//        DialogFragment fragment = new DialogForm();
//        fragment.setTargetFragment(this, REQUEST_CODE_FORM);
//        fragment.show(getFragmentManager(), fragment.getClass().getName());
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case REQUEST_CODE_UPDATE_TEXT:
//                    String newNodeTextFromDialog = data.getStringExtra(DialogText.TAG_NEW_TEXT);
//                    updateTextNode(allNodeForMindmap, selected_item.getId(), newNodeTextFromDialog);
//                    /*updateViewNode(selected_item.getId());*/
//                    myCanvas.removeAllViews();
//                    paintAllNode(allNodeForMindmap);
//                    break;
//                case REQUEST_CODE_NEW_NODE:
//                    String newNodeNameFromDialog = data.getStringExtra(DialogText.TAG_NEW_TEXT);
//                    Node newNode = new Node(newNodeNameFromDialog, allNodeForMindmap.size(), selected_item.getId());
//                    helper.addNode(idMindmap, newNode);
//                    addNewNodeInList(newNode);
//                    paintNodeWithLine(newNode);
//                    break;
//                case REQUEST_CODE_COLOR:
//                    int newBackgroundColorFromPicker = data.getIntExtra(DialogBackgroundColor.TAG_COLOR, -1);
//                    updateNodeColor(allNodeForMindmap, selected_item.getId(), newBackgroundColorFromPicker);
//                    updateViewNode(selected_item.getId());
//                    break;
//                case REQUEST_CODE_FORM:
//                    String newNodeFormFromDialog = data.getStringExtra(DialogForm.TAG_NEW_FORM);
//                    updateNodeForm(allNodeForMindmap, selected_item.getId(), newNodeFormFromDialog);
//                    updateViewNode(selected_item.getId());
//                    break;
//                //обработка других requestCode
//            }
//        }
//    }
}
