package com.ayra.pdamsurakarta.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListPDAM {
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("datas")
    @Expose
    private List<Data> datas = null;
    @SerializedName("saldo")
    @Expose
    private Integer saldo;

    public ListPDAM(String response, List<Data> datas, Integer saldo) {
        this.response = response;
        this.datas = datas;
        this.saldo = saldo;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<Data> getDatas() {
        return datas;
    }

    public void setDatas(List<Data> datas) {
        this.datas = datas;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public class Data {
        @SerializedName("kode")
        @Expose
        private String kode;
        @SerializedName("nama")
        @Expose
        private String nama;

        public Data(String kode, String nama) {
            this.kode = kode;
            this.nama = nama;
        }

        public String getKode() {
            return kode;
        }

        public void setKode(String kode) {
            this.kode = kode;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }
    }
}
