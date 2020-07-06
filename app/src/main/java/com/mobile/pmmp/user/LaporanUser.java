package com.mobile.pmmp.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.mobile.pmmp.FilePath;
import com.mobile.pmmp.HomeActivity;
import com.mobile.pmmp.R;
import com.mobile.pmmp.RiwayatLaporan;
import com.mobile.pmmp.api.ApiInterface;
import com.mobile.pmmp.api.ApiService;
import com.mobile.pmmp.model.Jadwal;
import com.mobile.pmmp.model.Laporan;
import com.mobile.pmmp.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanUser extends AppCompatActivity {
    public static final String DETAIL_LAPORAN = "detail_lapor";
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat simpleDateFormat;
    private EditText edtKode,edtTanggal,edtNoMesin,edtLokasi,edtPermasalahan,edtPenanganan,edtKeterangan,edtFile;
    private MaterialButton btnSubmit;
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog;
    String path_file,getSize;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_user);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }
        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        progressDialog = new ProgressDialog(this);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        edtKeterangan = findViewById(R.id.in_keterangan_laporan);
        edtLokasi = findViewById(R.id.in_lokasi_laporan);
        edtTanggal = findViewById(R.id.in_tgl_laporan);
        edtKode = findViewById(R.id.in_kode_laporan);
        edtNoMesin = findViewById(R.id.in_no_mesin_laporan);
        edtPermasalahan = findViewById(R.id.in_permasalahan_laporan);
        edtPenanganan = findViewById(R.id.in_penanganan);
        edtFile = findViewById(R.id.in_file_laporan);

        Jadwal list = getIntent().getParcelableExtra(DETAIL_LAPORAN);
        edtKode.setText(list.getKode_jadwa());
        edtNoMesin.setText(list.getNomor_mesin());
        edtLokasi.setText(list.getLokasi());
        edtPermasalahan.setText(list.getPermasalahan());


        edtFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("*/*");
                startActivityForResult(intent, 42);
            }
        });
        btnSubmit = findViewById(R.id.btn_submit_laporan);

        initToolbar();

        edtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(edtTanggal);
            }
        });
        String id = user.get(SessionManager.ID);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String mKode = edtKode.getText().toString().trim();
               String mTanggal =edtTanggal.getText().toString().trim();
               String mNomesin = edtNoMesin.getText().toString().trim();
               String mLokasi = edtLokasi.getText().toString().trim();
               String mPermasalahan = edtPermasalahan.getText().toString().trim();
               String mPenanganan = edtPenanganan.getText().toString().trim();
               String mFile = path_file;
               String mKeterangan = edtKeterangan.getText().toString().trim();
               if(mKode.equals("") || mTanggal.equals("")||mNomesin.equals("")||mLokasi.equals("")||mPermasalahan.equals("")||mPenanganan.equals("")||mFile.equals("")||mKeterangan.equals("")){
                   Toast.makeText(LaporanUser.this, "Semua field wajib di isi", Toast.LENGTH_SHORT).show();
               }else {
                   int size = (int) getFolderSizeLabel(file);
                   if(size > 5){
                       Toast.makeText(LaporanUser.this, "File yang dipilih melebihi batasan maksimal", Toast.LENGTH_SHORT).show();
                   }else{
                       progressDialog.setMessage("Loading...");
                       progressDialog.show();
                       submitLaporan(id,mKode,mTanggal,mPenanganan,mKeterangan,file);
                   }
               }
            }
        });

    }

    private void submitLaporan(String id, String mKode, String mTanggal, String mPenanganan, String mKeterangan, File file) {
        RequestBody resBody = RequestBody.create(MediaType.parse("multipart/form-file"), file);
        RequestBody rId = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody rKode = RequestBody.create(MediaType.parse("text/plain"), mKode);
        RequestBody rTanggal = RequestBody.create(MediaType.parse("text/plain"), mTanggal);
        RequestBody rPenanganan = RequestBody.create(MediaType.parse("text/plain"), mPenanganan);
        RequestBody rKeterangan = RequestBody.create(MediaType.parse("text/plain"), mKeterangan);
        MultipartBody.Part partFile = MultipartBody.Part.createFormData("file", file.getName(), resBody);
        Call<ResponseBody> submitFile = apiInterface.submitLaporan(rId,rKode,rTanggal,rPenanganan,rKeterangan,partFile);
        submitFile.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    try {
                        JSONObject o = new JSONObject(response.body().string());
                        if(o.getString("status").equals("1")){
                            Toast.makeText(LaporanUser.this, "Laporan berhasil di buat", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LaporanUser.this,RiwayatLaporan.class));
                        }else {
                            Toast.makeText(LaporanUser.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(LaporanUser.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 42:
                if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
//                      path_ktp = getRealPathFromUri(uri, AjukanPeminjamanActivity.this);
                    path_file = FilePath.getFilePath(LaporanUser.this, uri);
                    file = new File(path_file);
                    int size = (int) getFolderSizeLabel(file);
                    if(size > 5){
                        Toast.makeText(this, "File tidak boleh lebih dari 5 Mb", Toast.LENGTH_SHORT).show();
                    }
                    edtFile.setText(file.getName());


                }
                break;

        }
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
    public static long getFolderSizeLabel(File file) {
        long size = getFolderSize(file) / 1024; // Get size and convert bytes into Kb.
        if (size >= 1024) {
            return (size / 1024);
        } else {
            return 1;
        }
    }
    public static long getFolderSize(File file) {
        long size = 0;
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                size += getFolderSize(child);
            }
        } else {
            size = file.length();
        }
        return size;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1001:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Izin Diterima!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Izin Ditolak!", Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }
}