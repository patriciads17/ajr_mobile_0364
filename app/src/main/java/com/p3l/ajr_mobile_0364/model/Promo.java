package com.p3l.ajr_mobile_0364.model;

public class Promo {
    private String kode_promo;
    private String jenis_promo;
    private String syarat_promo;
    private String besar_potongan;

    public Promo(String kode_promo, String jenis_promo, String syarat_promo, String besar_potongan) {
        this.kode_promo = kode_promo;
        this.jenis_promo = jenis_promo;
        this.syarat_promo = syarat_promo;
        this.besar_potongan = besar_potongan;
    }

    public String getKode_promo() {
        return kode_promo;
    }

    public void setKode_promo(String kode_promo) {
        this.kode_promo = kode_promo;
    }

    public String getJenis_promo() {
        return jenis_promo;
    }

    public void setJenis_promo(String jenis_promo) {
        this.jenis_promo = jenis_promo;
    }

    public String getSyarat_promo() {
        return syarat_promo;
    }

    public void setSyarat_promo(String syarat_promo) {
        this.syarat_promo = syarat_promo;
    }

    public String getBesar_potongan() {
        return besar_potongan;
    }

    public void setBesar_potongan(String besar_potongan) {
        this.besar_potongan = besar_potongan;
    }
}
