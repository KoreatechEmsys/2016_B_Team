package com.pelkan.tab;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.List;

public class Register extends Activity {
    private Button registerButton;                              //ui 컴포넌트
    private EditText idInput;
    private EditText passwordInput;
    private EditText repasswordInput;
    private EditText phone;
    private EditText name;
    private ProgressDialog progressDialog;
    
    private boolean regFlag = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        idInput = (EditText) findViewById(R.id.idInput);              //뷰에서 아이디 찾아 인스턴스받음
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        repasswordInput = (EditText) findViewById(R.id.repasswordInput);
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone_num);
        registerButton = (Button) findViewById(R.id.registerButton);


        progressDialog = new ProgressDialog(this);							//진행바
        progressDialog.setCancelable(false);

        //회원가입 버튼 누르면
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idInput.getText().toString();				//입력된 정보 담기
                String password = passwordInput.getText().toString();
                String repassword = repasswordInput.getText().toString();
                String phoneNum = phone.getText().toString();
                String nameValue = name.getText().toString();
                
                boolean cancel = false;
                View focusView = null;
                String errorMsg = null;

                //패스워드 검사
                if (TextUtils.isEmpty(password)) {
                    focusView = passwordInput;
                    cancel = true;
                    errorMsg = "비밀번호를 입력해주세요.";
                } else if (!isPasswordValid(password)) {
                    focusView = passwordInput;
                    cancel = true;
                    errorMsg = "비밀번호는 최소 6자이상 입력하세요.";
                }
                if (!isRepasswordValid(password, repassword)) {
                    focusView = repasswordInput;
                    cancel = true;
                    errorMsg = "같은 비밀번호를 입력해주세요.";
                }
                //아이디 검사
                if (TextUtils.isEmpty(id)) {
                    focusView = idInput;
                    cancel = true;
                    errorMsg = "아이디를 입력해주세요.";
                }
                //이름 검사
                if (TextUtils.isEmpty(nameValue)) {
                    focusView = name;
                    cancel = true;
                    errorMsg = "이름을 입력해주세요.";
                }
                //전화번호 검사
                if (TextUtils.isEmpty(phoneNum)) {
                    focusView = phone;
                    cancel = true;
                    errorMsg = "전화번호를 입력하세요.";
                }
                //사용자 입력이 적절하면 등록 여부 결정
                if (!cancel) {												//차후에 디비에 물어봐서 아이디 등록
                    RegisterUser();											//회원등록
                } else {
                    focusView.requestFocus();								//에러 메세지 출력
                    cancel = false;
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    // 이메일, 비밀번호 db에 저장
    public void RegisterUser(){
        String id = idInput.getText().toString();
        String pass = passwordInput.getText().toString();
        String phoneNum = phone.getText().toString();
        String myName = name.getText().toString();

        register(id, pass, phoneNum, myName);
    }

    private void register(String id, String password, String phoneNum, String myName) {
        class RegisterUser extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "잠시 기다려주세요",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                if(regFlag == true) {
                	Intent intent1 = new Intent(Register.this, Start.class);
                    startActivity(intent1);
                    finish();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String > data = new HashMap<String,String>();
                data.put("id",params[0]);
                data.put("password",params[1]);
                data.put("phone",params[2]);
                data.put("name",params[3]);
                
                try{
                    String regURL = getString(R.string.powordURL);
                    regURL = regURL + "/regLogin/register.php";
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
                    
                    if(result.equals("1")) {
                        result = "이미 존재하는 아이디 입니다.";
                        regFlag = false;
                    } else{
                        result = "회원가입에 성공했습니다.";
                        regFlag = true;
                    }
                    
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(id, password, phoneNum, myName);
    }

    //진행바 
    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }


    private void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }
    //비밀번호 검사 함수
    private boolean isRepasswordValid(String password, String repassword) {
        if(password.equals(repassword)) {
            return true;
        }else {
            return false;
        }
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            return false;
        }else {
            return true;
        }
    }
}
