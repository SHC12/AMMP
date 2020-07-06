package com.mobile.pmmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mobile.pmmp.R;
import com.mobile.pmmp.model.Laporan;

public class DetailLaporan extends AppCompatActivity {

    public static final String DETAIL_LAPORAN = "detail_laporan" ;
    private TextView tanggal,lokasi,statusMesin,file,keterangan, penanganan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_laporan);

        Laporan  laporan = getIntent().getParcelableExtra(DETAIL_LAPORAN);
        String mTanggal = laporan.getTanggal();
        String mLokasi = laporan.getLokasi();
        String mStatusMesin = laporan.getLokasi();
        String mFile = laporan.getFile();
        String mKeterangan = laporan.getKeterangan();
        String mPenanganan = laporan.getPenanganan();

        tanggal = findViewById(R.id.tanggalDetailLaporan);
        lokasi = findViewById(R.id.lokasiDetailLaporan);
        statusMesin = findViewById(R.id.statusMesinDetailLaporan);
        file = findViewById(R.id.fileDetailLaporan);
        keterangan = findViewById(R.id.keterangan_laporan);
        penanganan = findViewById(R.id.penangananDetailLaporan);
        penanganan.setText(mPenanganan);
        tanggal.setText(mTanggal);
        lokasi.setText(mLokasi);
        statusMesin.setText(mStatusMesin);
        String lastNameFile = mFile.substring(mFile.lastIndexOf('/')+1);
        file.setText(lastNameFile);
        keterangan.setText(mKeterangan);
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
}