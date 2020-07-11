package com.mobile.pmmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.pmmp.R;
import com.mobile.pmmp.admin.DetailDataPetugas;
import com.mobile.pmmp.api.ApiInterface;
import com.mobile.pmmp.api.ApiService;
import com.mobile.pmmp.model.Laporan;
import com.mobile.pmmp.utils.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailLaporan extends AppCompatActivity {

    public static final String DETAIL_LAPORAN = "detail_laporan" ;
    private TextView tanggal,lokasi,permasalahan,file,keterangan, penanganan,kode,nama,shift,noMesin,title;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan);
        SessionManager sessionManager = new SessionManager(this);
        Laporan  laporan = getIntent().getParcelableExtra(DETAIL_LAPORAN);
        String mTanggal = laporan.getTanggal();
        String mLokasi = laporan.getLokasi();
        String mPermasalahan = laporan.getPermasalahan();
        String mNoMesin = laporan.getNoMesin();
        String mFile = laporan.getFile();
        String mKeterangan = laporan.getKeterangan();
        String mPenanganan = laporan.getPenanganan();
        String mShift = laporan.getShift();
        String mNama = laporan.getNama_petugas();
        String mKode = laporan.getKode();

        progressDialog = new ProgressDialog(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        title = findViewById(R.id.title_detail_laporan);
        tanggal = findViewById(R.id.tanggalDetailLaporan);
        lokasi = findViewById(R.id.lokasiDetailLaporan);
        permasalahan = findViewById(R.id.permasalahanDetailLaporan);
        file = findViewById(R.id.fileDetailLaporan);
        keterangan = findViewById(R.id.keterangan_laporan);
        kode = findViewById(R.id.kodeDetailLaporan);
        nama = findViewById(R.id.namaDetailLaporan);
        shift = findViewById(R.id.shiftDetailLaporan);
        noMesin = findViewById(R.id.noMesinDetailLaporan);
        penanganan = findViewById(R.id.penangananDetailLaporan);
        penanganan.setText(mPenanganan);
        tanggal.setText(mTanggal);
        lokasi.setText(mLokasi);
        shift.setText(mShift);
        kode.setText(mKode);
        nama.setText(mNama);
        noMesin.setText(mNoMesin);
        permasalahan.setText(mPermasalahan);
        String lastNameFile = mFile.substring(mFile.lastIndexOf('/')+1);
        file.setText(lastNameFile);
        keterangan.setText(mKeterangan);
        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                downloadFile(mFile);
            }
        });
        if(user.get(SessionManager.LEVEL).equals("2")){
            title.setText("Detail Laporan Hasil Kerja");
        }
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

    private void downloadFile(String mFile) {
        ApiInterface apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        Call<ResponseBody> downloadImage = apiInterface.downloadFoto(mFile);
        downloadImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    boolean suc = writeResponseBodyToDisk(response.body(),mFile);
                    if(suc){
                        String path = mFile.substring(mFile.lastIndexOf("."));
                        String name = mFile.substring(mFile.lastIndexOf('/')+1);
                        String foler = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+name;
                        if(path.equals(".mp4")){
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(foler), "video/mp4");
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(Uri.parse(foler), "image/*");
                            startActivity(intent);
                        }
                    }else{

                    }

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(DetailLaporan.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(DetailLaporan.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String path) {
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