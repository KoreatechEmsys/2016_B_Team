package com.pelkan.tab;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

/**
 * 用户注册页面
 */
public class Register extends Activity {
    private static final String TAG = Register.class.getSimpleName();
    private Button registerButton;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText repasswordInput;
    private EditText phone;
    private EditText name;
    private ProgressDialog progressDialog;
    private boolean regFlag = false;
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    //private SessionManager sessionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailInput = (EditText) findViewById(R.id.emailInput);//姓名输入框
        passwordInput = (EditText) findViewById(R.id.passwordInput);//邮箱输入框
        repasswordInput = (EditText) findViewById(R.id.repasswordInput);//密码输入框
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone_num);
        registerButton = (Button) findViewById(R.id.registerButton);//注册按钮


        progressDialog = new ProgressDialog(this);//进度条
        progressDialog.setCancelable(false);

        setting = getSharedPreferences("setting", 0);
        editor= setting.edit();


        //my_id = setting.getString("ID", "");   중요
      //  sessionManager = new SessionManager(getApplicationContext());
        //如果已经登陆,那么跳转到用户信息页面
        /*
        if (sessionManager.isLoggedIn()) {
            Intent intent = new Intent(RegisterActivity.this, UserDetailActivity.class);
            startActivity(intent);
            finish();
        }
        */

        //注册按钮点击操作
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                String repassword = repasswordInput.getText().toString();
                String phoneNum = phone.getText().toString();
                String nameValue = name.getText().toString();
                boolean cancel = false;
                View focusView = null;
                String errorMsg = null;

                //패스워드 비어있는지 확인
                if (TextUtils.isEmpty(password)) {
                    focusView = passwordInput;
                    cancel = true;
                    errorMsg = "비밀번호를 입력해주세요.";
                    //      passwordInput.setError("请输入密码哦");
                } else if (!isPasswordValid(password)) {
                    focusView = passwordInput;
                    cancel = true;
                    errorMsg = "비밀번호는 최소 6자이상 입력하세요.";
                    //          passwordInput.setError("密码长度不能少于6位哦");
                }
                if (!isRepasswordValid(password, repassword)) {
                    focusView = repasswordInput;
                    cancel = true;
                    errorMsg = "같은 비밀번호를 입력해주세요.";
                }
                //메일주소 확인
                if (TextUtils.isEmpty(email)) {
                    focusView = emailInput;
                    cancel = true;
                    errorMsg = "아이디를 입력해주세요.";
                    //        nameInput.setError("请输入名字哦");
                }

                if (TextUtils.isEmpty(nameValue)) {
                    focusView = name;
                    cancel = true;
                    errorMsg = "이름을 입력해주세요.";
                    //      passwordInput.setError("请输入密码哦");
                }

                if (TextUtils.isEmpty(phoneNum)) {
                    focusView = phone;
                    cancel = true;
                    errorMsg = "전화번호를 입력하세요.";
                    //      passwordInput.setError("请输入密码哦");
                }
                //사용자 입력이 적절하면 등록 여부 결정
                if (!cancel) {
                    //차후에 디비에 물어봐서 아이디 등록
                    //registerUser(name, email, password);
                    RegisterUser();
                    //아이디 등록 끝나고 메인화면 이동
                } else {
                    //에러 메세지 출력
                    focusView.requestFocus();
                    cancel = false;
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    // 이메일, 비밀번호 인설트
    public void RegisterUser(){
        String email = emailInput.getText().toString();
        String pass = passwordInput.getText().toString();
        String phoneNum = phone.getText().toString();
        String myName = name.getText().toString();

        register(email, pass, phoneNum, myName);
    }

    private void register(String email, String password, String phoneNum, String myName) {
        class RegisterUser extends AsyncTask<String, Void, String>{
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Register.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent(Register.this, Start.class);
                startActivity(intent1);
                finish();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String > data = new HashMap<String,String>();
                data.put("email",params[0]);
                data.put("password",params[1]);
                data.put("phone",params[2]);
                data.put("name",params[3]);
                System.out.println("메일은" + params[0]+ params[1]);

                String regURL = getString(R.string.powordURL);
                regURL = regURL + "register.php";
                String result = ruc.sendPostRequest(regURL,data);

                System.out.println("싴발 " + result);
                if(result.equals("1")) {
                    result = "이미 존재하는 아이디 입니다.";
                    regFlag = false;
                } else{
                    result = "회원가입에 성공했습니다.";
                    regFlag = true;
                }

                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(email, password, phoneNum, myName);
    }
    //로그인 구현


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //显示进度条
    private void showDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    //隐藏进度条
    private void hideDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    //判断邮箱是否包含@
    private boolean isEmailValid(String email) {
        if(!email.contains("@")) {
            return false;
        }else {
            return true;
        }
    }

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