package com.pelkan.tab;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
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
public class SelectMyFriendsAdapter  extends ArrayAdapter<Friend> {       //메인화면에서 문제리스트 어댑터
    int groupid;
    ArrayList<Friend> records;
    Context context;
    private AQuery aq;

    public SelectMyFriendsAdapter(Context context, int vg, ArrayList<Friend> records) {
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
        final int checkBoxPosition = position;
        View v = null;
        FriendsHolder holder = null;

        if(convertView==null){
            holder = new FriendsHolder();
            LayoutInflater inflater =( (Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.select_friends, null);
            holder.friend_id = (TextView) convertView.findViewById(R.id.friend_id);
            holder.friend_img = (ImageView) convertView.findViewById(R.id.friend_img);
            holder.ckbox = (CheckBox) convertView.findViewById(R.id.ckbox);
            aq = new AQuery(context);
            convertView.setTag(holder);

        }else{
            holder = (FriendsHolder) convertView.getTag();
        }

        holder.ckbox.setChecked(false);

        holder.ckbox.setChecked(((ListView)parent).isItemChecked(position));

        Friend food = records.get(position);
        holder.friend_id.setText(food.getId());
        aq.id(holder.friend_img).image(food.getImg_url());

        //체크박스 클릭 이벤트
        holder.ckbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Friend checkViewItem = (Friend)getItem(checkBoxPosition);

                //체크 안되어 있는 경우
                if(checkViewItem.isChecked()){
                    records.get(checkBoxPosition).setChecked(false);
                    //체크가 되어 있는 경우
                }else{
                    System.out.println("하이하이");
                    records.get(checkBoxPosition).setChecked(true);
                }
                //데이터 변경 알림
                notifyDataSetChanged();
            }


        });

        //로우별 isChecked 값에 따른 체크상태를 표시
        holder.ckbox.setChecked(records.get(position).isChecked());

        TestFragment1.is_checked_friendlist.clear();
        TestFragment1.idArr.clear();
        TestFragment1.imgArr.clear();
        for(int j = 0; j < records.size(); j++) {
            TestFragment1.is_checked_friendlist.add(j, records.get(j).isChecked());
            TestFragment1.idArr.add(j, records.get(j).getId());
            TestFragment1.imgArr.add(j, records.get(j).getImg_url());
        }
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
