package com.mobile.pmmp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mobile.pmmp.PDFViewer;
import com.mobile.pmmp.R;
import com.mobile.pmmp.adapter.TableAdapterAbsenAdmin;
import com.mobile.pmmp.model.Absen;
import com.mobile.pmmp.user.RiwayatAbsenUser;

import java.util.ArrayList;
import java.util.List;

public class AbsenPetugasAdmin extends AppCompatActivity {
    private RecyclerView rvAbsenAdmin;
    private TableAdapterAbsenAdmin adapterAbsenAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_petugas_admin);

        rvAbsenAdmin = findViewById(R.id.rv_absen_petugas_admin);
        adapterAbsenAdmin = new TableAdapterAbsenAdmin(getAbsen(),getApplicationContext());
        rvAbsenAdmin.setLayoutManager(new LinearLayoutManager(this));
        rvAbsenAdmin.setHasFixedSize(true);
        rvAbsenAdmin.setAdapter(adapterAbsenAdmin);

    }

    private List<Absen> getAbsen(){
        List<Absen> absen = new ArrayList<>();
        absen.add(new Absen("1","2020-05-30","Hadir","M. Aditya", "1"));
        absen.add(new Absen("2","2020-05-30","Hadir","Andika Pratama","2"));
        absen.add(new Absen("3","2020-05-30","Tidak Hadir","Slamet Hariyanto","1"));
        return absen;
    }

    public void ExportAbsenAdmin(View view) {
        Intent in = new Intent(AbsenPetugasAdmin.this, PDFViewer.class);
        in.putExtra("trigger", "absen_admin");
        startActivity(in);
    }
}