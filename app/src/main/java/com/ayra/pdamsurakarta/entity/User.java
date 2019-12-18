package com.ayra.pdamsurakarta.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("idpp")
    @Expose
    private String idpp;
    @SerializedName("namapp")
    @Expose
    private String namapp;
    @SerializedName("idsetor")
    @Expose
    private String idsetor;
    @SerializedName("saldo")
    @Expose
    private Integer saldo;
    @SerializedName("nama_printer")
    @Expose
    private String namaPrinter;
    @SerializedName("jenis_printer")
    @Expose
    private String jenisPrinter;

    public User(String response, String idpp, String namapp, String idsetor, Integer saldo, String namaPrinter, String jenisPrinter) {
        super();
        this.response = response;
        this.idpp = idpp;
        this.namapp = namapp;
        this.idsetor = idsetor;
        this.saldo = saldo;
        this.namaPrinter = namaPrinter;
        this.jenisPrinter = jenisPrinter;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getIdpp() {
        return idpp;
    }

    public void setIdpp(String idpp) {
        this.idpp = idpp;
    }

    public String getNamapp() {
        return namapp;
    }

    public void setNamapp(String namapp) {
        this.namapp = namapp;
    }

    public String getIdsetor() {
        return idsetor;
    }

    public void setIdsetor(String idsetor) {
        this.idsetor = idsetor;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public String getNamaPrinter() {
        return namaPrinter;
    }

    public void setNamaPrinter(String namaPrinter) {
        this.namaPrinter = namaPrinter;
    }

    public String getJenisPrinter() {
        return jenisPrinter;
    }

    public void setJenisPrinter(String jenisPrinter) {
        this.jenisPrinter = jenisPrinter;
    }

}
