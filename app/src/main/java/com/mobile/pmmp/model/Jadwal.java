package com.mobile.pmmp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Jadwal implements Parcelable {
    public static final Creator<Jadwal> CREATOR = new Creator<Jadwal>() {
        @Override
        public Jadwal createFromParcel(Parcel in) {
            return new Jadwal(in);
        }

        @Override
        public Jadwal[] newArray(int size) {
            return new Jadwal[size];
        }
    };
    @SerializedName("jenis")
    private String jenis_jadwal;
    @SerializedName("kode")
    private String kode_jadwa;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("shift")
    private String shift;
    @SerializedName("nama_petugas")
    private String nama;
    @SerializedName("lokasi")
    private String lokasi;
    @SerializedName("nomor_mesin")
    private String nomor_mesin;
    @SerializedName("permasalahan")
    private String permasalahan;
    @SerializedName("status_mesin")
    private String status_mesin;

    protected Jadwal(Parcel in) {
        jenis_jadwal = in.readString();
        kode_jadwa = in.readString();
        tanggal = in.readString();
        shift = in.readString();
        nama = in.readString();
        lokasi = in.readString();
        nomor_mesin = in.readString();
        permasalahan = in.readString();
        status_mesin = in.readString();
    }

    public String getJenis_jadwal() {
        return jenis_jadwal;
    }

    public void setJenis_jadwal(String jenis_jadwal) {
        this.jenis_jadwal = jenis_jadwal;
    }

    public String getKode_jadwa() {
        return kode_jadwa;
    }

    public void setKode_jadwa(String kode_jadwa) {
        this.kode_jadwa = kode_jadwa;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getNomor_mesin() {
        return nomor_mesin;
    }

    public void setNomor_mesin(String nomor_mesin) {
        this.nomor_mesin = nomor_mesin;
    }

    public String getPermasalahan() {
        return permasalahan;
    }

    public void setPermasalahan(String permasalahan) {
        this.permasalahan = permasalahan;
    }

    public String getStatus_mesin() {
        return status_mesin;
    }

    public void setStatus_mesin(String status_mesin) {
        this.status_mesin = status_mesin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jenis_jadwal);
        dest.writeString(kode_jadwa);
        dest.writeString(tanggal);
        dest.writeString(shift);
        dest.writeString(nama);
        dest.writeString(lokasi);
        dest.writeString(nomor_mesin);
        dest.writeString(permasalahan);
        dest.writeString(status_mesin);
    }
}
