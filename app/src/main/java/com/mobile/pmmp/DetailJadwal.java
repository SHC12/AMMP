package com.mobile.pmmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.mobile.pmmp.R;
import com.mobile.pmmp.api.ApiInterface;
import com.mobile.pmmp.api.ApiService;
import com.mobile.pmmp.model.Jadwal;
import com.mobile.pmmp.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailJadwal extends AppCompatActivity {

    public static final String DETAIL_JADWAL_PETUGAS = "detail_jadwal" ;
    private TextView kode,jenis,tanggal,noMesin,lokasi,status, shift, petugas,permasalahan,title;
    private MaterialButton btnDelete;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_jadwal_petugas);

        SessionManager sessionManager = new SessionManager(this);
        progressDialog = new ProgressDialog(this);
        tanggal = findViewById(R.id.tanggalDetailJadwal);
        lokasi = findViewById(R.id.lokasiDetailJadwal);
        permasalahan = findViewById(R.id.permasalahanDetailJadwal);
        shift = findViewById(R.id.shiftDetailJadwal);
        title = findViewById(R.id.title_detail_jadwal);
        petugas = findViewById(R.id.petugasDetailJadwal);
        kode = findViewById(R.id.kodeDetailJadwal);
        jenis = findViewById(R.id.jenisDetailJadwal);
        status = findViewById(R.id.statusMesinDetailJadwal);
        noMesin = findViewById(R.id.noMesinDetailJadwal);
        btnDelete = findViewById(R.id.btn_hapus_jadwal);

        HashMap<String, String> user = sessionManager.getUserDetail();
        String level = user.get(SessionManager.LEVEL);
        if(level.equals("1")){
            btnDelete.setVisibility(View.GONE);
        }
        String trigger = getIntent().getStringExtra("trigger");
        if(trigger.equals("absen")){
            btnDelete.setVisibility(View.GONE);
            title.setText("Detail Absen Petugas");
        }else{

        }
        Jadwal jadwal = getIntent().getParcelableExtra(DETAIL_JADWAL_PETUGAS);
        String mKode = jadwal.getKode_jadwa();
        String mJenis = jadwal.getJenis_jadwal();
        String mTanggal = jadwal.getTanggal();
        String mStatus = jadwal.getStatus_mesin();
        String mPermasalahan = jadwal.getPermasalahan();
        String mLokasi = jadwal.getLokasi();
        String mNama = jadwal.getNama();
        String mShift = jadwal.getShift();
        String mNoMesin = jadwal.getNomor_mesin();


        kode.setText(mKode);
        jenis.setText(mJenis);
        noMesin.setText(mNoMesin);
        tanggal.setText(mTanggal);
        lokasi.setText(mLokasi);
        permasalahan.setText(mPermasalahan);
        shift.setText(mShift);
        petugas.setText(mNama);
        if(mStatus.equals("0")){
            String gStatus = "Belum Di Kerjakan";
            status.setText(gStatus);
        }else{
            String gStatus = "Sudah Di Kerjakan";
            status.setText(gStatus);
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Loading..");
                progressDialog.show();
                hapusJadwal(mKode);
            }
        });

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
    private void hapusJadwal(String mKode) {
        ApiInterface apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        Call<ResponseBody> delete = apiInterface.deleteJadwal(mKode);
        delete.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    try {
                        JSONObject o = new JSONObject(response.body().string());
                        if(o.getString("status").equals("1")){
                            Toast.makeText(DetailJadwal.this, "Jadwal berhasil di hapus", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(DetailJadwal.this,JadwalPetugas.class));
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
                Toast.makeText(DetailJadwal.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}