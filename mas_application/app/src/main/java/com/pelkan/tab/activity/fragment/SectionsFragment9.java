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

public class SectionsFragment9 extends Fragment {
    private ListView my_question_list;
    private TextView question_count;
    ArrayList<QuestionList> question_array = new ArrayList<QuestionList>();
    ArrayList<String> ridArr = new ArrayList<>();
    String jsonResult;
    String rid;
    JSONObject product;
    private String url_response = "http://218.150.182.58:2041/mas/my_response.php";

    public SectionsFragment9() {

    }
    // PlaceholderFragment.newInstance() 와 똑같이 추가
    static SectionsFragment9 newInstance(int SectionNumber){
        SectionsFragment9 fragment = new SectionsFragment9();
        Bundle args = new Bundle();
        args.putInt("section_number", SectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page9,
                container, false);
        my_question_list = (ListView) rootView.findViewById(R.id.my_question_list);
        question_count = (TextView) rootView.findViewById(R.id.question_count);
        MyResponseTask mytask = new MyResponseTask();
        mytask.execute(new String[]{url_response});

        my_question_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {          //문제 리스트 클릭 이벤트
                FragmentTransaction trans = getFragmentManager()
                        .beginTransaction();
                SectionsFragment5 temp = new SectionsFragment5();
                int touchIndex = (int) arg3;
                // getting values from selected ListItem
                rid = ridArr.get(touchIndex).toString();
                Bundle args = new Bundle();
                args.putString("rid", rid);
                args.putString("mid", Login.my_id);
                temp.setArguments(args);

                System.out.println("r아이디는 " + rid);

                trans.replace(R.id.root_frame, temp);
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                trans.addToBackStack(null);

                trans.commit();
                Tab.mViewPager.setCurrentItem(1);
            }
        });

        return rootView;
    }



    private class MyResponseTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                HttpParams para = new BasicHttpParams();
                para.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
                HttpClient httpclient = new DefaultHttpClient(para);
                HttpPost httppost = new HttpPost(url_response);
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
                System.out.println("하하" + jsonResult);
                JSONArray jsonMainNode = jsonResponse.optJSONArray("response_count");
                product = jsonMainNode.getJSONObject(0);
                currentTime = mSimpleDateFormat.parse(mTime);
                question_count.setText("나의풀이 수 : " + product.optString("count"));

                jsonMainNode = jsonResponse.optJSONArray("response_list");
                for (int i = 0; i < jsonMainNode.length(); i++) {
                    JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                    ridArr.add(jsonChildNode.optString("r_id"));
                    String title = jsonChildNode.optString("r_title");
                    String reg_date = jsonChildNode.optString("reg_date");

                    compareDate = mSimpleDateFormat.parse(reg_date.substring(0, 10));        //년 월 일 추출
                    if(currentTime.compareTo(compareDate) == 0) {                           //날짜가 같으면 시, 분 추출
                        reg_date = reg_date.substring(11, 16);                                //00:00 형식
                    } else {                                                                //다른 날이면 년 월 일 추출
                        reg_date = reg_date.substring(5, 10);
                        reg_date = reg_date.replaceAll("-", ".");
                    }
                    QuestionList temp = new QuestionList();                                  //매번 만드는 이유. 그래야 하나로 안함 ㅇㅇ

                    temp.setTitle(title);
                    temp.setAddDate(reg_date);
                    question_array.add(temp);

                }
            } catch (JSONException e) {
            } catch (ParseException e ) {
            }
            MyResponseAdapter adapter = new MyResponseAdapter(getActivity(), R.layout.my_response, question_array);
            my_question_list.setAdapter(adapter);
        }
    }
}