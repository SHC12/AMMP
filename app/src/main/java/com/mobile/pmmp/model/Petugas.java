package com.mobile.pmmp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Petugas implements Parcelable {
    public static final Creator<Petugas> CREATOR = new Creator<Petugas>() {
        @Override
        public Petugas createFromParcel(Parcel in) {
            return new Petugas(in);
        }

        @Override
        public Petugas[] newArray(int size) {
            return new Petugas[size];
        }
    };
    @SerializedName("no")
    private String no;
    @SerializedName("id_user")
    private String idPetugas;
    @SerializedName("nama")
    private String namaLengkap;
    @SerializedName("username")
    private String username;
    @SerializedName("text_password")
    private String password;
    @SerializedName("foto")
    private String imagePetugas;

    protected Petugas(Parcel in) {
        no = in.readString();
        idPetugas = in.readString();
        namaLengkap = in.readString();
        username = in.readString();
        password = in.readString();
        imagePetugas = in.readString();
    }

    public String getImagePetugas() {
        return imagePetugas;
    }

    public void setImagePetugas(String imagePetugas) {
        this.imagePetugas = imagePetugas;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getIdPetugas() {
        return idPetugas;
    }

    public void setIdPetugas(String idPetugas) {
        this.idPetugas = idPetugas;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(no);
        dest.writeString(idPetugas);
        dest.writeString(namaLengkap);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(imagePetugas);
    }
}
