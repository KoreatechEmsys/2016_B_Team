package com.pelkan.tab;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by admin on 2016-09-09.
 */
class NodeView extends View {
    private final String lineBreak = "\n";
    private static final int TEXT_SIZE = 30;            //노드 글자 크기

    public int lineHight, viewHight;                //lineHight 세로길이
    private Paint p;
    private Node node;
    private float x;
    private float y;
    private int color;
    public int side;                                //가로길이
    private ArrayList<String> nodeTextArray;
    private String maxLengthWord;
    private Bitmap image;



    public NodeView(Context context, Node node) {               //노드의 가로 세로 길이는?
        super(context);
        p = new Paint();
        p.setTextSize(TEXT_SIZE);
        this.node = node;
        x = node.getCenterX();
        y = node.getCenterY();
        nodeTextArray = findLineBreak(node.getText());
        this.color = node.getColor();
                                        //노드의 세로 길이, 아래로 길어짐
        lineHight = 10 + TEXT_SIZE;// необходимо предусмотреть условие вхождения в строку символа переноса каретки
        ArrayList<String> wordsInNodesText = findLineBreak(node.getText());
        Resources r = context.getResources();
        image = BitmapFactory.decodeResource(r, R.drawable.ic_error_black_48dp);

        if(!wordsInNodesText.isEmpty()){
            maxLengthWord = findMaxLengthString(p, wordsInNodesText);
            viewHight = lineHight*wordsInNodesText.size();
        } else {
            maxLengthWord = node.getText();
            viewHight = lineHight;
        }
//        float width = 200;
        float width = p.measureText(maxLengthWord);// get text width to draw rectangle

        side = (int)width + 20;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredHeight = measureHeight(heightMeasureSpec);
        int measuredWidth = measureWidth(widthMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    private int measureHeight(int measureSpec) {
        // Height
        return (int) (viewHight);
    }

    private int measureWidth(int measureSpec) {
        // Width
        return (int) (side);
    }
    protected void onDraw(Canvas canvas) {
        p.setStyle(Paint.Style.FILL);
        //canvas.drawARGB(80, 102, 204, 255);
        p.setColor(color);
        // draw square
        int y_newLine = 0;
        p.setTypeface(Typeface.create((String)null, Typeface.BOLD));

        if(nodeTextArray.isEmpty()) {
//            drawShape(canvas, node, -side/2, -lineHight/2, side/2, lineHight/2);
            drawShape(canvas, node, 0, 0, side, lineHight);
            p.setColor(Color.WHITE);
            // draw text relative to the center of square
//            float z = 10; float k = 30;
//            TextPaint mTextPaint=new TextPaint();
//            for (String line: node.getText().split("\n")) {
//                canvas.drawText(line, z, k, mTextPaint);
//                k += mTextPaint.descent() - mTextPaint.ascent();
//            }
            canvas.drawText(node.getText(), 10, 30, p);                 //텍스트 쓰기
//            canvas.drawText(node.getText(), 10, 20, p);                 //텍스트 쓰기
        } else {
            p.setColor(color);
            drawShape(canvas, node, 0, y_newLine, side, viewHight);
            for(String lineText : nodeTextArray){
                p.setColor(Color.WHITE);
                // draw text relative to the center of square
                canvas.drawText(lineText, 10, lineHight + y_newLine, p);
//                canvas.drawText(lineText, 10, lineHight - 10 + y_newLine, p);         이거 낫배드
//                canvas.drawText(lineText, 20, lineHight - 10 + y_newLine, p);
                y_newLine += 30;
            }
        }

    }
    private static ArrayList<String> findLineBreak(String nodeText){
        ArrayList<String> nodeTextList = new ArrayList<>();
        int beginIndex = 0;
        int endIndex;
        int index = 0;
        index = nodeText.indexOf("\n");
        while (index != -1) {
            if(index != -1) {
                endIndex = index;
                nodeTextList.add(nodeText.substring(beginIndex, endIndex));
                index += 1;
                beginIndex = index;
            }
            index = nodeText.indexOf("\n", index);
        }
        if(!nodeTextList.isEmpty()){
            nodeTextList.add(nodeText.substring(beginIndex, nodeText.length()));
        }
        return nodeTextList;
    }
    private String findMaxLengthString(Paint p, ArrayList<String> nodeTextList){
        String maxLengthString = "";
        float maxWidth = p.measureText(maxLengthString);
        for(String tempString : nodeTextList){
            if(maxWidth < p.measureText(tempString)){
                maxLengthString = tempString;
                maxWidth = p.measureText(tempString);
            }
        }
        return maxLengthString;
    }
    //form에 따른 노드 그리기
    private void drawShape(Canvas canvas, Node node, int left, int top, int right, int bottom){
        RectF rectf;
        switch (node.getForm()){
            case "Rectangle":
//                Resources res = getResources();
//                BitmapDrawable bd = (BitmapDrawable)res.getDrawable(R.drawable.ic_error_black_48dp);
//                Bitmap bit = bd.getBitmap();
//                Rect source = new Rect(0, 0, bit.getWidth(), bit.getHeight());
//
//                canvas.drawBitmap(bit, source, source, p);
                System.out.println("값은 왼" + left + "위" + top + "오른" + right + "아래" + bottom);
                canvas.drawRect(left, top, right, bottom, p);
//                canvas.drawRect(-100, -100, 100, 100, p);
//                canvas.drawBitmap(image, 100, 100, p);
                break;
            case "Ellipse":
                rectf = new RectF(left, top, right, bottom);
                canvas.drawOval(rectf, p);
                break;
            case "RoundRect":
                rectf = new RectF(left, top, right, bottom);
                canvas.drawRoundRect(rectf, 20, 20, p);
                break;
        }

    }
}
