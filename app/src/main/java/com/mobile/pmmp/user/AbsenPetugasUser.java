package com.mobile.pmmp.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.mobile.pmmp.R;
import com.mobile.pmmp.model.Absen;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AbsenPetugasUser extends AppCompatActivity {
    private AutoCompleteTextView spinner;
    private String[] status_absen = {"Hadir","Tidak hadir","Sakit","Cuti"};
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat;
    private EditText edtTanggal,edtLokasi,edtKeterangan;
    private MaterialButton btnAbsen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen_petugas_user);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        edtKeterangan = findViewById(R.id.in_keterangan);
        edtLokasi = findViewById(R.id.in_lokasi);
        edtTanggal = findViewById(R.id.in_tgl_absen);
        btnAbsen = findViewById(R.id.btn_submit_absen);

        spinner = findViewById(R.id.in_status_absen);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.spinner_list_item,status_absen);
        spinner.setAdapter(adapter);

        edtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(edtTanggal);
            }
        });

        btnAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AbsenPetugasUser.this, "Berhasil Absen", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AbsenPetugasUser.this,RiwayatAbsenUser.class));
            }
        });









    }

    private void showDateDialog(final EditText edt_target) {

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {


                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                edt_target.setText(simpleDateFormat.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    public void toRiwayatAbsen(View view) {
        startActivity(new Intent(AbsenPetugasUser.this,RiwayatAbsenUser.class));
    }
}