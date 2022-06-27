package com.p3l.ajr_mobile_0364.model;

public class Report {
    private String tipe_mobil;
    private String nama_mobil;
    private String jumlah_peminjaman;
    private String jumlah_pendapatan;
    private String nama_customer;
    private String jenis_transaksi;
    private String jumlah_transaksi;
    private String id;
    private String nama_driver;
    private String rerata_rating;

    public Report(String tipe_mobil, String nama_mobil, String jumlah_peminjaman, String jumlah_pendapatan, String nama_customer, String jenis_transaksi, String jumlah_transaksi, String id, String nama_driver, String rerata_rating) {
        this.tipe_mobil = tipe_mobil;
        this.nama_mobil = nama_mobil;
        this.jumlah_peminjaman = jumlah_peminjaman;
        this.jumlah_pendapatan = jumlah_pendapatan;
        this.nama_customer = nama_customer;
        this.jenis_transaksi = jenis_transaksi;
        this.jumlah_transaksi = jumlah_transaksi;
        this.id = id;
        this.nama_driver = nama_driver;
        this.rerata_rating = rerata_rating;
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

    public String getJumlah_peminjaman() {
        return jumlah_peminjaman;
    }

    public void setJumlah_peminjaman(String jumlah_peminjaman) {
        this.jumlah_peminjaman = jumlah_peminjaman;
    }

    public String getJumlah_pendapatan() {
        return jumlah_pendapatan;
    }

    public void setJumlah_pendapatan(String jumlah_pendapatan) {
        this.jumlah_pendapatan = jumlah_pendapatan;
    }

    public String getNama_customer() {
        return nama_customer;
    }

    public void setNama_customer(String nama_customer) {
        this.nama_customer = nama_customer;
    }

    public String getJenis_transaksi() {
        return jenis_transaksi;
    }

    public void setJenis_transaksi(String jenis_transaksi) {
        this.jenis_transaksi = jenis_transaksi;
    }

    public String getJumlah_transaksi() {
        return jumlah_transaksi;
    }

    public void setJumlah_transaksi(String jumlah_transaksi) {
        this.jumlah_transaksi = jumlah_transaksi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama_driver() {
        return nama_driver;
    }

    public void setNama_driver(String nama_driver) {
        this.nama_driver = nama_driver;
    }

    public String getRerata_rating() {
        return rerata_rating;
    }

    public void setRerata_rating(String rerata_rating) {
        this.rerata_rating = rerata_rating;
    }
}
