package com.mobile.pmmp.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.pmmp.DetailJadwal;
import com.mobile.pmmp.HomeActivity;
import com.mobile.pmmp.JadwalPetugas;
import com.mobile.pmmp.R;
import com.mobile.pmmp.adapter.TableAdapterJadwal;
import com.mobile.pmmp.adapter.TableAdapterStatusMesin;
import com.mobile.pmmp.api.ApiInterface;
import com.mobile.pmmp.api.ApiService;
import com.mobile.pmmp.model.Jadwal;
import com.mobile.pmmp.utils.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusMesin extends AppCompatActivity {
    private RecyclerView rv_statusMesin;
    private TableAdapterStatusMesin adapter;
    private TableAdapterStatusMesin.RecyclerViewClickListener listener;
    private List<Jadwal> listStatusMesin;
    private ApiInterface apiInterface;
    private TextView noStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_mesin);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        listStatusMesin = new ArrayList<>();
        rv_statusMesin = findViewById(R.id.rv_status_mesin);
        listener = new TableAdapterStatusMesin.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent i = new Intent(StatusMesin.this,DetailJadwal.class);
                i.putExtra(DetailJadwal.DETAIL_JADWAL_PETUGAS,listStatusMesin.get(position-1));
                startActivity(i);
            }
        };
        noStatus = findViewById(R.id.noStatusMesin);
        adapter = new TableAdapterStatusMesin(listStatusMesin,getApplicationContext(),listener);
        rv_statusMesin.setLayoutManager(new LinearLayoutManager(this));
        rv_statusMesin.setHasFixedSize(true);
        rv_statusMesin.setAdapter(adapter);
        String idUser = user.get(SessionManager.ID);
        getStatusMesin(idUser, "All");
        initToolbar();
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
    private void getStatusMesin(String id_user, String jr) {
        Call<List<Jadwal>> rutinUser = apiInterface.jadwalPetugas(id_user,jr);
        rutinUser.enqueue(new Callback<List<Jadwal>>() {
            @Override
            public void onResponse(Call<List<Jadwal>> call, Response<List<Jadwal>> response) {
                if(response.isSuccessful()){
                 //   Toast.makeText(StatusMesin.this, ""+response.body().size(), Toast.LENGTH_SHORT).show();
                    listStatusMesin = response.body();
                    adapter = new TableAdapterStatusMesin(listStatusMesin,getApplicationContext(),listener);
                    rv_statusMesin.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if(listStatusMesin.isEmpty()){
                        rv_statusMesin.setVisibility(View.INVISIBLE);
                        noStatus.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Jadwal>> call, Throwable t) {

            }
        });
    }
}