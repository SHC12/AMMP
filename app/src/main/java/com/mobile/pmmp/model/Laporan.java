package com.mobile.pmmp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Laporan implements Parcelable {
    public static final Creator<Laporan> CREATOR = new Creator<Laporan>() {
        @Override
        public Laporan createFromParcel(Parcel in) {
            return new Laporan(in);
        }

        @Override
        public Laporan[] newArray(int size) {
            return new Laporan[size];
        }
    };
    @SerializedName("kode")
    private String kode;
    @SerializedName("tanggal")
    private String tanggal;
    @SerializedName("nomor_mesin")
    private String noMesin;
    @SerializedName("lokasi")
    private String lokasi;
    @SerializedName("permasalahan")
    private String permasalahan;
    @SerializedName("penanganan")
    private String penanganan;
    @SerializedName("file")
    private String file;
    @SerializedName("ket_tambahan")
    private String keterangan;
    @SerializedName("nama_petugas")
    private String nama_petugas;
    @SerializedName("shift")
    private String shift;

    protected Laporan(Parcel in) {
        kode = in.readString();
        tanggal = in.readString();
        noMesin = in.readString();
        lokasi = in.readString();
        permasalahan = in.readString();
        penanganan = in.readString();
        keterangan = in.readString();
        file = in.readString();
        nama_petugas = in.readString();
        shift = in.readString();
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getNama_petugas() {
        return nama_petugas;
    }

    public void setNama_petugas(String nama_petugas) {
        this.nama_petugas = nama_petugas;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNoMesin() {
        return noMesin;
    }

    public void setNoMesin(String noMesin) {
        this.noMesin = noMesin;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getPermasalahan() {
        return permasalahan;
    }

    public void setPermasalahan(String permasalahan) {
        this.permasalahan = permasalahan;
    }

    public String getPenanganan() {
        return penanganan;
    }

    public void setPenanganan(String penanganan) {
        this.penanganan = penanganan;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(kode);
        dest.writeString(tanggal);
        dest.writeString(noMesin);
        dest.writeString(lokasi);
        dest.writeString(permasalahan);
        dest.writeString(penanganan);
        dest.writeString(keterangan);
        dest.writeString(file);
        dest.writeString(nama_petugas);
        dest.writeString(shift);
    }
}
