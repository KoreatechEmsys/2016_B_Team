package com.pelkan.tab;

/**
 * Created by admin on 2016-02-04.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CuAdapter extends ArrayAdapter<Product> {

    int groupid;

    ArrayList<Product> records;

    Context context;
    int loader = R.drawable.ic_la;


    public CuAdapter(Context context, int vg, ArrayList<Product> records) {

        super(context, vg,  records);

        this.context = context;

        this.groupid = vg;

        this.records = records;



    }



    public View getView(int position, View convertView, ViewGroup parent) {

        View v = null;
        ListHolder holder = null;
        if(convertView==null){
            holder = new ListHolder();
            LayoutInflater inflater =( (Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.all_list, null);

            holder.q_id = (TextView) convertView.findViewById(R.id.q_id);
            //holder.img = (ImageView) convertView.findViewById(R.id.list_image);
            holder.title = (TextView) convertView.findViewById(R.id.title);        //홀더 설정
          //  holder.content = (TextView) convertView.findViewById(R.id.content);

            convertView.setTag(holder);

        }else{
            holder = (ListHolder) convertView.getTag();
            //holder.img.setImageResource(R.drawable.def); 임시이미지 def라는 파일로 하기
        }

        holder.img.setImageResource(R.drawable.ic_launcher);
        ImageLoader imgLoader = new ImageLoader(context);
        Product food = records.get(position);
        imgLoader.DisplayImage(food.getURL(), holder.img);       //String 타입의 url 받아서 로더에 url 넘겨서 출력한당 url이랑 이미지 뷰
        //holder.name.setText(food.getpName());					     이건 스트링 처리
        holder.q_id.setText(food.getQ_id());
        holder.title.setText(food.getTitle());
        holder.content.setText(food.getContent());


        return convertView;

    }

}