package com.p3l.ajr_mobile_0364.model;

import com.google.gson.annotations.SerializedName;

public class User {
    private String id;
    private String email;
    private String password;
    private String status_akun;

    //Customer Atribute
    private String nama_customer;
    private String alamat_customer;
    private String gender_customer;
    private String tgl_lahir_customer;
    private String no_telp_customer;
    private String url_tanda_pengenal;
    private String url_sim_customer;
    private String url_pp_customer;

    //ManagerAtribute
    private String idRole;
    private String nama_pegawai;
    private String alamat_pegawai;
    private String tgl_lahir_pegawai;
    private String gender_pegawai;
    private String no_telp_pegawai;
    private String url_foto_pegawai;

    //DriverAtribute
    private String nama_driver;
    private String alamat_driver;
    private String tgl_lahir_driver;
    private String gender_driver;
    private String no_telp_driver;
    private String rerata_rating;
    private String kemampuan_bahasa;
    private String tarif_harian_driver;
    private String status_ketersediaan_driver;
    private String url_foto_driver;
    private String url_sim_driver;
    private String url_bebas_napza;
    private String url_sehat_jiwa;
    private String url_sehat_fisik;
    private String url_skck;
    private String guard;

    public User(String guard) {
        this.guard = guard;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String id, String email, String password, String nama_customer, String alamat_customer, String gender_customer, String tgl_lahir_customer, String no_telp_customer) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nama_customer = nama_customer;
        this.alamat_customer = alamat_customer;
        this.gender_customer = gender_customer;
        this.tgl_lahir_customer = tgl_lahir_customer;
        this.no_telp_customer = no_telp_customer;
    }

    public User(String id, String email, String password, String idRole, String nama_pegawai, String alamat_pegawai, String tgl_lahir_pegawai, String gender_pegawai, String no_telp_pegawai, String url_foto_pegawai, String nama_customer, String alamat_customer, String gender_customer, String tgl_lahir_customer, String no_telp_customer) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.idRole = idRole;
        this.nama_pegawai = nama_pegawai;
        this.alamat_pegawai = alamat_pegawai;
        this.tgl_lahir_pegawai = tgl_lahir_pegawai;
        this.gender_pegawai = gender_pegawai;
        this.no_telp_pegawai = no_telp_pegawai;
        this.url_foto_pegawai = url_foto_pegawai;
        this.nama_customer = nama_customer;
        this.alamat_customer = alamat_customer;
        this.gender_customer = gender_customer;
        this.tgl_lahir_customer = tgl_lahir_customer;
        this.no_telp_customer = no_telp_customer;
    }

    public User(String id, String email, String password, String nama_driver, String alamat_driver, String tgl_lahir_driver, String gender_driver, String no_telp_driver, String rerata_rating, String kemampuan_bahasa, String tarif_harian_driver, String status_ketersediaan_driver, String status_akun, String url_foto_driver, String url_sim_driver, String url_bebas_napza, String url_sehat_jiwa, String url_sehat_fisik, String url_skck) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nama_driver = nama_driver;
        this.alamat_driver = alamat_driver;
        this.tgl_lahir_driver = tgl_lahir_driver;
        this.gender_driver = gender_driver;
        this.no_telp_driver = no_telp_driver;
        this.rerata_rating = rerata_rating;
        this.kemampuan_bahasa = kemampuan_bahasa;
        this.tarif_harian_driver = tarif_harian_driver;
        this.status_ketersediaan_driver = status_ketersediaan_driver;
        this.status_akun = status_akun;
        this.url_foto_driver = url_foto_driver;
        this.url_sim_driver = url_sim_driver;
        this.url_bebas_napza = url_bebas_napza;
        this.url_sehat_jiwa = url_sehat_jiwa;
        this.url_sehat_fisik = url_sehat_fisik;
        this.url_skck = url_skck;
    }

    public User(String id, String email, String password, String nama_driver, String alamat_driver, String tgl_lahir_driver, String gender_driver, String no_telp_driver, String kemampuan_bahasa, String tarif_harian_driver) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nama_driver = nama_driver;
        this.alamat_driver = alamat_driver;
        this.tgl_lahir_driver = tgl_lahir_driver;
        this.gender_driver = gender_driver;
        this.no_telp_driver = no_telp_driver;
        this.kemampuan_bahasa = kemampuan_bahasa;
        this.tarif_harian_driver = tarif_harian_driver;
    }

    public User(String id,String status_ketersediaan_driver,String nama_driver) {
        this.id = id;
        this.nama_driver = nama_driver;
        this.status_ketersediaan_driver = status_ketersediaan_driver;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl_tanda_pengenal() {
        return url_tanda_pengenal;
    }

    public void setUrl_tanda_pengenal(String url_tanda_pengenal) {
        this.url_tanda_pengenal = url_tanda_pengenal;
    }

    public String getUrl_sim_customer() {
        return url_sim_customer;
    }

    public void setUrl_sim_customer(String url_sim_customer) {
        this.url_sim_customer = url_sim_customer;
    }

    public String getUrl_pp_customer() {
        return url_pp_customer;
    }

    public void setUrl_pp_customer(String url_pp_customer) {
        this.url_pp_customer = url_pp_customer;
    }

    public String getStatus_akun() {
        return status_akun;
    }

    public void setStatus_akun(String status_akun) {
        this.status_akun = status_akun;
    }

    public String getIdRole() {
        return idRole;
    }

    public void setIdRole(String idRole) {
        this.idRole = idRole;
    }

    public String getNama_pegawai() {
        return nama_pegawai;
    }

    public void setNama_pegawai(String nama_pegawai) {
        this.nama_pegawai = nama_pegawai;
    }

    public String getAlamat_pegawai() {
        return alamat_pegawai;
    }

    public void setAlamat_pegawai(String alamat_pegawai) {
        this.alamat_pegawai = alamat_pegawai;
    }

    public String getTgl_lahir_pegawai() {
        return tgl_lahir_pegawai;
    }

    public void setTgl_lahir_pegawai(String tgl_lahir_pegawai) {
        this.tgl_lahir_pegawai = tgl_lahir_pegawai;
    }

    public String getGender_pegawai() {
        return gender_pegawai;
    }

    public void setGender_pegawai(String gender_pegawai) {
        this.gender_pegawai = gender_pegawai;
    }

    public String getNo_telp_pegawai() {
        return no_telp_pegawai;
    }

    public void setNo_telp_pegawai(String no_telp_pegawai) {
        this.no_telp_pegawai = no_telp_pegawai;
    }

    public String getUrl_foto_pegawai() {
        return url_foto_pegawai;
    }

    public void setUrl_foto_pegawai(String url_foto_pegawai) {
        this.url_foto_pegawai = url_foto_pegawai;
    }

    public String getNama_customer() {
        return nama_customer;
    }

    public void setNama_customer(String nama_customer) {
        this.nama_customer = nama_customer;
    }

    public String getAlamat_customer() {
        return alamat_customer;
    }

    public void setAlamat_customer(String alamat_customer) {
        this.alamat_customer = alamat_customer;
    }

    public String getGender_customer() {
        return gender_customer;
    }

    public void setGender_customer(String gender_customer) {
        this.gender_customer = gender_customer;
    }

    public String getTgl_lahir_customer() {
        return tgl_lahir_customer;
    }

    public void setTgl_lahir_customer(String tgl_lahir_customer) {
        this.tgl_lahir_customer = tgl_lahir_customer;
    }

    public String getNo_telp_customer() {
        return no_telp_customer;
    }

    public void setNo_telp_customer(String no_telp_customer) {
        this.no_telp_customer = no_telp_customer;
    }

    public String getNama_driver() {
        return nama_driver;
    }

    public void setNama_driver(String nama_driver) {
        this.nama_driver = nama_driver;
    }

    public String getAlamat_driver() {
        return alamat_driver;
    }

    public void setAlamat_driver(String alamat_driver) {
        this.alamat_driver = alamat_driver;
    }

    public String getTgl_lahir_driver() {
        return tgl_lahir_driver;
    }

    public void setTgl_lahir_driver(String tgl_lahir_driver) {
        this.tgl_lahir_driver = tgl_lahir_driver;
    }

    public String getGender_driver() {
        return gender_driver;
    }

    public void setGender_driver(String gender_driver) {
        this.gender_driver = gender_driver;
    }

    public String getNo_telp_driver() {
        return no_telp_driver;
    }

    public void setNo_telp_driver(String no_telp_driver) {
        this.no_telp_driver = no_telp_driver;
    }

    public String getRerata_rating() {
        return rerata_rating;
    }

    public void setRerata_rating(String rerata_rating) {
        this.rerata_rating = rerata_rating;
    }

    public String getKemampuan_bahasa() {
        return kemampuan_bahasa;
    }

    public void setKemampuan_bahasa(String kemampuan_bahasa) {
        this.kemampuan_bahasa = kemampuan_bahasa;
    }

    public String getTarif_harian_driver() {
        return tarif_harian_driver;
    }

    public void setTarif_harian_driver(String tarif_harian_driver) {
        this.tarif_harian_driver = tarif_harian_driver;
    }

    public String getStatus_ketersediaan_driver() {
        return status_ketersediaan_driver;
    }

    public void setStatus_ketersediaan_driver(String status_ketersediaan_driver) {
        this.status_ketersediaan_driver = status_ketersediaan_driver;
    }

    public String getUrl_foto_driver() {
        return url_foto_driver;
    }

    public void setUrl_foto_driver(String url_foto_driver) {
        this.url_foto_driver = url_foto_driver;
    }

    public String getUrl_sim_driver() {
        return url_sim_driver;
    }

    public void setUrl_sim_driver(String url_sim_driver) {
        this.url_sim_driver = url_sim_driver;
    }

    public String getUrl_bebas_napza() {
        return url_bebas_napza;
    }

    public void setUrl_bebas_napza(String url_bebas_napza) {
        this.url_bebas_napza = url_bebas_napza;
    }

    public String getUrl_sehat_jiwa() {
        return url_sehat_jiwa;
    }

    public void setUrl_sehat_jiwa(String url_sehat_jiwa) {
        this.url_sehat_jiwa = url_sehat_jiwa;
    }

    public String getUrl_sehat_fisik() {
        return url_sehat_fisik;
    }

    public void setUrl_sehat_fisik(String url_sehat_fisik) {
        this.url_sehat_fisik = url_sehat_fisik;
    }

    public String getUrl_skck() {
        return url_skck;
    }

    public void setUrl_skck(String url_skck) {
        this.url_skck = url_skck;
    }

    public String getGuard() {
        return guard;
    }

    public void setGuard(String guard) {
        this.guard = guard;
    }
}
