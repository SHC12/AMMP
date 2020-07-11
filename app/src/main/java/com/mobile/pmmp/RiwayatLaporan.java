package com.mobile.pmmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.mobile.pmmp.adapter.TableAdapterRiwayatLaporan;
import com.mobile.pmmp.api.ApiInterface;
import com.mobile.pmmp.api.ApiService;
import com.mobile.pmmp.model.Laporan;
import com.mobile.pmmp.user.RiwayatAbsenUser;
import com.mobile.pmmp.utils.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatLaporan extends AppCompatActivity {
    private RecyclerView rvRiwayatLaporan;
    private TableAdapterRiwayatLaporan.RecyclerViewClickListener listener;
    private TableAdapterRiwayatLaporan adapterRiwayatLaporan;
    List<Laporan> list;
    DownloadFilePDFTask downloadFileTask;
    private MaterialButton btnExport;
    private TextView noRiwayat,title;
    private String adapterTrigger,namaPetugas,trigger;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_laporan);
        apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        btnExport = findViewById(R.id.btn_export_riwayat_laporan);
        title = findViewById(R.id.title_riwayat_laporan);
        trigger = getIntent().getStringExtra("trigger");
        if(trigger.equals("laporan")){
            title.setText("Laporan Hasil Kerja");
            adapterTrigger = "laporan";
        }else{
            adapterTrigger = "riwayat";
        }
        rvRiwayatLaporan = findViewById(R.id.rv_riwayat_laporan_petugas_user);
        listener = new TableAdapterRiwayatLaporan.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent i = new Intent(RiwayatLaporan.this,DetailLaporan.class);
                i.putExtra(DetailLaporan.DETAIL_LAPORAN,list.get(position-1));
                startActivity(i);
            }
        };
        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, 101);
        noRiwayat = findViewById(R.id.noRiwayat);
        adapterRiwayatLaporan = new TableAdapterRiwayatLaporan(list,getApplicationContext(),listener,adapterTrigger);
        rvRiwayatLaporan.setLayoutManager(new LinearLayoutManager(this));
        rvRiwayatLaporan.setHasFixedSize(true);
        rvRiwayatLaporan.setAdapter(adapterRiwayatLaporan);
        namaPetugas = user.get(SessionManager.NAMA);
        String id_user = user.get(SessionManager.ID);
        String level = user.get(SessionManager.LEVEL);
        if(level.equals("1")){
            getRiwayat(id_user);
        }else{
            String id = "All";
            getRiwayat(id);
        }
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trigger.equals("laporan")){
                    ExportLaporanAdmin();
                }else{
                    ExportLaporanPetugas(id_user);
                }

            }
        });




        initToolbar();

    }

    private void ExportLaporanAdmin() {
        Call<ResponseBody> downloadMapel = apiInterface.downloadLaporanAdmin();
        downloadMapel.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    downloadFileTask = new DownloadFilePDFTask();
                    downloadFileTask.execute(response.body());


                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
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
    private void getRiwayat(String id_user) {
        ApiInterface apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        Call<List<Laporan>> riwayat = apiInterface.riwayatLaporan(id_user);
        riwayat.enqueue(new Callback<List<Laporan>>() {
            @Override
            public void onResponse(Call<List<Laporan>> call, Response<List<Laporan>> response) {
                if(response.isSuccessful()){
                    list = response.body();
                    adapterRiwayatLaporan = new TableAdapterRiwayatLaporan(list,getApplicationContext(),listener,adapterTrigger);
                    rvRiwayatLaporan.setAdapter(adapterRiwayatLaporan);
                    adapterRiwayatLaporan.notifyDataSetChanged();
                    if(list.isEmpty()){
                        rvRiwayatLaporan.setVisibility(View.INVISIBLE);
                        noRiwayat.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Laporan>> call, Throwable t) {

            }
        });

    }


    private void ExportLaporanPetugas(String idPetugas) {

        Call<ResponseBody> downloadMapel = apiInterface.downloadLaporanPetugas(idPetugas);
        downloadMapel.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    downloadFileTask = new DownloadFilePDFTask();
                    downloadFileTask.execute(response.body());


                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void saveToDisk(ResponseBody body, String filename) {
        try {

            File destinationFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(destinationFile);
                byte[] data = new byte[4096];
                int count;
                int progress = 0;
                long fileSize = body.contentLength();
                while ((count = inputStream.read(data)) != -1) {
                    outputStream.write(data, 0, count);
                    progress += count;
                    Pair<Integer, Long> pairs = new Pair<>(progress, fileSize);
                    downloadFileTask.doProgress(pairs);
                }

                outputStream.flush();

                Pair<Integer, Long> pairs = new Pair<>(100, 100L);
                downloadFileTask.doProgress(pairs);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                Pair<Integer, Long> pairs = new Pair<>(-1, Long.valueOf(-1));
                downloadFileTask.doProgress(pairs);
                return;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(RiwayatLaporan.this, permission) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(RiwayatLaporan.this, permission)) {
                ActivityCompat.requestPermissions(RiwayatLaporan.this, new String[]{permission}, requestCode);

            } else {
                ActivityCompat.requestPermissions(RiwayatLaporan.this, new String[]{permission}, requestCode);
            }
        } else if (ContextCompat.checkSelfPermission(RiwayatLaporan.this, permission) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getApplicationContext(), "Permission was denied", Toast.LENGTH_SHORT).show();
        }
    }
    private class DownloadFilePDFTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(ResponseBody... urls) {
            //Copy you logic to calculate progress and call
            if(trigger.equals("laporan")){
                saveToDisk(urls[0], "Laporan Hasil Kerja" + ".pdf");

            }else {
                saveToDisk(urls[0], "Laporan Maintenance Petugas " + namaPetugas+ ".pdf");

            }
            return null;
        }

        protected void onProgressUpdate(Pair<Integer, Long>... progress) {

            if (progress[0].first == 100) {
                Toast.makeText(getApplicationContext(), "File PDF berhasil di download, tersimpan di folder Downloads", Toast.LENGTH_SHORT).show();
            } else if (progress[0].second > 0) {
                int currentProgress = (int) ((double) progress[0].first / (double) progress[0].second * 100);

            }

            if (progress[0].first == -1) {
                Toast.makeText(getApplicationContext(), "Download failed", Toast.LENGTH_SHORT).show();
            }

        }

        public void doProgress(Pair<Integer, Long> progressDetails) {
            publishProgress(progressDetails);
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

}