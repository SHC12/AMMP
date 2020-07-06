package com.mobile.pmmp;

import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDFViewer extends AppCompatActivity {

    PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_viewer);

        pdfView = findViewById(R.id.pdfViewer);
        String trigger = getIntent().getStringExtra("trigger");
        String namaFileKtp = getIntent().getStringExtra("ktp");
        String namaFileNpwp = getIntent().getStringExtra("npwp");
        if(trigger.equals("absen_user")){

            pdfView.fromAsset("riwayat_absen_petugas.pdf").load();
        }
        if(trigger.equals("laporan_user")){
            pdfView.fromAsset("riwayat_laporan.pdf").load();
        }
        if(trigger.equals("absen_admin")){
            pdfView.fromAsset("rekap_absen_admin.pdf").load();
        }
        if(trigger.equals("laporan_admin")){
            pdfView.fromAsset("riwayat_laporan_admin.pdf").load();
        }
        if(trigger.equals("data_petugas")){
            pdfView.fromAsset("data_petugas_admin.pdf").load();
        }



    }
}
