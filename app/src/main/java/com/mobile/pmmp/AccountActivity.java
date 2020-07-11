package com.mobile.pmmp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.mobile.pmmp.admin.AksiPetugas;
import com.mobile.pmmp.admin.DataPetugas;
import com.mobile.pmmp.api.ApiInterface;
import com.mobile.pmmp.api.ApiService;
import com.mobile.pmmp.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {
    private CircleImageView akunImage;
    private MaterialButton btnGantiPhoto,btnGantiPassword,toTambahUser,toHapusUser;
    private EditText newPassword;
    private File newFoto;
    private String fotoPath,id,nama;
    private SessionManager sessionManager;
    private ApiInterface apiInterface;
    private TextView username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        setContentView(R.layout.activity_account);
        akunImage = findViewById(R.id.img_akun);
        btnGantiPassword = findViewById(R.id.btnGantiPassword);
        btnGantiPhoto = findViewById(R.id.btn_ganti_photo);
        toHapusUser = findViewById(R.id.toHapusUser);
        toTambahUser = findViewById(R.id.toTambahUser);
        newPassword = findViewById(R.id.in_password_reset);
        username = findViewById(R.id.akun_username);
        password = findViewById(R.id.akun_password);
        username.setText(user.get(SessionManager.USERNAME));
        password.setText(user.get(SessionManager.PASSWORD));
        id = user.get(SessionManager.ID);
        nama = user.get(SessionManager.NAMA);
        Glide.with(getApplicationContext())
                .load(user.get(SessionManager.FOTO))
                .into(akunImage);
        btnGantiPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, 42);
            }
        });
        if(user.get(SessionManager.LEVEL).equals("1")){
            toHapusUser.setVisibility(View.GONE);
            toTambahUser.setVisibility(View.GONE);
        }else if(user.get(SessionManager.LEVEL).equals("3")){
            toHapusUser.setVisibility(View.GONE);
            toTambahUser.setVisibility(View.GONE);
        }
        btnGantiPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass = newPassword.getText().toString().trim();
                if(newPass.equals("")){
                    Toast.makeText(AccountActivity.this, "Form password wajib di isi", Toast.LENGTH_SHORT).show();
                }else{
                    gantiPassword(id,newPass);

                }
            }
        });

        toTambahUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, AksiPetugas.class).putExtra("trigger","tambah"));
            }
        });

        toHapusUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountActivity.this, DataPetugas.class).putExtra("trigger","hapus_user"));
            }
        });
        initGantiPhoto();
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle(null);

    }
    private void gantiPassword(String id,String newPass) {
        Call<ResponseBody> gantPass = apiInterface.updatePassword(id,newPass);
        gantPass.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject o = new JSONObject(response.body().string());
                        if(o.getString("status").equals("1")){
                            Toast.makeText(AccountActivity.this, "Password berhasil di ganti,Silahkan login ulang", Toast.LENGTH_SHORT).show();
                            sessionManager.logout();
                        }else {
                            Toast.makeText(AccountActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AccountActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initGantiPhoto() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 42:
                if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    fotoPath = FilePath.getFilePath(AccountActivity.this, uri);
                    newFoto = new File(fotoPath);
                    String pathSession = "https://adity.xyz/foto_petugas/"+nama+fotoPath.substring(fotoPath.lastIndexOf("."));
                    Toast.makeText(this, ""+pathSession, Toast.LENGTH_SHORT).show();
                    sessionManager.editor.putString("FOTO",pathSession).commit();
                    updateImage(id, nama, newFoto);
                    Bitmap myBitmap = BitmapFactory.decodeFile(fotoPath);
                    akunImage.setImageBitmap(myBitmap);

                }
                break;
        }

    }

    private void updateImage(String id,String name,File foto){
        RequestBody resBody = RequestBody.create(MediaType.parse("multipart/form-file"), foto);
        RequestBody rId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody rName = RequestBody.create(MediaType.parse("text/plain"), name);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("foto", foto.getName(), resBody);
        Call<ResponseBody> updateImage = apiInterface.updateImage(rId, rName, partImage);
        updateImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject o = new JSONObject(response.body().string());
                        if (o.getString("status").equals("1")) {
                            Toast.makeText(AccountActivity.this, "Update Photo Profile Berhasil", Toast.LENGTH_SHORT).show();

                        } else if (o.getString("status").equals("0")) {
                            Toast.makeText(AccountActivity.this, "Update Gagal,Terjadi Kesalahan", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AccountActivity.this, ""+response.errorBody(), Toast.LENGTH_LONG).show();
                    Log.e("ERROR  FOTO",""+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AccountActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Izin Diterima!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Izin Ditolak!", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
}