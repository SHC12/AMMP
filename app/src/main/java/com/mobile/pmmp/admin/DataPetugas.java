package com.mobile.pmmp.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.mobile.pmmp.HomeActivity;
import com.mobile.pmmp.PDFViewer;
import com.mobile.pmmp.R;
import com.mobile.pmmp.adapter.TableAdapterDataPetugas;
import com.mobile.pmmp.api.ApiInterface;
import com.mobile.pmmp.api.ApiService;
import com.mobile.pmmp.model.Petugas;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataPetugas extends AppCompatActivity {
    private RecyclerView rvDataPetugas;
    private TableAdapterDataPetugas adapterDataPetugas;
    private TableAdapterDataPetugas.RecyclerViewClickListener listener;
    private List<Petugas> listPetugas;
    private ApiInterface apiInterface;
    private MaterialButton btnExport,btnTambahPetugas;
    private TextView noDataPetugas,title_bawah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_petugas);
         apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        rvDataPetugas = findViewById(R.id.rv_data_petugas_admin);
        btnExport = findViewById(R.id.btnExportDataPetugas);
        btnTambahPetugas = findViewById(R.id.btn_tambah_petugas);
        title_bawah = findViewById(R.id.textView7);
        listPetugas = new ArrayList<>();
        String trigger = getIntent().getStringExtra("trigger");
        if(trigger.equals("data_petugas")){
            listener = new TableAdapterDataPetugas.RecyclerViewClickListener() {
                @Override
                public void onRowClick(View view, int position) {
                    Intent i = new Intent(DataPetugas.this,DetailDataPetugas.class);
                    i.putExtra(DetailDataPetugas.DETAIL_PETUGAS,listPetugas.get(position-1));
                    startActivity(i);
                }
            };
        }else{
            listener = new TableAdapterDataPetugas.RecyclerViewClickListener() {
                @Override
                public void onRowClick(View view, int position) {
                    hapusUser(listPetugas.get(position-1).getIdPetugas());
                    listPetugas.remove(listPetugas.get(position-1));
                    adapterDataPetugas.notifyItemRemoved(position);
                    adapterDataPetugas.notifyItemRangeChanged(position,listPetugas.size());
                }
            };
            title_bawah.setText("Klik nama untuk hapus");
            btnTambahPetugas.setVisibility(View.GONE);
            btnExport.setVisibility(View.GONE);

        }
        btnTambahPetugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toInsertDataPetugas();
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExportDataPetugas();
            }
        });
        noDataPetugas = findViewById(R.id.noDataPetugas);
        adapterDataPetugas = new TableAdapterDataPetugas(listPetugas,getApplicationContext(),listener);
        rvDataPetugas.setLayoutManager(new LinearLayoutManager(this));
        rvDataPetugas.setHasFixedSize(true);
        rvDataPetugas.setAdapter(adapterDataPetugas);
        adapterDataPetugas.notifyDataSetChanged();
        getPetugas();
        initToolbar();
    }

    private void hapusUser(String mId) {
        ApiInterface apiInterface = ApiService.getApiClient().create(ApiInterface.class);
        Call<ResponseBody> deleteUser = apiInterface.deleteUser(mId);
        deleteUser.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        JSONObject o = new JSONObject(response.body()
                                .string());
                        if(o.getString("status").equals("1")){
                            Toast.makeText(DataPetugas.this, "User berhasil di hapus", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(DataPetugas.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DataPetugas.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
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
               onBackPressed();
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


    private void toInsertDataPetugas() {
        startActivity(new Intent(DataPetugas.this, AksiPetugas.class).putExtra("trigger","tambah"));
    }

    private void ExportDataPetugas() {
        String fileName = "Data Petugas.pdf";
        Call<ResponseBody> downloadDataPetugas = apiInterface.downloadDataPetugas();
        downloadDataPetugas.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    boolean suc = writeResponseBodyToDisk(response.body(),fileName);
                    if(suc){
                        Toast.makeText(DataPetugas.this, "PDF berhasil di download, file tersimpan di folder Downloads", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(DataPetugas.this, "Gagal simpan PDF", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(DataPetugas.this, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(DataPetugas.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(DataPetugas.this,HomeActivity.class));
    }

    private boolean writeResponseBodyToDisk(ResponseBody body,String path) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    path);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

}