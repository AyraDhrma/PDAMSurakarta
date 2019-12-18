package com.ayra.pdamsurakarta.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Inquiry implements Serializable {

    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("respid")
    @Expose
    private String respid;
    @SerializedName("respdata")
    @Expose
    private List<RespData> respdata = null;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("saldo")
    @Expose
    private Integer saldo;

    boolean isSelected;

    public Inquiry(String response, String respid, List<RespData> respdata, String info, Integer saldo, boolean isSelected) {
        this.response = response;
        this.respid = respid;
        this.respdata = respdata;
        this.info = info;
        this.saldo = saldo;
        this.isSelected = isSelected;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRespid() {
        return respid;
    }

    public void setRespid(String respid) {
        this.respid = respid;
    }

    public List<RespData> getRespdata() {
        return respdata;
    }

    public void setRespdata(List<RespData> respdata) {
        this.respdata = respdata;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Integer getSaldo() {
        return saldo;
    }

    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public class RespData implements Serializable {
        @SerializedName("nometer")
        @Expose
        private String nometer;
        @SerializedName("idpel")
        @Expose
        private String idpel;
        @SerializedName("nama")
        @Expose
        private String nama;
        @SerializedName("blth")
        @Expose
        private String blth;
        @SerializedName("tarif")
        @Expose
        private String tarif;
        @SerializedName("daya")
        @Expose
        private String daya;
        @SerializedName("tagihan")
        @Expose
        private Integer tagihan;
        @SerializedName("admin")
        @Expose
        private Integer admin;
        @SerializedName("total")
        @Expose
        private Integer total;
        @SerializedName("rp_admin")
        @Expose
        private String rpAdmin;
        @SerializedName("nama_transaksi")
        @Expose
        private String namaTransaksi;
        @SerializedName("nomor_registrasi")
        @Expose
        private String nomorRegistrasi;
        @SerializedName("tanggal_registrasi")
        @Expose
        private String tanggalRegistrasi;
        @SerializedName("nopel")
        @Expose
        private String nopel;
        @SerializedName("alamat")
        @Expose
        private String alamat;
        @SerializedName("stan_awal")
        @Expose
        private String stanAwal;
        @SerializedName("stan_akhir")
        @Expose
        private String stanAkhir;
        @SerializedName("denda")
        @Expose
        private String denda;
        @SerializedName("custno")
        @Expose
        private String custno;
        @SerializedName("custname")
        @Expose
        private String custname;
        @SerializedName("branchid")
        @Expose
        private String branchid;
        @SerializedName("branchname")
        @Expose
        private String branchname;
        @SerializedName("amount")
        @Expose
        private Integer amount;
        @SerializedName("periode")
        @Expose
        private String periode;
        @SerializedName("notelp")
        @Expose
        private String notelp;
        @SerializedName("nokapst")
        @Expose
        private String nokapst;
        @SerializedName("reference")
        @Expose
        private String reference;

        boolean isSelected;

        public RespData(String nometer, String idpel, String nama, String blth, String tarif, String daya, Integer tagihan, Integer admin, Integer total, String rpAdmin, String namaTransaksi, String nomorRegistrasi, String tanggalRegistrasi, String nopel, String alamat, String stanAwal, String stanAkhir, String denda, String custno, String custname, String branchid, String branchname, Integer amount, String periode, String notelp, String nokapst, String reference, boolean isSelected) {
            this.nometer = nometer;
            this.idpel = idpel;
            this.nama = nama;
            this.blth = blth;
            this.tarif = tarif;
            this.daya = daya;
            this.tagihan = tagihan;
            this.admin = admin;
            this.total = total;
            this.rpAdmin = rpAdmin;
            this.namaTransaksi = namaTransaksi;
            this.nomorRegistrasi = nomorRegistrasi;
            this.tanggalRegistrasi = tanggalRegistrasi;
            this.nopel = nopel;
            this.alamat = alamat;
            this.stanAwal = stanAwal;
            this.stanAkhir = stanAkhir;
            this.denda = denda;
            this.custno = custno;
            this.custname = custname;
            this.branchid = branchid;
            this.branchname = branchname;
            this.amount = amount;
            this.periode = periode;
            this.notelp = notelp;
            this.nokapst = nokapst;
            this.reference = reference;
            this.isSelected = isSelected;
        }

        public String getNometer() {
            return nometer;
        }

        public void setNometer(String nometer) {
            this.nometer = nometer;
        }

        public String getIdpel() {
            return idpel;
        }

        public void setIdpel(String idpel) {
            this.idpel = idpel;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getBlth() {
            return blth;
        }

        public void setBlth(String blth) {
            this.blth = blth;
        }

        public String getTarif() {
            return tarif;
        }

        public void setTarif(String tarif) {
            this.tarif = tarif;
        }

        public String getDaya() {
            return daya;
        }

        public void setDaya(String daya) {
            this.daya = daya;
        }

        public Integer getTagihan() {
            return tagihan;
        }

        public void setTagihan(Integer tagihan) {
            this.tagihan = tagihan;
        }

        public Integer getAdmin() {
            return admin;
        }

        public void setAdmin(Integer admin) {
            this.admin = admin;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public String getRpAdmin() {
            return rpAdmin;
        }

        public void setRpAdmin(String rpAdmin) {
            this.rpAdmin = rpAdmin;
        }

        public String getNamaTransaksi() {
            return namaTransaksi;
        }

        public void setNamaTransaksi(String namaTransaksi) {
            this.namaTransaksi = namaTransaksi;
        }

        public String getNomorRegistrasi() {
            return nomorRegistrasi;
        }

        public void setNomorRegistrasi(String nomorRegistrasi) {
            this.nomorRegistrasi = nomorRegistrasi;
        }

        public String getTanggalRegistrasi() {
            return tanggalRegistrasi;
        }

        public void setTanggalRegistrasi(String tanggalRegistrasi) {
            this.tanggalRegistrasi = tanggalRegistrasi;
        }

        public String getNopel() {
            return nopel;
        }

        public void setNopel(String nopel) {
            this.nopel = nopel;
        }

        public String getAlamat() {
            return alamat;
        }

        public void setAlamat(String alamat) {
            this.alamat = alamat;
        }

        public String getStanAwal() {
            return stanAwal;
        }

        public void setStanAwal(String stanAwal) {
            this.stanAwal = stanAwal;
        }

        public String getStanAkhir() {
            return stanAkhir;
        }

        public void setStanAkhir(String stanAkhir) {
            this.stanAkhir = stanAkhir;
        }

        public String getDenda() {
            return denda;
        }

        public void setDenda(String denda) {
            this.denda = denda;
        }

        public String getCustno() {
            return custno;
        }

        public void setCustno(String custno) {
            this.custno = custno;
        }

        public String getCustname() {
            return custname;
        }

        public void setCustname(String custname) {
            this.custname = custname;
        }

        public String getBranchid() {
            return branchid;
        }

        public void setBranchid(String branchid) {
            this.branchid = branchid;
        }

        public String getBranchname() {
            return branchname;
        }

        public void setBranchname(String branchname) {
            this.branchname = branchname;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public String getPeriode() {
            return periode;
        }

        public void setPeriode(String periode) {
            this.periode = periode;
        }

        public String getNotelp() {
            return notelp;
        }

        public void setNotelp(String notelp) {
            this.notelp = notelp;
        }

        public String getNokapst() {
            return nokapst;
        }

        public void setNokapst(String nokapst) {
            this.nokapst = nokapst;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
