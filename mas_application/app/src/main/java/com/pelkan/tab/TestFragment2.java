package com.pelkan.tab;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by JangLab on 2016-09-19.
 */
public class TestFragment2 extends Fragment {
    private String json;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    ArrayList<Friend> friends = new ArrayList<Friend>();
    public static ArrayList<Boolean> is_checked_friendlist = new ArrayList<Boolean>();
    public static ArrayList<String> idArr = new ArrayList<String>();
    public static ArrayList<String> imgArr = new ArrayList<String>();
    static String qidList="";
    static String targetUserList="";
    static String startTime;
    static String limitTime;
    static String endTime;
    static String e_title;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String url = "http://218.150.182.58:2041/mas/add_exam.php";
    private static EditText month;
    private static EditText day;
    private static EditText hour;
    private static EditText minitue;
    private static EditText limit_time;
    private static EditText title;

    public TestFragment2() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */

//    아래에 있는 리스트가 선택한 체크 리스트랑 qid목록임ㅇㅇ
//    for(int i = 0; i < TestFragment.is_checked_list.size(); i++) {
//        System.out.println("리스트는 " + TestFragment.is_checked_list.get(i));
//        System.out.println("리스트는 " + TestFragment.qidArr.get(i));
//    }
    public static AddExamActivity.PlaceholderFragment newInstance(int sectionNumber) {
        AddExamActivity.PlaceholderFragment fragment = new AddExamActivity.PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public static void nextStap2() {            //모든 정보를 통해 시험문제 등록
        qidList = "";
        targetUserList = "";
        System.out.println("시험에 필요한 모든 정보");
        System.out.println("문제 정보");
        for(int i = 0; i < TestFragment.is_checked_list.size(); i++) {
            System.out.println("리스트는 " + TestFragment.is_checked_list.get(i));
            System.out.println("리스트는 " + TestFragment.qidArr.get(i));
            if(TestFragment.is_checked_list.get(i)) {
                qidList += "," + TestFragment.qidArr.get(i);
            }
        }
        qidList = qidList.substring(1);
        System.out.println("유저정보");
        for(int i = 0; i < TestFragment1.is_checked_friendlist.size(); i++) {
            System.out.println("리스트는 " + TestFragment1.is_checked_friendlist.get(i));
            System.out.println("리스트는 " + TestFragment1.idArr.get(i));
            System.out.println("리스트는 " + TestFragment1.imgArr.get(i));
            if(TestFragment1.is_checked_friendlist.get(i)) {
                targetUserList += "," + TestFragment1.idArr.get(i);
            }
        }
        targetUserList = targetUserList.substring(1);
        new AddExam().execute("http://218.150.182.58:2041/mas/add_exam.php");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_exam3, container, false);

        month = (EditText) rootView.findViewById(R.id.month);
        day = (EditText) rootView.findViewById(R.id.day);
        hour = (EditText) rootView.findViewById(R.id.hour);
        minitue = (EditText) rootView.findViewById(R.id.minitue);
        limit_time = (EditText) rootView.findViewById(R.id.limit_time);
        title = (EditText) rootView.findViewById(R.id.title);

        return rootView;
    }

    private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public static class AddExam  extends AsyncTask<String, Void, Void> {


        protected void onPreExecute() {
            startTime="2016-";
            startTime += month.getText().toString() + "-" + day.getText().toString() + " " + hour.getText().toString() + ":" + minitue.getText().toString();
            limitTime = limit_time.getText().toString();
            e_title = title.getText().toString();
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date to = transFormat.parse(startTime);
                Calendar cal = Calendar.getInstance();
                cal.setTime(to);
                cal.add(Calendar.MINUTE, Integer.parseInt(limitTime));
                endTime = transFormat.format(cal.getTime());
            } catch (ParseException e){}

        }

        // onPreExecute 메소드 이후 호출
        protected Void doInBackground(String... urls) {//클릭이벤트 실행시 파라미터값 urls= LongOperation().execute(serverURL);

            System.out.println("연결 준비");
            BufferedReader reader = null;

            // 데이터 전송
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setChunkedStreamingMode(0);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setConnectTimeout(6000);
                conn.setReadTimeout(6000);
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("m_id", Login.my_id));         //출제자
                param.add(new BasicNameValuePair("target", targetUserList));         //대상자
                param.add(new BasicNameValuePair("start_time", startTime));         //시작시간
                param.add(new BasicNameValuePair("end_time", endTime));         //종료시간
                param.add(new BasicNameValuePair("question_list", qidList));         //시험문제
                param.add(new BasicNameValuePair("e_title", e_title));         //종료시간
                System.out.println(Login.my_id + "" + targetUserList+ "" +startTime+ "" +endTime+ "" +qidList);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                System.out.println("연결 준비끝");
                wr.write(getQuery(param));//onPreExecute 메소드의 data 변수의 파라미터 내용을 POST 전송명령
                wr.flush();//OutputStreamWriter 버퍼 메모리 비우기

                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                System.out.println("욕할 준비 끝");
                StringBuilder sb = new StringBuilder();
                String line = null;

                //서버 응답변수 reader 에서 내용을 라인단위 문자열로 만듬
                while ((line = reader.readLine()) != null) {
                    //서버응답값을 String 형태로 추가함
                    sb.append(line + "\n");
                }
                String json = sb.toString();

                System.out.println(json);

            } catch (Exception ex) {
            } finally {
                try {

                    reader.close();
                } catch (Exception ex) {
                }
            }

            /*****************************************************/
            return null;
        }

        //doInBackground POST전송 후 자동실행 코드
        protected void onPostExecute(Void unused) {
        }
    }
}