package com.p3l.ajr_mobile_0364.model;

public class Car {
    private String id;
    private String no_plat;
    private String tipe_mobil;
    private String nama_mobil;
    private String jenis_transmisi;
    private String jenis_bahan_bakar;
    private String warna_mobil;
    private String vol_bagasi;
    private String fasilitas_mobil;
    private String url_car_img;
    private Double tarif_harian_mobil;

    public Car(String id, String no_plat, String tipe_mobil, String nama_mobil, String jenis_transmisi, String jenis_bahan_bakar, String warna_mobil, String vol_bagasi, String fasilitas_mobil, String url_car_img, Double tarif_harian_mobil) {
        this.id = id;
        this.no_plat = no_plat;
        this.tipe_mobil = tipe_mobil;
        this.nama_mobil = nama_mobil;
        this.jenis_transmisi = jenis_transmisi;
        this.jenis_bahan_bakar = jenis_bahan_bakar;
        this.warna_mobil = warna_mobil;
        this.vol_bagasi = vol_bagasi;
        this.fasilitas_mobil = fasilitas_mobil;
        this.url_car_img = url_car_img;
        this.tarif_harian_mobil = tarif_harian_mobil;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo_plat() {
        return no_plat;
    }

    public void setNo_plat(String no_plat) {
        this.no_plat = no_plat;
    }

    public String getTipe_mobil() {
        return tipe_mobil;
    }

    public void setTipe_mobil(String tipe_mobil) {
        this.tipe_mobil = tipe_mobil;
    }

    public String getNama_mobil() {
        return nama_mobil;
    }

    public void setNama_mobil(String nama_mobil) {
        this.nama_mobil = nama_mobil;
    }

    public String getJenis_transmisi() {
        return jenis_transmisi;
    }

    public void setJenis_transmisi(String jenis_transmisi) {
        this.jenis_transmisi = jenis_transmisi;
    }

    public String getJenis_bahan_bakar() {
        return jenis_bahan_bakar;
    }

    public void setJenis_bahan_bakar(String jenis_bahan_bakar) {
        this.jenis_bahan_bakar = jenis_bahan_bakar;
    }

    public String getWarna_mobil() {
        return warna_mobil;
    }

    public void setWarna_mobil(String warna_mobil) {
        this.warna_mobil = warna_mobil;
    }

    public String getVol_bagasi() {
        return vol_bagasi;
    }

    public void setVol_bagasi(String vol_bagasi) {
        this.vol_bagasi = vol_bagasi;
    }

    public String getFasilitas_mobil() {
        return fasilitas_mobil;
    }

    public void setFasilitas_mobil(String fasilitas_mobil) {
        this.fasilitas_mobil = fasilitas_mobil;
    }

    public String getUrl_car_img() {
        return url_car_img;
    }

    public void setUrl_car_img(String url_car_img) {
        this.url_car_img = url_car_img;
    }

    public Double getTarif_harian_mobil() {
        return tarif_harian_mobil;
    }

    public void setTarif_harian_mobil(Double tarif_harian_mobil) {
        this.tarif_harian_mobil = tarif_harian_mobil;
    }
}
