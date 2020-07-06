package com.mobile.pmmp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Absen implements Parcelable {
    private String no;
    private String tanggal;
    private String nama;
    private String shift;


    public Absen(String no, String tanggal,String status,String nama, String shift) {
        this.no = no;
        this.tanggal = tanggal;
        this.status = status;
        this.nama = nama;
        this.shift = shift;
    }

    protected Absen(Parcel in) {
        no = in.readString();
        tanggal = in.readString();
        status = in.readString();
        nama = in.readString();
        shift = in.readString();
    }

    public static final Creator<Absen> CREATOR = new Creator<Absen>() {
        @Override
        public Absen createFromParcel(Parcel in) {
            return new Absen(in);
        }

        @Override
        public Absen[] newArray(int size) {
            return new Absen[size];
        }
    };

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(no);
        dest.writeString(tanggal);
        dest.writeString(status);
        dest.writeString(nama);
        dest.writeString(shift);
    }
}
