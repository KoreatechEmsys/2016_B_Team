package com.pelkan.tab;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class Start extends Activity {
    public static Activity startActivisy;
    private BackPressCloseHandler backPressCloseHandler;            //두번 누르면 종료
    private String packageName = "com.pelkan.tab";
    private String versionName = "";
    PackageInfo pi = null;
    private ProgressDialog pDialog;
    private String nowVersion = "";
    AlertDialog.Builder alt_bld;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    MarketVersionChecker mChecker = new MarketVersionChecker();
    public static ArrayList<KnowledgeMap> maps = new ArrayList<KnowledgeMap>();
    public static ArrayList<Integer> type_size = new ArrayList<Integer>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();
        startActivisy = Start.this;
        Button bt1 = (Button)findViewById(R.id.btn1);
        bt1.setText("회원가입");

        Button bt2 = (Button)findViewById(R.id.btn2);
        bt2.setText("로그인");

        editor.putString("add_sequence", "0");
        editor.commit();

        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            nowVersion = pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.getStackTrace();
        }

//        new GetProductDetails().execute();

        if(setting.getBoolean("Auto_Login_enabled", false)){
            login(setting.getString("ID", ""), setting.getString("PW", ""));
        }

        backPressCloseHandler = new BackPressCloseHandler(this);            //두번 누르면 종료
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), Register.class);
                startActivity(intent1);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent1 = new Intent(getApplicationContext(), Tab.class);
//                startActivity(intent1);

                Intent intent1 = new Intent(getApplicationContext(), Login.class);
                startActivity(intent1);

/*
                Intent intent1 = new Intent(getApplicationContext(), MediaPlayerActivity.class);
                startActivity(intent1);
                */

            }
        });
    }

    private void login(final String username, final String password) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(Start.this, "Please wait", "Loading..");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = username;
                String pass = password;
                Login.my_id = username;
                //System.out.println("이름은 " + uname + "
                // 이거임");

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("m_id", uname));
                nameValuePairs.add(new BasicNameValuePair("m_pass", pass));
                String result = null;

                try{
                    String logURL = getString(R.string.powordURL);
                    logURL = logURL + "login.php";
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(logURL);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                loadingDialog.dismiss();

                if(s.equalsIgnoreCase("success")){
                    Intent intent1 = new Intent(Start.this, Tab.class);
                    startActivity(intent1);
                    finish();

                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(username, password);

    }
    @Override                       //두번 누르면 종료
    public void onBackPressed() {
        // TODO Auto-generated method stub
        backPressCloseHandler.onBackPressed();
    }


    class GetProductDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Start.this);
            pDialog.setMessage("업데이트 확인중...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... params) {
            System.out.println("패키지이름 " + packageName);
            versionName = MarketVersionChecker.getMarketVersionFast(packageName);
            return null;
        }
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            alt_bld = new AlertDialog.Builder(Start.this);

            if (!versionName.equals(nowVersion)) {
                System.out.println("핫하");
                alt_bld.setMessage("업데이트 후 사용해주세요.")
                        .setCancelable(false)
                        .setPositiveButton("업데이트 바로가기",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        Intent marketLaunch = new Intent(
                                                Intent.ACTION_VIEW);
                                        marketLaunch.setData(Uri
                                                .parse("https://play.google.com/store/apps/details?id=com.pelkan.tab"));
                                        startActivity(marketLaunch);
                                        finish();
                                    }
                                });
                AlertDialog alert = alt_bld.create();
                alert.setTitle("안 내");
                alert.show();
            }
        }
    }
}
