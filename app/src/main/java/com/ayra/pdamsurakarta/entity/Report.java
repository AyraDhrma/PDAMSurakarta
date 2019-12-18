package com.ayra.pdamsurakarta.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Report {
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    public Report(String response, List<Data> data) {
        this.response = response;
        this.data = data;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {
        @SerializedName("userid")
        @Expose
        private String userid;
        @SerializedName("produk")
        @Expose
        private String produk;
        @SerializedName("kode_produk")
        @Expose
        private String kode_produk;
        @SerializedName("jml")
        @Expose
        private String jml;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("adminfee")
        @Expose
        private String adminfee;
        @SerializedName("denda")
        @Expose
        private String denda;
        @SerializedName("total")
        @Expose
        private String total;

        public Data(String userid, String produk, String kode_produk, String jml, String amount, String adminfee, String denda, String total) {
            this.userid = userid;
            this.produk = produk;
            this.kode_produk = kode_produk;
            this.jml = jml;
            this.amount = amount;
            this.adminfee = adminfee;
            this.denda = denda;
            this.total = total;

        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getProduk() {
            return produk;
        }

        public void setProduk(String produk) {
            this.produk = produk;
        }

        public String getKode_produk() {
            return kode_produk;
        }

        public void setKode_produk(String kode_produk) {
            this.kode_produk = kode_produk;
        }

        public String getJml() {
            return jml;
        }

        public void setJml(String jml) {
            this.jml = jml;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAdminfee() {
            return adminfee;
        }

        public void setAdminfee(String adminfee) {
            this.adminfee = adminfee;
        }

        public String getDenda() {
            return denda;
        }

        public void setDenda(String denda) {
            this.denda = denda;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }
    }
}
