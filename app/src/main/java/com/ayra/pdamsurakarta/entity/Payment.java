package com.ayra.pdamsurakarta.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Payment {
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("info_struk")
    @Expose
    private String infoStruk;
    @SerializedName("saldo")
    @Expose
    private Integer saldo;
    @SerializedName("trxid")
    @Expose
    private String trxid;

    public Payment(String response, String infoStruk, Integer saldo, String trxid) {
        this.response = response;
        this.infoStruk = infoStruk;
        this.saldo = saldo;
        this.trxid = trxid;
    }

    public String getTrxid() {
        return trxid;
    }

    public void setTrxid(String trxid) {
        this.trxid = trxid;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getInfoStruk() {
        return infoStruk;
    }

    public void setInfoStruk(String infoStruk) {
        this.infoStruk = infoStruk;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

}
