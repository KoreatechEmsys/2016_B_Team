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
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by JangLab on 2016-09-17.
 */
public class TestFragment1 extends Fragment {
    private static ListView my_question_list;
    private String json;
    static FragmentTransaction trans;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    ArrayList<Friend> friends = new ArrayList<Friend>();
    public static ArrayList<Boolean> is_checked_friendlist = new ArrayList<Boolean>();
    public static ArrayList<String> idArr = new ArrayList<String>();
    public static ArrayList<String> imgArr = new ArrayList<String>();
    private static final String ARG_SECTION_NUMBER = "section_number";
    private String url = "http://218.150.182.58:2041/mas/my_friends.php";

    public TestFragment1() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_exam2, container, false);
        my_question_list = (ListView) rootView.findViewById(R.id.my_question_list);
        trans = getActivity().getSupportFragmentManager().beginTransaction();
        new MyFriendsListTask().execute(url);

        return rootView;
    }

    public static void nextStap1() {
        TestFragment2 temp = new TestFragment2();
        trans.replace(R.id.root_frame, temp);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(null);

        trans.commit();
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
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
    private class MyFriendsListTask  extends AsyncTask<String, Void, Void> {


        protected void onPreExecute() {

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
                param.add(new BasicNameValuePair("m_id", Login.my_id));
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                System.out.println("연결 준비끝");
                wr.write(getQuery(param));//onPreExecute 메소드의 data 변수의 파라미터 내용을 POST 전송명령
                wr.flush();//OutputStreamWriter 버퍼 메모리 비우기

                //PHP 서버 응답값을 변수에 저장
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                //서버 응답변수 reader 에서 내용을 라인단위 문자열로 만듬
                while ((line = reader.readLine()) != null) {
                    //서버응답값을 String 형태로 추가함
                    sb.append(line + "\n");
                }
                json = sb.toString();
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
            ArrayList<Responser> array = new ArrayList<Responser>();
            System.out.println("튜플 받아옴");
            System.out.println("데이터는 " + json);
            friends.clear();

            try{
                JSONObject jsonResponse = new JSONObject(json);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("friend_list");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String friend_id = jsonChildNode.optString("friend_id");
                    String img_url = jsonChildNode.optString("img_url");

                    Friend temp = new Friend();
                    temp.setId(friend_id);
                    temp.setImg_url(img_url);
                    friends.add(temp);
                }
                SelectMyFriendsAdapter adapter = new SelectMyFriendsAdapter(getActivity(), R.layout.select_friends, friends);
                my_question_list.setAdapter(adapter);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
