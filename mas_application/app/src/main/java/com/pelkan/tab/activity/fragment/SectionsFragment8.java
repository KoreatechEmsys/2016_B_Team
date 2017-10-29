package com.pelkan.tab;

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
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SectionsFragment8 extends Fragment {
    private ListView my_question_list;
    private TextView question_count;
    ArrayList<QuestionList> question_array = new ArrayList<QuestionList>();
    ArrayList<Integer> pidArr = new ArrayList<>();                  //문제의 리스트의 id
    String jsonResult;
    String pid;
    JSONObject product;
    private String url_qustion = "http://218.150.182.58:2041/mas/my_question.php";

    public SectionsFragment8() {

    }
    // PlaceholderFragment.newInstance() 와 똑같이 추가
    static SectionsFragment8 newInstance(int SectionNumber){
        SectionsFragment8 fragment = new SectionsFragment8();
        Bundle args = new Bundle();
        args.putInt("section_number", SectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page8,
                container, false);
        my_question_list = (ListView) rootView.findViewById(R.id.my_question_list);
        question_count = (TextView) rootView.findViewById(R.id.question_count);
        MyQuestionTask mytask = new MyQuestionTask();
        mytask.execute(new String[]{url_qustion});

        my_question_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {          //문제 리스트 클릭 이벤트
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                SectionsFragment4 temp = new SectionsFragment4();

                int touchIndex = (int) arg3;
                // getting values from selected ListItem

                pid = question_array.get(touchIndex).getQid().toString();
                Bundle args = new Bundle();
                args.putString("pid", pid);
                temp.setArguments(args);
                System.out.println("arg3는 " + arg3);
                System.out.println("pid는 " + pid);

                trans.replace(R.id.root_frame, temp);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
                Tab.mViewPager.setCurrentItem(1);
                System.out.println("리스트 전환 완료 및 fragment 시작");
            }
        });

        return rootView;
    }

    private class MyQuestionTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                HttpParams para = new BasicHttpParams();
                para.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                HttpClient httpclient = new DefaultHttpClient(para);
                HttpPost httppost = new HttpPost(url_qustion);
                List<NameValuePair> param = new ArrayList<NameValuePair>();
                param.add(new BasicNameValuePair("m_id", Login.my_id));
                httppost.setEntity(new UrlEncodedFormEntity(param, "utf-8"));
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            }

            catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));

            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            }

            catch (IOException e) {
                System.out.println("에러 싴발 1");
            }
            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd", Locale.KOREA );
            Date currentTime = new Date( );
            Date compareDate = null;
            String mTime = mSimpleDateFormat.format ( currentTime );
            question_array.clear();
            try {
                JSONObject jsonResponse = new JSONObject(jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("question_count");
                product = jsonMainNode.getJSONObject(0);
                currentTime = mSimpleDateFormat.parse(mTime);
                question_count.setText("나의문제 수 : " + product.optString("count"));

                jsonMainNode = jsonResponse.optJSONArray("question_list");

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
                }
            } catch (JSONException e) {
            } catch (ParseException e ) {
            }

            MainQuestionAdapter adapter = new MainQuestionAdapter(getActivity(), R.layout.main_question, question_array);
            my_question_list.setAdapter(adapter);
//            MyQuestionAdapter adapter = new MyQuestionAdapter(getActivity(), R.layout.my_question, question_array);
//            my_question_list.setAdapter(adapter);
        }
    }
}