package com.ayra.pdamsurakarta.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Menu {
    @SerializedName("response")
    @Expose
    private String response;
    @SerializedName("datas")
    @Expose
    private List<Data> datas = null;
    @SerializedName("saldo")
    @Expose
    private int saldo;

    public Menu(String response, List<Data> datas, int saldo) {
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

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public class Data {
        @SerializedName("id_loket")
        @Expose
        private String idLoket;
        @SerializedName("nama_loket")
        @Expose
        private String namaLoket;
        @SerializedName("new_message")
        @Expose
        private String newMessage;
        @SerializedName("menu")
        @Expose
        private List<Icon> icon = null;
        @SerializedName("slider")
        @Expose
        private List<String> slider = null;

        public Data(String idLoket, String namaLoket, String newMessage, List<Icon> icon, List<String> slider) {
            this.idLoket = idLoket;
            this.namaLoket = namaLoket;
            this.newMessage = newMessage;
            this.icon = icon;
            this.slider = slider;
        }

        public String getIdLoket() {
            return idLoket;
        }

        public void setIdLoket(String idLoket) {
            this.idLoket = idLoket;
        }

        public String getNamaLoket() {
            return namaLoket;
        }

        public void setNamaLoket(String namaLoket) {
            this.namaLoket = namaLoket;
        }

        public String getNewMessage() {
            return newMessage;
        }

        public void setNewMessage(String newMessage) {
            this.newMessage = newMessage;
        }

        public List<Icon> getIcon() {
            return icon;
        }

        public void setIcon(List<Icon> icon) {
            this.icon = icon;
        }

        public List<String> getSlider() {
            return slider;
        }

        public void setSlider(List<String> slider) {
            this.slider = slider;
        }
    }

    public class Icon {
        @SerializedName("img")
        @Expose
        private String img;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("status")
        @Expose
        private String status;

        public Icon(String img, String title, String status) {
            this.img = img;
            this.title = title;
            this.status = status;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

}
