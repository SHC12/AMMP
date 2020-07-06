package com.mobile.pmmp.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mobile.pmmp.PDFViewer;
import com.mobile.pmmp.R;
import com.mobile.pmmp.adapter.TableAdapterRiwayatAbsen;
import com.mobile.pmmp.model.Absen;

import java.util.ArrayList;
import java.util.List;

public class RiwayatAbsenUser extends AppCompatActivity {
    private RecyclerView rvRiwayatAbsen;
    private TableAdapterRiwayatAbsen adapterRiwayatAbsen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_absen_user);
        rvRiwayatAbsen = findViewById(R.id.rv_riwayat_absen_petugas_user);
        adapterRiwayatAbsen = new TableAdapterRiwayatAbsen(getAbsen(),getApplicationContext());
        rvRiwayatAbsen.setLayoutManager(new LinearLayoutManager(this));
        rvRiwayatAbsen.setHasFixedSize(true);
        rvRiwayatAbsen.setAdapter(adapterRiwayatAbsen);
    }

    private List<Absen> getAbsen(){
        List<Absen> absen = new ArrayList<>();
        absen.add(new Absen("1","2020-05-30","Hadir","M. Aditya", "1"));
        absen.add(new Absen("2","2020-05-30","Hadir","Andika Pratama","2"));
        absen.add(new Absen("3","2020-05-30","Tidak Hadir","Slamet Hariyanto","1"));
        return absen;
    }

    public void ExportAbsenUser(View view) {
        Intent in = new Intent(RiwayatAbsenUser.this, PDFViewer.class);
        in.putExtra("trigger", "absen_user");
        startActivity(in);
    }
}