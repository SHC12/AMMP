package com.mobile.pmmp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.mobile.pmmp.HomeActivity;
import com.mobile.pmmp.JadwalPetugas;
import com.mobile.pmmp.R;
import com.mobile.pmmp.api.ApiInterface;
import com.mobile.pmmp.api.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitJadwalPetugas extends AppCompatActivity {
    private String[] shift = {"Shift 1","Shift 2"};
    private String[] jenis_jadwal = {"Jadwal Rutin","Jadwal Komplain"};
    private String[] permasalahan = {"Mesin Mati","Kertas Habis","Baterai Lowbat"};
    private String[] lokasi = {"Jl.Juanda Raya","Jl.Pencongan","Jl.Pintu Air","Jl.Batu Tulis","Jl.H.Agus Salim"};
    private ArrayList<String> namaPetugas = new ArrayList<>();
    private AutoCompleteTextView spJenisJadwal,spShift,spNama,spLokasi,spPermasalahan,spNomorMesin;
    private EditText edtTanggal,edtKode;
    private String newKode;
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;
    private MaterialButton btnSubmitJadwal;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_jadwal_petugas);

        progressDialog = new ProgressDialog(this);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        edtTanggal = findViewById(R.id.in_tgl_laporan);
        edtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(edtTanggal);
            }
        });
        edtKode = findViewById(R.id.in_kode_jadwal);
        spJenisJadwal = findViewById(R.id.in_jenis_jadwal);
        spShift = findViewById(R.id.in_shift);
        spNama = findViewById(R.id.in_nama_petugas);
        spLokasi = findViewById(R.id.in_lokasi);
        spPermasalahan = findViewById(R.id.in_permasalahan);
        spNomorMesin = findViewById(R.id.in_nomor_mesin);
        btnSubmitJadwal = findViewById(R.id.btn_submit_jadwal);
        initSpinner();
        getPetugas(spNama);
        initToolbar();

        btnSubmitJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jenis = spJenisJadwal.getText().toString().trim();
                String kode = edtKode.getText().toString().trim();
                String tanggal = edtTanggal.getText().toString().trim();
                String shifts = spShift.getText().toString().trim();
                String namaPetugas = spNama.getText().toString().trim();
                String lokasi = spLokasi.getText().toString().trim();
                String nomorMesin = spNomorMesin.getText().toString().trim();
                String permasalahan = spPermasalahan.getText().toString().trim();
                if(jenis.equals("") || kode.equals("")||tanggal.equals("")||shifts.equals("")||namaPetugas.equals("")||lokasi.equals("")||nomorMesin.equals("")||permasalahan.equals("")){
                    Toast.makeText(SubmitJadwalPetugas.this, "Semua field wajib di isi", Toast.LENGTH_SHORT).show();
                }else{
                    submitJadwal(jenis,kode,tanggal,shifts,namaPetugas,lokasi,nomorMesin,permasalahan);
                }
            }
        });

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
    private void submitJadwal(String jenis, String kode, String tanggal, String shift, String namaPetugas, String lokasi, String nomorMesin, String permasalahan) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    Call<ResponseBody> submit = apiInterface.submitJadwal(jenis,kode,tanggal,shift,namaPetugas,lokasi,nomorMesin,permasalahan);
    submit.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if(response.isSuccessful()){
                progressDialog.dismiss();
                try {
                    JSONObject o = new JSONObject(response.body().string());
                    if(o.getString("status").equals("1")){
                        Toast.makeText(SubmitJadwalPetugas.this, "Jadwal berhasil di upload", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SubmitJadwalPetugas.this, JadwalPetugas.class));
                    }else if(o.getString("status").equals("2")){
                        Toast.makeText(SubmitJadwalPetugas.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                progressDialog.dismiss();
                Toast.makeText(SubmitJadwalPetugas.this, "Koneksi", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            progressDialog.dismiss();
            Toast.makeText(SubmitJadwalPetugas.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    });
    }




    private void initSpinner() {
        getSpinner(spJenisJadwal, jenis_jadwal);
        getSpinner(spShift, shift);
        getSpinner(spLokasi, lokasi);
        getSpinnerNoMesin(spNomorMesin,numberArray(200));
        getSpinner(spPermasalahan, permasalahan);
        spJenisJadwal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                progressDialog.setMessage("Loading...");
                progressDialog.show();
                String jenis = spJenisJadwal.getText().toString().trim();
                getKode(jenis);
            }
        });
    }

    private ArrayList<String> numberArray(int jumlah){
        ArrayList<String> getNum = new ArrayList<>();
        for(int i =0;i<=jumlah;i++){
            getNum.add(""+i);
        }
        return getNum;
    }

    private void getPetugas(AutoCompleteTextView nama) {
        progressDialog.show();
        progressDialog.setMessage("Loading...");
        Call<ResponseBody> getPetugas = apiInterface.getPetugas();
            getPetugas.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        try {
                            JSONObject o = new JSONObject(response.body().string());
                            JSONArray a = o.getJSONArray("Petugas");
                            for (int i = 0; i < a.length(); i++) {
                                JSONObject ao = a.getJSONObject(i);
                                namaPetugas.add(ao.getString("nama"));
                            }
                            Log.d("API",""+namaPetugas.size());
                            String[] listPetugas = namaPetugas.toArray(new String[namaPetugas.size()]);
                            getSpinner(nama, listPetugas);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(SubmitJadwalPetugas.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

            private void getKode(String jenis) {
                Call<ResponseBody> getKode = apiInterface.lastjadwal(jenis);
                getKode.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            progressDialog.dismiss();
                            try {
                                JSONObject o = new JSONObject(response.body().string());
                                if(o.getString("status").equals("1")){
                                    newKode = o.getString("kode");
                                    edtKode.setText(newKode);
                                }else{
                                    Toast.makeText(SubmitJadwalPetugas.this, "Kesalahan", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(SubmitJadwalPetugas.this, "Koneksi bermasalah", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(SubmitJadwalPetugas.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }


    private void getSpinner(AutoCompleteTextView target, String[] item) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_item, item);
        target.setAdapter(adapter);
    }

    private void getSpinnerNoMesin(AutoCompleteTextView target, ArrayList<String> item) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_item, item);
        target.setAdapter(adapter);
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
}