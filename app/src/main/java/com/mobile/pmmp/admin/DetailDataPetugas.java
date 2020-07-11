package com.mobile.pmmp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.mobile.pmmp.HomeActivity;
import com.mobile.pmmp.R;
import com.mobile.pmmp.api.ApiInterface;
import com.mobile.pmmp.api.ApiService;
import com.mobile.pmmp.model.Petugas;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailDataPetugas extends AppCompatActivity {

    public static final String DETAIL_PETUGAS = "detail_petugas" ;
    private TextView id,nama,username,password;
    private CircleImageView imagePetugas;
    private ProgressDialog progressDialog;
    private MaterialButton btnEdit,btnHapus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_data_petugas);
        id = findViewById(R.id.idDetailPetugas);
        nama = findViewById(R.id.namaDetailPetugas);
        username = findViewById(R.id.usernameDetailPetugas);
        password = findViewById(R.id.passwordDetailPetugas);
        imagePetugas = findViewById(R.id.img_petugas);
        btnEdit = findViewById(R.id.btn_edit_user);
        btnHapus = findViewById(R.id.btn_hapus_user);

        progressDialog = new ProgressDialog(this);
        Petugas petugas = getIntent().getParcelableExtra(DETAIL_PETUGAS);
        String mId = petugas.getIdPetugas();
        String mNama = petugas.getNamaLengkap();
        String mUsername = petugas.getUsername();
        String mPassword = petugas.getPassword();
        String mFoto = petugas.getImagePetugas();

        Glide.with(getApplicationContext())
                .load(mFoto)
                .placeholder(R.drawable.vaa)
                .into(imagePetugas);

        id.setText(mId);
        nama.setText(mNama);
        username.setText(mUsername);
        password.setText(mPassword);
        
        imagePetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Downloading Image");
                progressDialog.show();
                downloadFoto(mFoto);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailDataPetugas.this, AksiPetugas.class);
                i.putExtra("id",mId);
                i.putExtra("foto",mFoto);
                i.putExtra("nama",mNama);
                i.putExtra("username",mUsername);
                i.putExtra("password",mPassword);
                i.putExtra("trigger","edit");
                startActivity(i);
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusUser(mId);
            }
        });
        initToolbar();
    }

    private void hapusUser(String mId) {
        ApiInterface apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        Call<ResponseBody> deleteUser = apiInterface.deleteUser(mId);
        deleteUser.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject o = new JSONObject(response.body()
                        .string());
                        if(o.getString("status").equals("1")){
                            Toast.makeText(DetailDataPetugas.this, "User berhasil di hapus", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DetailDataPetugas.this,DataPetugas.class).putExtra("trigger","data_petugas"));
                        }else{
                            Toast.makeText(DetailDataPetugas.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DetailDataPetugas.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadFoto(String mFoto) {
       ApiInterface apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        Call<ResponseBody> downloadImage = apiInterface.downloadFoto(mFoto);
        downloadImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(DetailDataPetugas.this, ""+mFoto, Toast.LENGTH_SHORT).show();
             if(response.isSuccessful()){
                 progressDialog.dismiss();
                 boolean suc = writeResponseBodyToDisk(response.body(),mFoto);
                 if(suc){
                     Toast.makeText(DetailDataPetugas.this, "Berhasil simpan foto", Toast.LENGTH_SHORT).show();

                 }else{
                     Toast.makeText(DetailDataPetugas.this, "Gagal simpan foto", Toast.LENGTH_SHORT).show();
                 }

             }else{
                 progressDialog.dismiss();
                 Toast.makeText(DetailDataPetugas.this, "error", Toast.LENGTH_SHORT).show();
             }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailDataPetugas.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
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
    private boolean writeResponseBodyToDisk(ResponseBody body,String path) {
        String getName = path.substring(path.lastIndexOf('/')+1);
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    getName);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }


}