package com.p3l.ajr_mobile_0364.model;

import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

public class Transaction{
	private String note;
	private String idCustomer;
	private String idCar;
	private String tgl_transaksi;
	private String tgl_pengembalian;
	private String url_bukti_pembayaran;
	private String id;
	private String idPromo;
	private String jenis_transaksi;
	private String tgl_mulai_sewa;
	private String status_transaksi;
	private String rating_ajr;
	private String rating_driver;
	private String tgl_selesai_sewa;
	private String komentar_ajr;
	private String idDriver;
	private String idEmployee;
	private String metode_pembayaran;
	private String komentar_driver;
	private Double total_denda;
	private Double total_pembayaran;
	private Double total_potongan_promo;
	private Double sub_total_pembayaran;

	public Transaction(String note, String idCustomer, String idCar, String tgl_transaksi, String tgl_pengembalian, String url_bukti_pembayaran, String id, String idPromo, String jenis_transaksi, String tgl_mulai_sewa, String status_transaksi, String rating_ajr, String rating_driver, String tgl_selesai_sewa, String komentar_ajr, String idDriver, String idEmployee, String metode_pembayaran, String komentar_driver, Double total_denda, Double total_pembayaran, Double total_potongan_promo, Double sub_total_pembayaran) {
		this.note = note;
		this.idCustomer = idCustomer;
		this.idCar = idCar;
		this.tgl_transaksi = tgl_transaksi;
		this.tgl_pengembalian = tgl_pengembalian;
		this.url_bukti_pembayaran = url_bukti_pembayaran;
		this.id = id;
		this.idPromo = idPromo;
		this.jenis_transaksi = jenis_transaksi;
		this.tgl_mulai_sewa = tgl_mulai_sewa;
		this.status_transaksi = status_transaksi;
		this.rating_ajr = rating_ajr;
		this.rating_driver = rating_driver;
		this.tgl_selesai_sewa = tgl_selesai_sewa;
		this.komentar_ajr = komentar_ajr;
		this.idDriver = idDriver;
		this.idEmployee = idEmployee;
		this.metode_pembayaran = metode_pembayaran;
		this.komentar_driver = komentar_driver;
		this.total_denda = total_denda;
		this.total_pembayaran = total_pembayaran;
		this.total_potongan_promo = total_potongan_promo;
		this.sub_total_pembayaran = sub_total_pembayaran;
	}

	public String getRating_ajr() {
		return rating_ajr;
	}

	public void setRating_ajr(String rating_ajr) {
		this.rating_ajr = rating_ajr;
	}

	public String getRating_driver() {
		return rating_driver;
	}

	public void setRating_driver(String rating_driver) {
		this.rating_driver = rating_driver;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Double getTotal_pembayaran() {
		return total_pembayaran;
	}

	public void setTotal_pembayaran(Double total_pembayaran) {
		this.total_pembayaran = total_pembayaran;
	}

	public String getIdCustomer() {
		return idCustomer;
	}

	public void setIdCustomer(String idCustomer) {
		this.idCustomer = idCustomer;
	}

	public String getIdCar() {
		return idCar;
	}

	public void setIdCar(String idCar) {
		this.idCar = idCar;
	}

	public String getTgl_transaksi() {
		return tgl_transaksi;
	}

	public void setTgl_transaksi(String tgl_transaksi) {
		this.tgl_transaksi = tgl_transaksi;
	}

	public String getTgl_pengembalian() {
		return tgl_pengembalian;
	}

	public void setTgl_pengembalian(String tgl_pengembalian) {
		this.tgl_pengembalian = tgl_pengembalian;
	}

	public String getUrl_bukti_pembayaran() {
		return url_bukti_pembayaran;
	}

	public void setUrl_bukti_pembayaran(String url_bukti_pembayaran) {
		this.url_bukti_pembayaran = url_bukti_pembayaran;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdPromo() {
		return idPromo;
	}

	public void setIdPromo(String idPromo) {
		this.idPromo = idPromo;
	}

	public String getJenis_transaksi() {
		return jenis_transaksi;
	}

	public void setJenis_transaksi(String jenis_transaksi) {
		this.jenis_transaksi = jenis_transaksi;
	}

	public String getTgl_mulai_sewa() {
		return tgl_mulai_sewa;
	}

	public void setTgl_mulai_sewa(String tgl_mulai_sewa) {
		this.tgl_mulai_sewa = tgl_mulai_sewa;
	}

	public String getStatus_transaksi() {
		return status_transaksi;
	}

	public void setStatus_transaksi(String status_transaksi) {
		this.status_transaksi = status_transaksi;
	}

	public String getTgl_selesai_sewa() {
		return tgl_selesai_sewa;
	}

	public void setTgl_selesai_sewa(String tgl_selesai_sewa) {
		this.tgl_selesai_sewa = tgl_selesai_sewa;
	}

	public String getKomentar_ajr() {
		return komentar_ajr;
	}

	public void setKomentar_ajr(String komentar_ajr) {
		this.komentar_ajr = komentar_ajr;
	}

	public String getIdDriver() {
		return idDriver;
	}

	public void setIdDriver(String idDriver) {
		this.idDriver = idDriver;
	}

	public String getIdEmployee() {
		return idEmployee;
	}

	public void setIdEmployee(String idEmployee) {
		this.idEmployee = idEmployee;
	}

	public String getMetode_pembayaran() {
		return metode_pembayaran;
	}

	public void setMetode_pembayaran(String metode_pembayaran) {
		this.metode_pembayaran = metode_pembayaran;
	}

	public Double getTotal_denda() {
		return total_denda;
	}

	public void setTotal_denda(Double total_denda) {
		this.total_denda = total_denda;
	}

	public String getKomentar_driver() {
		return komentar_driver;
	}

	public void setKomentar_driver(String komentar_driver) {
		this.komentar_driver = komentar_driver;
	}

	public Double getTotal_potongan_promo() {
		return total_potongan_promo;
	}

	public void setTotal_potongan_promo(Double total_potongan_promo) {
		this.total_potongan_promo = total_potongan_promo;
	}

	public Double getSub_total_pembayaran() {
		return sub_total_pembayaran;
	}

	public void setSub_total_pembayaran(Double sub_total_pembayaran) {
		this.sub_total_pembayaran = sub_total_pembayaran;
	}
}