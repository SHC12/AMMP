package com.mobile.pmmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mobile.pmmp.adapter.TableAdapterStatusMesin;
import com.mobile.pmmp.api.ApiInterface;
import com.mobile.pmmp.api.ApiService;
import com.mobile.pmmp.model.Jadwal;
import com.mobile.pmmp.user.LaporanUser;
import com.mobile.pmmp.user.StatusMesin;
import com.mobile.pmmp.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanMaintenance extends AppCompatActivity {
    private RecyclerView rvList;
    private TableAdapterStatusMesin adapter;
    private TableAdapterStatusMesin.RecyclerViewClickListener listener;
    private List<Jadwal> list ;
    private ApiInterface apiInterface;
    private TextView noJadwal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_maintenance);
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user=sessionManager.getUserDetail();
        apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        list = new ArrayList<>();
        listener = new TableAdapterStatusMesin.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent i = new Intent(LaporanMaintenance.this, LaporanUser.class);
                i.putExtra(LaporanUser.DETAIL_LAPORAN,list.get(position-1));
                startActivity(i);
            }
        };
        noJadwal = findViewById(R.id.noLaporanMaintenance);
        rvList = findViewById(R.id.rv_list_maintenance);
        adapter = new TableAdapterStatusMesin(list,getApplicationContext(),listener);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setHasFixedSize(true);
        rvList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        String idUser = user.get(SessionManager.ID);
        String jenis = "Maintenance";
        getList(idUser,jenis);

        initToolbar();

    }

    private void getList(String idUser,String jenis) {
        Call<List<Jadwal>> getList = apiInterface.jadwalPetugas(idUser,jenis);
        getList.enqueue(new Callback<List<Jadwal>>() {
            @Override
            public void onResponse(Call<List<Jadwal>> call, Response<List<Jadwal>> response) {
                if(response.isSuccessful()){
                    list = response.body();
                    adapter = new TableAdapterStatusMesin(list,getApplicationContext(),listener);
                    rvList.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if(list.isEmpty()){
                        rvList.setVisibility(View.INVISIBLE);
                        noJadwal.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Jadwal>> call, Throwable t) {

            }
        });
    }

    public void toRiwayarLaporan(View view) {
        startActivity(new Intent(LaporanMaintenance.this,RiwayatLaporan.class).putExtra("trigger","riwayat"));
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
}