package com.mobile.pmmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mobile.pmmp.adapter.TableAdapterJadwal;
import com.mobile.pmmp.admin.SubmitJadwalPetugas;
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

public class JadwalPetugas extends AppCompatActivity {
    private RecyclerView rv_jadwalRutin,rv_jadwalComplain;
    private TableAdapterJadwal adapter,adapterKomplain;
    private FloatingActionButton floatAddJadwal;
    String jk = "Jadwal Komplain";
    String jr = "Jadwal Rutin";
    private TextView noJadwalRutin,noJadwalKomplain;
    private TableAdapterJadwal.RecyclerViewClickListener listenerRutin,listenerKomplain;
    private ApiInterface apiInterface;
    private List<Jadwal> listRutin,listKomplain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_petugas_user);
        apiInterface = ApiService.getApiClient().create(ApiInterface.class);

        listRutin = new ArrayList<>();
        listKomplain = new ArrayList<>();
        configView();


    }

    private void configView() {
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        TextView namaPetugas =(TextView) findViewById(R.id.tx_nama_petugas);
        namaPetugas.setText("Nama Petugas : "+user.get(SessionManager.NAMA));
        String level = user.get(SessionManager.LEVEL);
        String id_user = user.get(SessionManager.ID);
        floatAddJadwal = findViewById(R.id.float_add_jadwal);
        noJadwalKomplain = findViewById(R.id.noJadwalKomplain);
        noJadwalRutin = findViewById(R.id.noJadwalRutin);
        listenerRutin = new TableAdapterJadwal.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent i = new Intent(JadwalPetugas.this, DetailJadwal.class);
                i.putExtra(DetailJadwal.DETAIL_JADWAL_PETUGAS,listRutin.get(position-1));
                startActivity(i);
            }
        };

        listenerKomplain = new TableAdapterJadwal.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent i = new Intent(JadwalPetugas.this, DetailJadwal.class);
                i.putExtra(DetailJadwal.DETAIL_JADWAL_PETUGAS,listKomplain.get(position-1));
                startActivity(i);
            }
        };

        if(level.equals("2")){
            namaPetugas.setVisibility(View.GONE);
            floatAddJadwal.setVisibility(View.VISIBLE);
            floatAddJadwal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(JadwalPetugas.this, SubmitJadwalPetugas.class));
                }
            });
        }
        adapterKomplain = new TableAdapterJadwal(listKomplain,getApplicationContext(),listenerKomplain);
        adapter = new TableAdapterJadwal(listRutin,getApplicationContext(),listenerRutin);
        rv_jadwalRutin = findViewById(R.id.rv_jadwal_rutin_petugas);
        rv_jadwalRutin.setLayoutManager(new LinearLayoutManager(this));
        rv_jadwalRutin.setAdapter(adapter);
        rv_jadwalComplain = findViewById(R.id.rv_jadwal_komplain_petugas);
        rv_jadwalComplain.setLayoutManager(new LinearLayoutManager(this));
        rv_jadwalComplain.setAdapter(adapterKomplain);
        if(level.equals("1")){
            getRutinUser(id_user,jr);
            getKomplainUser(id_user,jk);
        } else if(level.equals("2")){
            getRutinAdmin(jr);
            getKomplainAdmin(jk);
        }

        initToolbar();


    }


    private void getRutinUser(String id_user, String jr) {
        Call<List<Jadwal>> rutinUser = apiInterface.jadwalPetugas(id_user,jr);
        rutinUser.enqueue(new Callback<List<Jadwal>>() {
            @Override
            public void onResponse(Call<List<Jadwal>> call, Response<List<Jadwal>> response) {
                if(response.isSuccessful()){
                    listRutin = response.body();
                    adapter = new TableAdapterJadwal(listRutin,getApplicationContext(),listenerRutin);
                    rv_jadwalRutin.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if(listRutin.isEmpty()){
                        rv_jadwalRutin.setVisibility(View.INVISIBLE);
                        noJadwalRutin.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Jadwal>> call, Throwable t) {

            }
        });
    }
    private void getKomplainUser(String id_user, String jk) {
        Call<List<Jadwal>> rutinUser = apiInterface.jadwalPetugas(id_user,jk);
        rutinUser.enqueue(new Callback<List<Jadwal>>() {
            @Override
            public void onResponse(Call<List<Jadwal>> call, Response<List<Jadwal>> response) {
                if(response.isSuccessful()){
                    listKomplain = response.body();
                    adapterKomplain = new TableAdapterJadwal(listKomplain,getApplicationContext(),listenerKomplain);
                    rv_jadwalComplain.setAdapter(adapterKomplain);
                    adapterKomplain.notifyDataSetChanged();
                    if(listKomplain.isEmpty()){
                        rv_jadwalComplain.setVisibility(View.INVISIBLE);
                        noJadwalKomplain.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Jadwal>> call, Throwable t) {

            }
        });
    }



    private void getRutinAdmin(String jr) {
        Call<List<Jadwal>> getRutin = apiInterface.jadwalAdmin(jr);
        getRutin.enqueue(new Callback<List<Jadwal>>() {
            @Override
            public void onResponse(Call<List<Jadwal>> call, Response<List<Jadwal>> response) {
                if(response.isSuccessful()){
                    listRutin = response.body();
                    adapter = new TableAdapterJadwal(listRutin,getApplicationContext(),listenerRutin);
                    rv_jadwalRutin.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    if(listRutin.isEmpty()){
                        rv_jadwalRutin.setVisibility(View.INVISIBLE);
                        noJadwalRutin.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Jadwal>> call, Throwable t) {

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

    private void getKomplainAdmin(String jk) {
        Call<List<Jadwal>> getRutin = apiInterface.jadwalAdmin(jk);
        getRutin.enqueue(new Callback<List<Jadwal>>() {
            @Override
            public void onResponse(Call<List<Jadwal>> call, Response<List<Jadwal>> response) {
                if(response.isSuccessful()){
                    listKomplain = response.body();
                    adapterKomplain = new TableAdapterJadwal(listKomplain,getApplicationContext(),listenerKomplain);
                    rv_jadwalComplain.setAdapter(adapterKomplain);
                    adapterKomplain.notifyDataSetChanged();
                    if(listKomplain.isEmpty()){
                        rv_jadwalComplain.setVisibility(View.INVISIBLE);
                        noJadwalKomplain.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Jadwal>> call, Throwable t) {

            }
        });
    }


}