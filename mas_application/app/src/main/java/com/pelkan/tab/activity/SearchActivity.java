package com.pelkan.tab;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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

import mehdi.sakout.fancybuttons.FancyButton;

public class SearchActivity extends ActionBarActivity {
    private LinearLayout open_config;
    private TextView open_config2;
    private LinearLayout config1;
    private LinearLayout config2;
    private FancyButton closeBtn;
    private FancyButton searchBtn;
    private ListView searchedQuestion;
    private CheckBox check_id;
    private CheckBox check_keyword;
    private CheckBox check_title;
    private CheckBox check_content;
    private ProgressDialog pDialog;
    private EditText search_keyword;
    private Spinner spinner;
    private String url = "http://218.150.182.58:2041/mas/search_details.php";    //검색조건에 따른 결과 url
    private String jsonResult;
    private String sorting_option;
    boolean is_open = false;
    Tab aActivity = (Tab)Tab.startActivisy;

    SharedPreferences setting;
    SharedPreferences.Editor editor;
    ArrayList<Integer> pidArr = new ArrayList<>();                             //문제의 리스트의 id
    ArrayList<QuestionList> question_array = new ArrayList<QuestionList>();    //문제 리스트
    ArrayList<QuestionList> question_array_date = new ArrayList<QuestionList>();          //날짜를 위한 리스트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        open_config = (LinearLayout)findViewById(R.id.open_config);
        open_config2 = (TextView)findViewById(R.id.open_config2);
        config1 = (LinearLayout) findViewById(R.id.search_config_layout);
        config2 = (LinearLayout) findViewById(R.id.search_config2_layout);
        searchedQuestion = (ListView) findViewById(R.id.list);
        closeBtn = (FancyButton) findViewById(R.id.closeBtn);
        searchBtn = (FancyButton) findViewById(R.id.searchBtn);
        check_content = (CheckBox) findViewById(R.id.checkContent);
        check_id = (CheckBox) findViewById(R.id.checkID);
        check_keyword = (CheckBox) findViewById(R.id.checkKeyword);
        check_title = (CheckBox) findViewById(R.id.checkTitle);
        search_keyword = (EditText) findViewById(R.id.search_keyword);
        spinner = (Spinner) findViewById(R.id.sorting_option);
        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();

        searchBtn.setOnClickListener(new View.OnClickListener() {           //검색 버튼 이벤트

            @Override
            public void onClick(View view) {                                //상세 검색 펼친거 끄거
                config1.setVisibility(View.GONE);
                config2.setVisibility(View.GONE);
                is_open = false;                                             //상세 검색 아래부터 리스트 뿌려주기
                searchedQuestion.setVisibility(View.VISIBLE);
                sorting_option = spinner.getSelectedItem().toString();
                new SearchOperation().execute(url);
            }
        });

        searchedQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {          //문제 리스트 클릭 이벤트
                int touchIndex = (int) arg3;
                // getting values from selected ListItem
                String pid = question_array.get(touchIndex).getQid().toString();
                editor.putBoolean("is_searched", true);
                editor.putString("search_pid", pid);
                editor.commit();
                Intent intent1 = new Intent(SearchActivity.this, Tab.class);
                startActivity(intent1);
                System.out.println("arg3는 " + arg3);
                System.out.println("pid는 " + pid);
                aActivity.finish();
                finish();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                finish();
            }
        });

        open_config.setOnClickListener(new View.OnClickListener() {         //설정 펼치기

            @Override
            public void onClick(View view) {
                if(is_open == false) {
                    config1.setVisibility(View.VISIBLE);
                    config2.setVisibility(View.VISIBLE);
                    is_open = true;
                }
                else{
                    config1.setVisibility(View.GONE);
                    config2.setVisibility(View.GONE);
                    is_open = false;
                }
            }
        });

        open_config2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(is_open == false) {
                    config1.setVisibility(View.VISIBLE);
                    config2.setVisibility(View.VISIBLE);
                    is_open = true;
                }
                else{
                    config1.setVisibility(View.GONE);
                    config2.setVisibility(View.GONE);
                    is_open = false;
                }
            }
        });
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

    private class SearchOperation  extends AsyncTask<String, Void, Void> {
        private String id_check;
        private String title_check;
        private String keyword_check;
        private String content_check;
        String temp_search = "";


        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("로딩중...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            if (check_id.isChecked() == true)
                id_check = "true";
            else
                id_check = "false";

            if (check_keyword.isChecked() == true)
                keyword_check = "true";
            else
                keyword_check = "false";

            if (check_title.isChecked() == true)
                title_check = "true";
            else
                title_check = "false";

            if (check_content.isChecked() == true)
                content_check = "true";
            else
                content_check = "false";
            System.out.print("체크 설정");
            System.out.println(id_check);
            System.out.println(keyword_check);
            System.out.println(title_check);
            System.out.println(content_check);
            temp_search = search_keyword.getText().toString();

        }

        // onPreExecute 메소드 이후 호출
        protected Void doInBackground(String... urls) {//클릭이벤트 실행시 파라미터값 urls= LongOperation().execute(serverURL);

            System.out.println("연결 준비");
            BufferedReader reader = null;

            // 데이터 전송
            try {
                // Lab code here....
                //Data를 보낼 URL =
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
                param.add(new BasicNameValuePair("is_id", id_check));
                param.add(new BasicNameValuePair("is_title", title_check));
                param.add(new BasicNameValuePair("is_keyword", keyword_check));
                param.add(new BasicNameValuePair("is_content", content_check));
                param.add(new BasicNameValuePair("sorting", sorting_option));
                param.add(new BasicNameValuePair("keyword", temp_search));
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
                //위 StringBuilder값을 : sb를 Content 변수에 저장함
                jsonResult = sb.toString();
                System.out.println("황커 온다1");
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
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd", Locale.KOREA );
            Date currentTime = new Date( );
            Date compareDate = null;
            String mTime = mSimpleDateFormat.format ( currentTime );
            question_array.clear();
            question_array_date.clear();

            try {
                JSONObject jsonResponse = new JSONObject(jsonResult);
                System.out.println("나머지 결과는 " + jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("question_list");
                currentTime = mSimpleDateFormat.parse(mTime);                               //현재 시간은 Date 타입으로 구함

//            for (int i = 0; i < jsonMainNode.length(); i++) {
//                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
//                String q_id = jsonChildNode.optString("q_id");
//                String m_id = jsonChildNode.optString("m_id");
//                String title = jsonChildNode.optString("title");
//                String keyword = jsonChildNode.optString("keyword");
//                String viewCount = jsonChildNode.optString("view_count");
//                String responseCount = jsonChildNode.optString("response_count");
//                String successCount = jsonChildNode.optString("success_count");
//                String addDate = jsonChildNode.optString("reg_date");
//
//                String[] keywordspit = keyword.split(",");
//
//                keyword = keywordspit[0];
//                compareDate = mSimpleDateFormat.parse(addDate.substring(0, 10));        //년 월 일 추출
//                if(currentTime.compareTo(compareDate) == 0) {                           //날짜가 같으면 시, 분 추출
//                    addDate = addDate.substring(11, 16);                                //00:00 형식
//                } else {                                                                //다른 날이면 년 월 일 추출
//                    addDate = addDate.substring(5, 10);
//                    addDate = addDate.replaceAll("-", ".");
//                }
//
//                int charIndex = m_id.indexOf("@");                                      //아이디 이메일 앞부분 추출
//                m_id = m_id.substring(0, charIndex);
//
//                pidArr.add(i, Integer.parseInt(q_id));
//                QuestionList temp = new QuestionList();
//                temp.setQid(q_id);
//                temp.setKeywords(keyword);
//                temp.setViewCount(viewCount);
//                temp.setM_id(m_id);
//                temp.setTitle(title);
//                temp.setSuccess_count(successCount);
//                temp.setAddDate(addDate);
//                temp.setResponseCount(responseCount);
//                question_array.add(temp);
//                question_array_date.add(temp);
//            }

                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    String q_id = jsonChildNode.optString("q_id");
                    String title = jsonChildNode.optString("title");
                    String keyword = jsonChildNode.optString("keyword");
                    String viewCount = jsonChildNode.optString("view_count");
                    String responseCount = jsonChildNode.optString("response_count");
                    String successCount = jsonChildNode.optString("success_count");
                    String addDate = jsonChildNode.optString("reg_date");

                    String[] keywordspit = keyword.split(",");
                    keyword = keywordspit[0];

                    compareDate = mSimpleDateFormat.parse(addDate.substring(0, 10));        //년 월 일 추출
                    if(currentTime.compareTo(compareDate) == 0) {                           //날짜가 같으면 시, 분 추출
                        addDate = addDate.substring(11, 16);                                //00:00 형식
                    } else {                                                                //다른 날이면 년 월 일 추출
                        addDate = addDate.substring(5, 10);
                        addDate = addDate.replaceAll("-", ".");
                    }
/*
이메일 제거
                int charIndex = m_id.indexOf("@");                                      //아이디 이메일 앞부분 추출
                m_id = m_id.substring(0, charIndex);
*/
                    pidArr.add(i, Integer.parseInt(q_id));
                    QuestionList temp = new QuestionList();
                    temp.setQid(q_id);
                    temp.setKeywords(keyword);
                    temp.setViewCount(viewCount);
                    temp.setTitle(title);
                    temp.setSuccess_count(successCount);
                    temp.setAddDate(addDate);
                    temp.setResponseCount(responseCount);
                    question_array.add(temp);
                    question_array_date.add(temp);
                }



                for (int i = 0; i < question_array.size(); i++) {
                    System.out.println("내부의 데이터는 " + question_array.get(i).getQid());
                }
            } catch (JSONException e) {
                System.out.println("에러 싴발 2");
                question_array.clear();
                question_array_date.clear();
            } catch (ParseException e ) {
            }
//        System.out.println("배열의 사이즈는 " + question_array.size());
        SearchedQuestionAdapter adapter = new SearchedQuestionAdapter(getApplicationContext(), R.layout.main_question, question_array);
        searchedQuestion.setAdapter(adapter);
            pDialog.cancel();
//            MainQuestionAdapter adapter = new MainQuestionAdapter(getApplicationContext(), R.layout.main_question, question_array);
//            searchedQuestion.setAdapter(adapter);
        }
    }

//    private class JsonReadTask extends AsyncTask<String, Void, String> {
//        private String id_check;
//        private String title_check;
//        private String keyword_check;
//        private String content_check;
//        String temp_search = "";
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = new ProgressDialog(SearchActivity.this);
//            pDialog.setMessage("로딩중...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
//            if (check_id.isChecked() == true)
//                id_check = "true";
//            else
//                id_check = "false";
//
//            if (check_keyword.isChecked() == true)
//                keyword_check = "true";
//            else
//                keyword_check = "false";
//
//            if (check_title.isChecked() == true)
//                title_check = "true";
//            else
//                title_check = "false";
//
//            if (check_content.isChecked() == true)
//                content_check = "true";
//            else
//                content_check = "false";
//            System.out.print("체크 설정");
//            System.out.println(id_check);
//            System.out.println(keyword_check);
//            System.out.println(title_check);
//            System.out.println(content_check);
//            temp_search = search_keyword.getText().toString();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                HttpParams para = new BasicHttpParams();
//                para.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
//                DefaultHttpClient httpClient = new DefaultHttpClient(para);
//                HttpPost httppost = new HttpPost(url);
//                List<NameValuePair> param = new ArrayList<NameValuePair>();
//                param.add(new BasicNameValuePair("is_id", id_check));
//                param.add(new BasicNameValuePair("is_title", title_check));
//                param.add(new BasicNameValuePair("is_keyword", keyword_check));
//                param.add(new BasicNameValuePair("is_content", content_check));
//                param.add(new BasicNameValuePair("sorting", sorting_option));
//                param.add(new BasicNameValuePair("keyword", temp_search));
//                httppost.setEntity(new UrlEncodedFormEntity(param, "utf-8"));
//                HttpResponse httpResponse = httpClient.execute(httppost);
//                jsonResult = inputStreamToString(httpResponse.getEntity().getContent()).toString();
//
//
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        private StringBuilder inputStreamToString(InputStream is) {
//            String rLine = "";
//            StringBuilder answer = new StringBuilder();
//            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//
//            try {
//                while ((rLine = rd.readLine()) != null) {
//                    answer.append(rLine);
//                }
//            }
//
//            catch (IOException e) {
//                System.out.println("에러 싴발 1");
//            }
//            return answer;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            ListDrwaer();
//            pDialog.dismiss();
//        }
//    }// end async task
//
//    public void accessWebService() {
//        JsonReadTask task = new JsonReadTask();
//        // passes values for the urls string array
//        task.execute(new String[]{url});
//    }
//
//    // build hash set for list view
//    public void ListDrwaer() {
//        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd", Locale.KOREA );
//        Date currentTime = new Date( );
//        Date compareDate = null;
//        String mTime = mSimpleDateFormat.format ( currentTime );
//        question_array.clear();
//        question_array_date.clear();
//
//        try {
//            JSONObject jsonResponse = new JSONObject(jsonResult);
//            System.out.println("나머지 결과는 " + jsonResult);
//            JSONArray jsonMainNode = jsonResponse.optJSONArray("question_list");
//            currentTime = mSimpleDateFormat.parse(mTime);                               //현재 시간은 Date 타입으로 구함
//
////            for (int i = 0; i < jsonMainNode.length(); i++) {
////                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
////                String q_id = jsonChildNode.optString("q_id");
////                String m_id = jsonChildNode.optString("m_id");
////                String title = jsonChildNode.optString("title");
////                String keyword = jsonChildNode.optString("keyword");
////                String viewCount = jsonChildNode.optString("view_count");
////                String responseCount = jsonChildNode.optString("response_count");
////                String successCount = jsonChildNode.optString("success_count");
////                String addDate = jsonChildNode.optString("reg_date");
////
////                String[] keywordspit = keyword.split(",");
////
////                keyword = keywordspit[0];
////                compareDate = mSimpleDateFormat.parse(addDate.substring(0, 10));        //년 월 일 추출
////                if(currentTime.compareTo(compareDate) == 0) {                           //날짜가 같으면 시, 분 추출
////                    addDate = addDate.substring(11, 16);                                //00:00 형식
////                } else {                                                                //다른 날이면 년 월 일 추출
////                    addDate = addDate.substring(5, 10);
////                    addDate = addDate.replaceAll("-", ".");
////                }
////
////                int charIndex = m_id.indexOf("@");                                      //아이디 이메일 앞부분 추출
////                m_id = m_id.substring(0, charIndex);
////
////                pidArr.add(i, Integer.parseInt(q_id));
////                QuestionList temp = new QuestionList();
////                temp.setQid(q_id);
////                temp.setKeywords(keyword);
////                temp.setViewCount(viewCount);
////                temp.setM_id(m_id);
////                temp.setTitle(title);
////                temp.setSuccess_count(successCount);
////                temp.setAddDate(addDate);
////                temp.setResponseCount(responseCount);
////                question_array.add(temp);
////                question_array_date.add(temp);
////            }
//
//            for (int i = 0; i < jsonMainNode.length(); i++) {
//                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
//                String q_id = jsonChildNode.optString("q_id");
//                String title = jsonChildNode.optString("title");
//                String keyword = jsonChildNode.optString("keyword");
//                String viewCount = jsonChildNode.optString("view_count");
//                String responseCount = jsonChildNode.optString("response_count");
//                String successCount = jsonChildNode.optString("success_count");
//                String addDate = jsonChildNode.optString("reg_date");
//
//                String[] keywordspit = keyword.split(",");
//                keyword = keywordspit[0];
//
//                compareDate = mSimpleDateFormat.parse(addDate.substring(0, 10));        //년 월 일 추출
//                if(currentTime.compareTo(compareDate) == 0) {                           //날짜가 같으면 시, 분 추출
//                    addDate = addDate.substring(11, 16);                                //00:00 형식
//                } else {                                                                //다른 날이면 년 월 일 추출
//                    addDate = addDate.substring(5, 10);
//                    addDate = addDate.replaceAll("-", ".");
//                }
///*
//이메일 제거
//                int charIndex = m_id.indexOf("@");                                      //아이디 이메일 앞부분 추출
//                m_id = m_id.substring(0, charIndex);
//*/
//                pidArr.add(i, Integer.parseInt(q_id));
//                QuestionList temp = new QuestionList();
//                temp.setQid(q_id);
//                temp.setKeywords(keyword);
//                temp.setViewCount(viewCount);
//                temp.setTitle(title);
//                temp.setSuccess_count(successCount);
//                temp.setAddDate(addDate);
//                temp.setResponseCount(responseCount);
//                question_array.add(temp);
//                question_array_date.add(temp);
//            }
//
//
//
//            for (int i = 0; i < question_array.size(); i++) {
//                System.out.println("내부의 데이터는 " + question_array.get(i).getQid());
//            }
//        } catch (JSONException e) {
//            System.out.println("에러 싴발 2");
//            question_array.clear();
//            question_array_date.clear();
//        } catch (ParseException e ) {
//        }
////        System.out.println("배열의 사이즈는 " + question_array.size());
////        SearchedQuestionAdapter adapter = new SearchedQuestionAdapter(getApplicationContext(), R.layout.main_question, question_array);
////        searchedQuestion.setAdapter(adapter);
//
//
//        MainQuestionAdapter adapter = new MainQuestionAdapter(getApplicationContext(), R.layout.main_question, question_array);
//        searchedQuestion.setAdapter(adapter);
//    }
}
