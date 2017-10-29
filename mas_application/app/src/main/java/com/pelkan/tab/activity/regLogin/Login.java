package com.pelkan.tab;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class Login extends ActionBarActivity {
    private EditText idInput;
    private EditText passwordInput;
    private Button loginBtn;
    private CheckBox autoLogin;
    Boolean loginChecked;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    
    Start aActivity = (Start)Start.startActivisy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        idInput = (EditText) findViewById(R.id.idInput);				//뷰에서 인스턴스 받음
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        loginBtn = (Button) findViewById(R.id.registerButton);
        autoLogin = (CheckBox) findViewById(R.id.autoLogin);

        setting = getSharedPreferences("setting", 0);					//이전에 저장된 값 읽어오기
        editor= setting.edit();

        if(setting.getBoolean("Auto_Login_enabled", false)){			//자동로그인 설정 유무 확인
            emailInput.setText(setting.getString("ID", ""));
            passwordInput.setText(setting.getString("PW", ""));

            login(emailInput.getText().toString(), passwordInput.getText().toString());
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginChecked = autoLogin.isChecked();

                if(loginChecked == true) {								//자동로그인 설정 유무에따라 설정한 값 저장
                    editor.putString("ID", emailInput.getText().toString());
                    editor.putString("PW", passwordInput.getText().toString());
                    editor.putBoolean("Auto_Login_enabled", true);
                    editor.commit();
                }
                login(emailInput.getText().toString(), passwordInput.getText().toString());
            }
        });
    }

    private void login(final String username, final String password) {

        class LoginAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(Login.this, "잠시 기다려주세요",null, true, true);
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = username;
                String pass = password;

                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("m_id", uname));
                nameValuePairs.add(new BasicNameValuePair("m_pass", pass));
                String result = null;

                try{
                    String logURL = getString(R.string.powordURL);
                    logURL = logURL + "regLogin/login.php";
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(logURL);
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line);
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
                    Intent intent1 = new Intent(Login.this, Tab.class);
                    startActivity(intent1);
                    finish();
                    aActivity.finish();

                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "유효하지 않은 사용자 정보입니다", Toast.LENGTH_LONG).show();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(username, password);

    }
}
