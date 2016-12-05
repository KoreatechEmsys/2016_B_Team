package com.pelkan.tab;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SectionsFragment7 extends Fragment {
    Switch screenBtn;
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    public SectionsFragment7() {

    }
    // PlaceholderFragment.newInstance() 와 똑같이 추가
    static SectionsFragment7 newInstance(int SectionNumber){
        SectionsFragment7 fragment = new SectionsFragment7();
        Bundle args = new Bundle();
        args.putInt("section_number", SectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page7,
                container, false);
        screenBtn = (Switch) rootView.findViewById(R.id.screen_switch);
        setting = getActivity().getSharedPreferences("setting", 0);
        editor= setting.edit();

        if(setting.getBoolean("Screen_Lock", false)) {                                          //환경 설정 저장된 값으로 ui로 표현
            screenBtn.setChecked(true);
        }
        screenBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {       //화면 설정 on off
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on) {                                       //화면 꺼짐 on off
                if(on) {
                    //getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);               //tab 최초 생서일성
                    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    editor.putBoolean("Screen_Lock", true);
                    editor.commit();
                }
                else {
                    editor.putBoolean("Screen_Lock", false);
                    editor.commit();
                }
            }
        });

        return rootView;
    }
}