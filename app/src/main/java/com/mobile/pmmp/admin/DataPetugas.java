package com.mobile.pmmp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.pmmp.HomeActivity;
import com.mobile.pmmp.PDFViewer;
import com.mobile.pmmp.R;
import com.mobile.pmmp.adapter.TableAdapterDataPetugas;
import com.mobile.pmmp.api.ApiInterface;
import com.mobile.pmmp.api.ApiService;
import com.mobile.pmmp.model.Petugas;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataPetugas extends AppCompatActivity {
    private RecyclerView rvDataPetugas;
    private TableAdapterDataPetugas adapterDataPetugas;
    private TableAdapterDataPetugas.RecyclerViewClickListener listener;
    private List<Petugas> listPetugas;
    private ApiInterface apiInterface;
    private TextView noDataPetugas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_petugas);
         apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        rvDataPetugas = findViewById(R.id.rv_data_petugas_admin);
        listPetugas = new ArrayList<>();
        listener = new TableAdapterDataPetugas.RecyclerViewClickListener() {
            @Override
            public void onRowClick(View view, int position) {
                Intent i = new Intent(DataPetugas.this,DetailDataPetugas.class);
                i.putExtra(DetailDataPetugas.DETAIL_PETUGAS,listPetugas.get(position-1));
                startActivity(i);
            }
        };
        noDataPetugas = findViewById(R.id.noDataPetugas);
        adapterDataPetugas = new TableAdapterDataPetugas(listPetugas,getApplicationContext(),listener);
        rvDataPetugas.setLayoutManager(new LinearLayoutManager(this));
        rvDataPetugas.setHasFixedSize(true);
        rvDataPetugas.setAdapter(adapterDataPetugas);
        adapterDataPetugas.notifyDataSetChanged();
        getPetugas();
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
    private void getPetugas() {
        Call<List<Petugas>> getPetugas = apiInterface.getPetugasAdmin();
        getPetugas.enqueue(new Callback<List<Petugas>>() {
            @Override
            public void onResponse(Call<List<Petugas>> call, Response<List<Petugas>> response) {
                if(response.isSuccessful()){
                    listPetugas = response.body();
                    adapterDataPetugas = new TableAdapterDataPetugas(listPetugas,getApplicationContext(),listener);
                    rvDataPetugas.setAdapter(adapterDataPetugas);
                    adapterDataPetugas.notifyDataSetChanged();
                    if(listPetugas.isEmpty()){
                        rvDataPetugas.setVisibility(View.INVISIBLE);
                        noDataPetugas.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Petugas>> call, Throwable t) {

            }
        });
    }

//    private List<Petugas> getPetugas(){
//        List<Petugas> petugases = new ArrayList<>();
//        petugases.add(new Petugas("1","001","M. Aditya ","aditya","123"));
//        petugases.add(new Petugas("2","002","Andika Pratama","andika","123"));
//        petugases.add(new Petugas("3","003","Slamet Hariyanto","slamet","123"));
//
//
//        return petugases;
//
//    }

    public void toInsertDataPetugas(View view) {
        startActivity(new Intent(DataPetugas.this,TambahPetugas.class));
    }

    public void ExportDataPetugas(View view) {
        Intent intent =  new Intent(DataPetugas.this, PDFViewer.class);
        intent.putExtra("trigger", "data_petugas");
        startActivity(intent);
    }
}