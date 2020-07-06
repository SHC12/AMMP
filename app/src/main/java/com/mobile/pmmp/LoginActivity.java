package com.mobile.pmmp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.mobile.pmmp.api.ApiInterface;
import com.mobile.pmmp.api.ApiService;
import com.mobile.pmmp.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText username,password;
    private MaterialButton btnLogin;
    private SessionManager sessionManager;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        username = findViewById(R.id.in_username_login);
        password = findViewById(R.id.in_password_login);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mUsername = username.getText().toString().trim();
                String mPassword = password.getText().toString().trim();
                if(mUsername.equals("") || mPassword.equals("")){
                    Toast.makeText(LoginActivity.this, "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.setMessage("Loading");
                    progressDialog.show();
                    login(mUsername,mPassword);
                }
            }
        });
    }

    private void login(String username,String password) {
        ApiInterface apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        Call<ResponseBody> login = apiInterface.login(username,password);
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    try {
                        JSONObject o = new JSONObject(response.body().string());
                        if(o.getString("status").equals("1")){
                            String id = o.getString("id_user");
                            String username = o.getString("username");
                            String nama = o.getString("nama");
                            String level = o.getString("level");
                            String password = o.getString("password");
                            String foto = o.getString("foto");

                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            sessionManager.createSession(id,username,nama,level,password,foto);

                        }else {
                            Toast.makeText(LoginActivity.this, "Username atau password salah", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Koneksi internet bermasalah", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}