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
    public static String my_id="";
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    private EditText emailInput;
    private EditText passwordInput;
    private Button loginBtn;
    Boolean loginChecked;
    Start aActivity = (Start)Start.startActivisy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final CheckBox autoLogin;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailInput = (EditText) findViewById(R.id.emailInput);//姓名输入框
        passwordInput = (EditText) findViewById(R.id.passwordInput);//邮箱输入框
        loginBtn = (Button) findViewById(R.id.registerButton);
        autoLogin = (CheckBox) findViewById(R.id.autoLogin);

        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();

        if(setting.getBoolean("Auto_Login_enabled", false)){
            emailInput.setText(setting.getString("ID", ""));
            passwordInput.setText(setting.getString("PW", ""));

            login(emailInput.getText().toString(), passwordInput.getText().toString());
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginChecked = autoLogin.isChecked();

                if(loginChecked == true) {
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
                loadingDialog = ProgressDialog.show(Login.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                String uname = username;
                String pass = password;
                my_id = username;
                //System.out.println("이름은 " + uname + " 이거임");

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
                    Intent intent1 = new Intent(Login.this, Tab.class);
                    startActivity(intent1);
                    finish();
                    aActivity.finish();

                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(username, password);

    }
}
