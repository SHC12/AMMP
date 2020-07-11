package com.mobile.pmmp.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.mobile.pmmp.FilePath;
import com.mobile.pmmp.HomeActivity;
import com.mobile.pmmp.R;
import com.mobile.pmmp.api.ApiInterface;
import com.mobile.pmmp.api.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AksiPetugas extends AppCompatActivity {
    private EditText edtNama,edtUsername,edtPassword;
    private File foto;
    private MaterialButton btnTambah;
    private TextView header;
    private CircleImageView imgFoto,imgOldFoto;
    private String path_foto;
    private ProgressDialog progressDialog;
    private String trigger,mId;
    private boolean gantiFoto = false;
    private ApiInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_petugas);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }
        apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        progressDialog = new ProgressDialog(this);
        edtNama = findViewById(R.id.in_nama_lengkap);
        edtUsername = findViewById(R.id.in_username_daftar);
        edtPassword = findViewById(R.id.in_password);
        imgFoto = findViewById(R.id.img_foto);
        header = findViewById(R.id.textView4);
        imgOldFoto = findViewById(R.id.image_edit_user);
        btnTambah = findViewById(R.id.btn_tambah_petugas);
        trigger = getIntent().getStringExtra("trigger");
        if(trigger.equals("edit")){
            imgOldFoto.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext())
                    .load(getIntent().getStringExtra("foto"))
                    .into(imgOldFoto);
            edtNama.setText(getIntent().getStringExtra("nama"));
            edtUsername.setText(getIntent().getStringExtra("username"));
            edtPassword.setText(getIntent().getStringExtra("password"));
            mId = getIntent().getStringExtra("id");
            header.setText("Form Perubahan Data Petugas");
        }

        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, 42);
            }
        });
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = edtNama.getText().toString().trim();
                String username = edtUsername.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String mPath = path_foto;
                if(trigger.equals("edit")){
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                        if(gantiFoto){
                            editPetugasFoto(mId,nama,username,password,foto);

                        }else{
                            editPetugas(mId,nama,username,password);

                        }

                        }
                else {
                    if(nama.equals("")||username.equals("")||password.equals("")||path_foto.equals("")){
                        Toast.makeText(AksiPetugas.this, "Semua field wajib di isi", Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                            tambahPetugas(nama,username,password,foto);


                }

                }
            }
        });

        initToolbar();
    }

    private void editPetugas(String mId, String nama, String username, String password) {
        Call<ResponseBody> editPetugas = apiInterface.editPetugas(mId,nama,username,password);
        editPetugas.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    try {
                        JSONObject o = new JSONObject(response.body().string());
                        if(o.getString("status").equals("1")){
                            Toast.makeText(AksiPetugas.this, "Data user berhasil di ubah", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AksiPetugas.this,DataPetugas.class).putExtra("trigger","data_petugas"));
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
                progressDialog.dismiss();
            }
        });
    }

    private void tambahPetugas(String nama, String username, String password, File foto) {
        RequestBody resBody = RequestBody.create(MediaType.parse("multipart/form-file"), foto);
        RequestBody rNama = RequestBody.create(MediaType.parse("text/plain"), nama);
        RequestBody rUsername = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody rPassword = RequestBody.create(MediaType.parse("text/plain"), password);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("foto", foto.getName(), resBody);
        Call<ResponseBody> tambahPetugas = apiInterface.submitPetugas(rNama,rUsername,rPassword,partImage);
        tambahPetugas.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    try {
                        JSONObject o = new JSONObject(response.body().string());
                        if(o.getString("status").equals("1")){
                            Toast.makeText(AksiPetugas.this, "Petugas berhasil di tambahkan", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AksiPetugas.this,DataPetugas.class).putExtra("trigger","data_petugas"));
                        }else{
                            Toast.makeText(AksiPetugas.this, "Error", Toast.LENGTH_SHORT).show();
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
                progressDialog.dismiss();
                Toast.makeText(AksiPetugas.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void editPetugasFoto(String id,String nama, String username, String password, File foto) {
        RequestBody resBody = RequestBody.create(MediaType.parse("multipart/form-file"), foto);
        RequestBody rNama = RequestBody.create(MediaType.parse("text/plain"), nama);
        RequestBody rId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody rUsername = RequestBody.create(MediaType.parse("text/plain"), username);
        RequestBody rPassword = RequestBody.create(MediaType.parse("text/plain"), password);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("foto", foto.getName(), resBody);
        Call<ResponseBody> editPetugasFoto = apiInterface.editPetugasFoto(rId,rNama,rUsername,rPassword,partImage);
        editPetugasFoto.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    try {
                        JSONObject o = new JSONObject(response.body().string());
                        if(o.getString("status").equals("1")){
                            Toast.makeText(AksiPetugas.this, "Data petugas berhasil di ubah", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AksiPetugas.this,DataPetugas.class).putExtra("trigger","data_petugas"));
                        }else{
                            Toast.makeText(AksiPetugas.this, "Error", Toast.LENGTH_SHORT).show();
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
                progressDialog.dismiss();
                Toast.makeText(AksiPetugas.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 42:
                if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    path_foto = FilePath.getFilePath(AksiPetugas.this, uri);
                    foto = new File(path_foto);
                    gantiFoto = true;
                    Bitmap myBitmap = BitmapFactory.decodeFile(path_foto);
                    imgFoto.setImageBitmap(myBitmap);
                }
                break;
        }
    }
}