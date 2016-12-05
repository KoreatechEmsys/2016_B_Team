package com.pelkan.tab;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;

/**
 * Created by JangLab on 2016-09-18.
 */
public class SelectMyFriendsCheckAdapter extends ArrayAdapter<Friend> {       //메인화면에서 문제리스트 어댑터
    int groupid;
    ArrayList<Friend> records;
    Context context;
    private AQuery aq;

    public SelectMyFriendsCheckAdapter(Context context, int vg, ArrayList<Friend> records) {
        super(context, vg,  records);
        this.context = context;
        this.groupid = vg;
        this.records = records;
    }

    @Override
    public int getCount() {
        return records.size() ;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = null;
        FriendsHolder holder = null;

        if(convertView==null){
            holder = new FriendsHolder();
            LayoutInflater inflater =( (Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.select_friends_check, null);
            holder.friend_id = (TextView) convertView.findViewById(R.id.friend_id);
            holder.friend_img = (ImageView) convertView.findViewById(R.id.friend_img);
            aq = new AQuery(context);
            convertView.setTag(holder);

        }else{
            holder = (FriendsHolder) convertView.getTag();
        }


        Friend food = records.get(position);
        holder.friend_id.setText(food.getId());
        aq.id(holder.friend_img).image(food.getImg_url());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Friend getItem(int position) {
        return records.get(position);
    }
}
