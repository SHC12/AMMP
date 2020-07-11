package com.mobile.pmmp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.mobile.pmmp.DetailJadwal;
import com.mobile.pmmp.HomeActivity;
import com.mobile.pmmp.JadwalPetugas;
import com.mobile.pmmp.PDFViewer;
import com.mobile.pmmp.R;
import com.mobile.pmmp.adapter.TableAdapterAbsenAdmin;
import com.mobile.pmmp.api.ApiInterface;
import com.mobile.pmmp.api.ApiService;
import com.mobile.pmmp.model.Absen;
import com.mobile.pmmp.model.Jadwal;
import com.mobile.pmmp.user.RiwayatAbsenUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AbsenPetugasAdmin extends AppCompatActivity {
    private RecyclerView rvAbsenAdmin;
    private TableAdapterAbsenAdmin adapterAbsenAdmin;
    private TableAdapterAbsenAdmin.RecyclerViewClickListener listener;
    private List<Jadwal> listAbsen;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_petugas_admin);

      apiInterface = ApiService.getApiClient().create(ApiInterface.class);

        listAbsen = new ArrayList<>();
        rvAbsenAdmin = findViewById(R.id.rv_absen_petugas_admin);
        listener = new TableAdapterAbsenAdmin.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent i = new Intent(AbsenPetugasAdmin.this, DetailJadwal.class);
                i.putExtra(DetailJadwal.DETAIL_JADWAL_PETUGAS,listAbsen.get(position-1));
                i.putExtra("trigger","absen");
                startActivity(i);
            }
        };
        initToolbar();
        adapterAbsenAdmin = new TableAdapterAbsenAdmin(listAbsen,getApplicationContext(),listener);
        rvAbsenAdmin.setLayoutManager(new LinearLayoutManager(this));
        rvAbsenAdmin.setHasFixedSize(true);
        rvAbsenAdmin.setAdapter(adapterAbsenAdmin);

        getAbsen();

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

    private void getAbsen() {
        Call<List<Jadwal>> getAbsen = apiInterface.getAbsensi();
        getAbsen.enqueue(new Callback<List<Jadwal>>() {
            @Override
            public void onResponse(Call<List<Jadwal>> call, Response<List<Jadwal>> response) {
                listAbsen = response.body();
                adapterAbsenAdmin = new TableAdapterAbsenAdmin(listAbsen,getApplicationContext(),listener);
                rvAbsenAdmin.setAdapter(adapterAbsenAdmin);
                adapterAbsenAdmin.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Jadwal>> call, Throwable t) {

            }
        });
    }

//    private List<Absen> getAbsen(){
//        List<Absen> absen = new ArrayList<>();
//        absen.add(new Absen("1","2020-05-30","Hadir","M. Aditya", "1"));
//        absen.add(new Absen("2","2020-05-30","Hadir","Andika Pratama","2"));
//        absen.add(new Absen("3","2020-05-30","Tidak Hadir","Slamet Hariyanto","1"));
//        return absen;
//    }

    public void ExportAbsenAdmin(View view) {
        String fileName = "Absen Petugas.pdf";
        Call<ResponseBody> downloadAbsen = apiInterface.downloadAbsenPetugas();
        downloadAbsen.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    boolean suc = writeResponseBodyToDisk(response.body(),fileName);
                    if(suc){
                        Toast.makeText(AbsenPetugasAdmin.this, "Rekapan berhasil di download, file tersimpan di folder Downloads", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(AbsenPetugasAdmin.this, "Gagal simpan PDF", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(AbsenPetugasAdmin.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(AbsenPetugasAdmin.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body,String path) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    path);

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