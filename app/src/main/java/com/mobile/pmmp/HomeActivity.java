package com.mobile.pmmp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mobile.pmmp.admin.DataPetugas;
import com.mobile.pmmp.model.Laporan;
import com.mobile.pmmp.user.LaporanUser;
import com.mobile.pmmp.user.StatusMesin;
import com.mobile.pmmp.utils.SessionManager;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private SessionManager sessionManager;
    private CircleImageView imageUser;
    private TextView nameUser;
    HashMap<String, String> user;
    String level;
    private CardView cvUser,cvAdmin,cvKaop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();
        user = sessionManager.getUserDetail();
        level = user.get(SessionManager.LEVEL);
        imageUser = findViewById(R.id.img_user);
        nameUser = findViewById(R.id.name_user);
        cvUser = findViewById(R.id.cv_user);
        cvAdmin = findViewById(R.id.cv_admin);
        cvKaop = findViewById(R.id.cv_kaop);
        getDetailUser();
        if(level.equals("1")){
            cvUser.setVisibility(View.VISIBLE);
            initViewUser();
        }else if(level.equals("2")){
            cvAdmin.setVisibility(View.VISIBLE);
            initViewAdmin();
        }else if(level.equals("3")){
            cvKaop.setVisibility(View.VISIBLE);
        }


    }

    private void initViewAdmin() {
        LinearLayout btnJadwalPetugas = (LinearLayout)findViewById(R.id.jadwal_petugas_user);
//        LinearLayout btnCheckStatus = (LinearLayout)findViewById(R.id.check_status_mesin);
//        LinearLayout btnLaporanMaintenance = (LinearLayout)findViewById(R.id.laporan_maintenance_user);
//        LinearLayout btnAkun = (LinearLayout)findViewById(R.id.akun_user);
        LinearLayout btnLogout = (LinearLayout)findViewById(R.id.logout_user);
        LinearLayout btnLogoutAdmin = (LinearLayout)findViewById(R.id.logout_admin);
        LinearLayout btnDataPetugasAdmin = (LinearLayout)findViewById(R.id.data_petugas);
        LinearLayout btnAkunUser = (LinearLayout)findViewById(R.id.akun_user);

        btnJadwalPetugas.setOnClickListener(this);
//        btnCheckStatus.setOnClickListener(this);
//        btnLaporanMaintenance.setOnClickListener(this);
//        btnAkun.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnLogoutAdmin.setOnClickListener(this);
        btnDataPetugasAdmin.setOnClickListener(this);
        btnAkunUser.setOnClickListener(this);
    }

    private void initViewUser() {
        LinearLayout btnJadwalPetugas = (LinearLayout)findViewById(R.id.jadwal_petugas_user);
        LinearLayout btnCheckStatus = (LinearLayout)findViewById(R.id.check_status_mesin);
        LinearLayout btnLaporanMaintenance = (LinearLayout)findViewById(R.id.laporan_maintenance_user);
        LinearLayout btnAkun = (LinearLayout)findViewById(R.id.akun_user);
        LinearLayout btnLogout = (LinearLayout)findViewById(R.id.logout_user);

        btnJadwalPetugas.setOnClickListener(this);
        btnCheckStatus.setOnClickListener(this);
        btnLaporanMaintenance.setOnClickListener(this);
        btnAkun.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
    }

    private void getDetailUser() {
        Glide.with(getApplicationContext())
                .load(user.get(SessionManager.FOTO))
                .placeholder(R.drawable.vaa_title)
                .into(imageUser);
        nameUser.setText(user.get(SessionManager.NAMA));
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.jadwal_petugas_admin:
            case R.id.check_status_mesin:
                startActivity(new Intent(HomeActivity.this, StatusMesin.class));
                break;
            case R.id.jadwal_petugas_user:
            startActivity(new Intent(HomeActivity.this, JadwalPetugas.class));
            break;

            case R.id.laporan_maintenance_user:
                startActivity(new Intent(HomeActivity.this, LaporanMaintenance.class));
                break;



            case R.id.data_petugas:
                startActivity(new Intent(HomeActivity.this, DataPetugas.class));
                break;

            case R.id.logout_admin:
            case R.id.logout_user:
                sessionManager.logout();
            break;
        }
    }
}