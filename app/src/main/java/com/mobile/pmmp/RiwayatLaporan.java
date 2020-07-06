package com.mobile.pmmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiwayatLaporan extends AppCompatActivity {
    private RecyclerView rvRiwayatLaporan;
    private TableAdapterRiwayatLaporan.RecyclerViewClickListener listener;
    private TableAdapterRiwayatLaporan adapterRiwayatLaporan;
    List<Laporan> list;
    private MaterialButton btnExport;
    private TextView noRiwayat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_laporan);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        btnExport = findViewById(R.id.btn_export_riwayat_laporan);
        rvRiwayatLaporan = findViewById(R.id.rv_riwayat_laporan_petugas_user);
        listener = new TableAdapterRiwayatLaporan.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent i = new Intent(RiwayatLaporan.this,DetailLaporan.class);
                i.putExtra(DetailLaporan.DETAIL_LAPORAN,list.get(position-1));
                startActivity(i);
            }
        };
        noRiwayat = findViewById(R.id.noRiwayat);
        adapterRiwayatLaporan = new TableAdapterRiwayatLaporan(list,getApplicationContext(),listener);
        rvRiwayatLaporan.setLayoutManager(new LinearLayoutManager(this));
        rvRiwayatLaporan.setHasFixedSize(true);
        rvRiwayatLaporan.setAdapter(adapterRiwayatLaporan);
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
                ExportLaporanPetugas(level);
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
    private void getRiwayat(String id_user) {
        ApiInterface apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        Call<List<Laporan>> riwayat = apiInterface.riwayatLaporan(id_user);
        riwayat.enqueue(new Callback<List<Laporan>>() {
            @Override
            public void onResponse(Call<List<Laporan>> call, Response<List<Laporan>> response) {
                if(response.isSuccessful()){
                    list = response.body();
                    adapterRiwayatLaporan = new TableAdapterRiwayatLaporan(list,getApplicationContext(),listener);
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

//    private List<Laporan> getLaporan(){
//        List<Laporan> laporans = new ArrayList<>();
//        laporans.add(new Laporan("1","2020-01-20","Jl.Juanda Raya","Kertas Selip","PT.jpg","normal"));
//        laporans.add(new Laporan("2","2020-01-20","Jl.Pencenogan","Kertas Selip","PT.jpg","normal"));
//        laporans.add(new Laporan("3","2020-01-20","Jl.Pintu Air","Kerusakan Kecil","PT.jpg","normal"));
//        laporans.add(new Laporan("4","2020-01-20","Jl.Batu Tulis","Kerusakan Kecil","PT.jpg","Kerusakan sudah fix"));
//        laporans.add(new Laporan("5","2020-01-20","Jl.H.Agus Salim","Kerusakan Kecil","PT.jpg","Kerusakan sudah fix"));
//        return laporans;
//    }

    private void ExportLaporanPetugas(String level) {

        if(level.equals("1")){
            Intent in = new Intent(RiwayatLaporan.this, PDFViewer.class);
            in.putExtra("trigger", "laporan_user");
            startActivity(in);
        }else if(level.equals("2")){
            Intent in = new Intent(RiwayatLaporan.this, PDFViewer.class);
            in.putExtra("trigger", "laporan_admin");
            startActivity(in);
        }

    }
}