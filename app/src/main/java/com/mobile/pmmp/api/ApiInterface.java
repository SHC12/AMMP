package com.mobile.pmmp.api;

import com.mobile.pmmp.R;
import com.mobile.pmmp.model.Jadwal;
import com.mobile.pmmp.model.Laporan;
import com.mobile.pmmp.model.Petugas;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("api_android/model_login.php")
    Call<ResponseBody> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api_android/input_jadwal.php")
    Call<ResponseBody> submitJadwal(
            @Field("jenis_jadwal") String jenisJadwal,
            @Field("kode_jadwal") String kodeJadwal,
            @Field("tanggal") String tanggal,
            @Field("shift") String shift,
            @Field("nama_petugas") String namaPetugas,
            @Field("lokasi") String lokasi,
            @Field("nomor_mesin") String nomorMesin,
            @Field("permasalahan") String permasalahan
    );

    @FormUrlEncoded
    @POST("api_android/get_last_jadwal.php")
    Call<ResponseBody> lastjadwal(
            @Field("jenis_jadwal") String jenis_jadwal
    );

    @FormUrlEncoded
    @POST("api_android/get_jadwal_petugas.php")
    Call<List<Jadwal>> jadwalPetugas(
            @Field("id_petugas") String id_petugas,
            @Field("jenis_jadwal") String jenis_jadwal
    );

    @FormUrlEncoded
    @POST("api_android/get_riwayat_laporan_petugas.php")
    Call<List<Laporan>> riwayatLaporan(
            @Field("id_user") String id_petugas
    );


    @Multipart
    @POST("api_android/input_laporan_maintenance_petugas.php")
    Call<ResponseBody> submitLaporan(
            @Part("id_user") RequestBody id,
            @Part("kode") RequestBody kode,
            @Part("tanggal") RequestBody tanggal,
            @Part("penanganan") RequestBody penanganan,
            @Part("ket_tambahan") RequestBody ket_tambahan,
            @Part MultipartBody.Part laporan);

    @Multipart
    @POST("api_android/register_petugas.php")
    Call<ResponseBody> submitPetugas(
            @Part("nama_lengkap") RequestBody nama,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part foto);

    @FormUrlEncoded
    @POST("api_android/get_jadwal_admin.php")
    Call<List<Jadwal>> jadwalAdmin(
            @Field("jenis_jadwal") String jenis_jadwal
    );

    @POST("api_android/absensi_petugas.php")
    Call<List<Jadwal>> getAbsensi();


    @FormUrlEncoded
    @POST("api_android/update_password.php")
    Call<ResponseBody> updatePassword(
            @Field("id") String id,
            @Field("new_password") String newPass
    );

    @FormUrlEncoded
    @POST("api_android/delete_user.php")
    Call<ResponseBody> deleteUser(
            @Field("id_user") String id
    );

    @FormUrlEncoded
    @POST("api_android/delete_jadwal.php")
    Call<ResponseBody> deleteJadwal(
            @Field("kode_jadwal") String kode_jadwal
    );

    @POST("api_android/get_nama_petugas.php")
    Call<ResponseBody> getPetugas();

    @FormUrlEncoded
    @POST("api_android/get_konten.php")
    Call<ResponseBody> getKonten(
            @Field("jenis_konten") String konten
    );

    @POST("api_android/get_petugas.php")
    Call<List<Petugas>> getPetugasAdmin();

    @GET
    Call<ResponseBody> downloadFoto(@Url String url);

    @Multipart
    @POST("api_android/update_image.php")
    Call<ResponseBody> updateImage(
            @Part("id") RequestBody id,
            @Part("nama_lengkap") RequestBody name,
            @Part MultipartBody.Part foto);


    @Multipart
    @POST("api_android/edit_petugas_foto.php")
    Call<ResponseBody> editPetugasFoto(
            @Part("id_user") RequestBody id,
            @Part("nama_lengkap") RequestBody nama,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part MultipartBody.Part foto);

    @FormUrlEncoded
    @POST("api_android/edit_petugas.php")
    Call<ResponseBody> editPetugas(
            @Field("id_user") String id,
            @Field("nama_lengkap") String nama,
            @Field("username") String  username,
            @Field("password") String  password
    );


    @GET("api_android/export_riwayat_laporan.php")
    Call<ResponseBody> downloadLaporanPetugas(@Query("id_user") String id_user);


    @GET("api_android/export_riwayat_laporan_all.php")
    Call<ResponseBody> downloadLaporanAdmin();

    @GET("api_android/export_absen.php")
    Call<ResponseBody> downloadAbsenPetugas();

    @GET("api_android/export_data_petugas.php")
    Call<ResponseBody> downloadDataPetugas();

}
