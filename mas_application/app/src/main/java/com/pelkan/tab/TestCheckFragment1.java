package com.pelkan.tab;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JangLab on 2016-09-17.
 */
public class TestCheckFragment1 extends Fragment {
    private static ListView my_question_list;
    private String json;
    private String target;
    private String e_id;
    private String m_id;
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
    private String url = "http://218.150.182.58:2041/mas/target_img.php";

    public TestCheckFragment1() {
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
    public static CheckExamActivity.PlaceholderFragment newInstance(int sectionNumber) {
        CheckExamActivity.PlaceholderFragment fragment = new CheckExamActivity.PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_check_exam2, container, false);
        my_question_list = (ListView) rootView.findViewById(R.id.my_question_list);
        if (getArguments() != null) {                       //유저 리스트 얻기
            target = getArguments().getString("target");
            e_id = getArguments().getString("e_id");
        }
        trans = getActivity().getSupportFragmentManager().beginTransaction();
        new MyFriendsListTask().execute(url);

        my_question_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {          //문제 리스트 클릭 이벤트
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                TestCheckFragment2 temp = new TestCheckFragment2();

                int touchIndex = (int) arg3;
                // getting values from selected ListItem

                m_id = friends.get(touchIndex).getId().toString();
                Bundle args = new Bundle();
                args.putString("m_id", m_id);
                args.putString("e_id", e_id);
                temp.setArguments(args);
                System.out.println("arg3는 " + arg3);
                System.out.println("pid는 " + m_id);
                System.out.println("eid는 " + e_id);

                trans.replace(R.id.root_frame, temp);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
                System.out.println("리스트 전환 완료 및 fragment 시작");
            }
        });

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
                param.add(new BasicNameValuePair("target", target));
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
                JSONArray jsonMainNode = jsonResponse.optJSONArray("member_list");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String friend_img_url = jsonChildNode.optString("profile_img_url");
                    String m_id = jsonChildNode.optString("m_id");

                    Friend temp = new Friend();
                    temp.setId(m_id);
                    temp.setImg_url(friend_img_url);
                    friends.add(temp);
                }
                SelectMyFriendsCheckAdapter adapter = new SelectMyFriendsCheckAdapter(getActivity(), R.layout.select_friends_check, friends);
                my_question_list.setAdapter(adapter);
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
