package com.ayra.pdamsurakarta.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ayra.pdamsurakarta.R;
import com.ayra.pdamsurakarta.adapter.RecyclerViewAdapter;
import com.ayra.pdamsurakarta.api.MySingleton;
import com.ayra.pdamsurakarta.entity.Inquiry;
import com.ayra.pdamsurakarta.entity.Payment;
import com.ayra.pdamsurakarta.entity.Version;
import com.ayra.pdamsurakarta.manager.LibraryManager;
import com.ayra.pdamsurakarta.manager.SharedPreferencesManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InquiryDialogActivity extends AppCompatActivity {

    // UI
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.firstData)
    TextView firstData;
    @BindView(R.id.totalTagihan)
    TextView tvTotalTagihan;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.bayar)
    Button btnPay;
    @BindView(R.id.cancel)
    Button btnCancel;
    @BindView(R.id.rvData)
    RecyclerView recyclerView;
    @BindView(R.id.img)
    ImageView image;
    @BindView(R.id.scroll)
    NestedScrollView scrollView;

    // Object
    Intent intent;
    Bundle bundle;
    SpannableString colorText;
    String nometer_idpel, nama, tarifdaya, rp_admin, respdata, respid, dataInquiry, nomorregistrasi, uuid, ppid, udata;
    List<Inquiry.RespData> respdatums;
    Gson gson;
    int totalTagihan;
    LibraryManager libraryManager;
    SharedPreferencesManager sharedPreferencesManager;
    MySingleton mySingleton;
    Version version;
    Payment payment;
    JsonObject value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_dialog);

        // Init ButterKnife
        ButterKnife.bind(this);

        // Setup Toolbar
        setupToolbar();

        // Init New Object
        initNewObject();

        // Get Bundle To Inquiry
        getBundleData();

        // Listener
        listener();
    }

    // Setup Toolbar
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.inquiry));
        }
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
    }

    // Handler Toolbar Back
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    // Init New Object
    private void initNewObject() {
        bundle = getIntent().getExtras();
        libraryManager = LibraryManager.getInstance();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        mySingleton = MySingleton.getInstance(this);
        gson = new Gson();
        version = sharedPreferencesManager.loadVersion();
    }

    // Get Bundle To Inquiry
    @SuppressLint("SetTextI18n")
    private void getBundleData() {
        // Setup size
//        setDialogSize(800, 300);

        // Set the title
        title.setText(getResources().getString(R.string.app_name));

        // Set the title image
        Picasso.get()
                .load("http://103.245.181.220/sipoint/img/sipoint_icon/icon_menu_home/pdam.png")
                .placeholder(R.drawable.ic_broken)
                .resize(170, 170)
                .into(image);

        // Get bundle data
        nometer_idpel = bundle.getString("nopel");
        nama = bundle.getString("nama");
        rp_admin = bundle.getString("alamat");
        tarifdaya = bundle.getString("tarif");
        nomorregistrasi = bundle.getString("awal");
        respdata = bundle.getString("akhir");
        respid = bundle.getString("respid");

        // Store list data in transaction class
        respdatums = gson.fromJson(respdata, new TypeToken<ArrayList<Inquiry.RespData>>() {
        }.getType());

        // Init recycle view
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new RecyclerViewAdapter(this, respdatums, "pdam"));

        // Add the dataInquiry for TextView FirstData
        dataInquiry =
                "\tNo Pelanggan : " + nometer_idpel + "\n" +
                        "\tNama         : " + nama + "\n" +
                        "\tAlamat       : " + rp_admin + "\n" +
                        "\tTarif        : " + tarifdaya + "\n" +
                        "\tStan         : " + nomorregistrasi;

        // Add color for the dataInquiry
        colorText = new SpannableString(dataInquiry);
        colorText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), dataInquiry.indexOf(nometer_idpel), dataInquiry.indexOf(nometer_idpel) + nometer_idpel.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        colorText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), dataInquiry.indexOf(nama), dataInquiry.indexOf(nama) + nama.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        colorText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), dataInquiry.indexOf(rp_admin), dataInquiry.indexOf(rp_admin) + rp_admin.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        colorText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), dataInquiry.indexOf(tarifdaya), dataInquiry.indexOf(tarifdaya) + tarifdaya.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        colorText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), dataInquiry.indexOf(nomorregistrasi), dataInquiry.indexOf(nomorregistrasi) + nomorregistrasi.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the text
        firstData.setText(colorText);

        // Set total tagihan
        for (Inquiry.RespData respData : respdatums) {
            totalTagihan += respData.getTotal();
        }
        tvTotalTagihan.setText("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(totalTagihan));
    }

    // Set btn clicked
    private void listener() {
        btnPay.setOnClickListener(view -> new AlertDialog.Builder(InquiryDialogActivity.this)
                .setTitle("Pembayaran")
                .setMessage("Apakah anda yakin ingin membayar?")
                .setPositiveButton("Bayar", (dialogInterface, i) -> {
                    // Hit Api Payment
                    hitApiPayment();
                })
                .setNegativeButton("Batal", null)
                .create().show());

        btnCancel.setOnClickListener(view -> finish());
    }

    // Hit Api Payment
    private void hitApiPayment() {
        libraryManager.loadingShow(getResources().getString(R.string.loading),
                getResources().getString(R.string.payment_process), InquiryDialogActivity.this);

        // Parameter API POST
        uuid = getResources().getString(R.string.tele_android) + sharedPreferencesManager.loadUniqueDevice() + getResources().getString(R.string.indonesia);
        ppid = sharedPreferencesManager.loadUserPass();
        udata = getResources().getString(R.string.product_pdam) + "|" + nometer_idpel + "|" + respid;

        // Hit API PAYMENT
        mySingleton.connectApi(version.getUri() + getResources().getString(R.string.trx_pay), uuid, ppid, udata, response -> {
            value = gson.fromJson(response.toString(), JsonObject.class);
            if (value.get("response").toString().replace("\"", "").equals("SWT:0000")) {
                response(response.toString());
            } else {
                libraryManager.loadingDismiss();
                libraryManager.errorShow(value.get("response").toString().replace("\"", "").replace("KILLME:", ""), InquiryDialogActivity.this);
            }
        }, error -> {
            libraryManager.onError(error, InquiryDialogActivity.this);
            libraryManager.loadingDismiss();
        });
    }

    // Set Dialog Size
//    private void setDialogSize(int size1, int size2) {
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int width = displayMetrics.widthPixels;
//
//        if (width > 720) {
//            scrollView.getLayoutParams().width = size1;
//            scrollView.getLayoutParams().height = size1;
//        } else {
//            scrollView.getLayoutParams().width = size2;
//            scrollView.getLayoutParams().height = size2;
//        }
//    }

    // Response Listener To Struck
    private void response(String response) {
        // Store data to model
        Log.d(libraryManager.TAG_SUCCESS, response);
        payment = gson.fromJson(response, Payment.class);

        // Check if success or not
        if (payment.getResponse().equals("SWT:0000")) {
            // Dismiss loading and the dialog
            libraryManager.loadingDismiss();

            // Set bundle and send data to transaction activity
            intent = new Intent(this, StatusPaymentActivity.class);
            intent.putExtra("cek", "transaction");
            intent.putExtra("infoStruk", payment.getInfoStruk().replace("|", "\r\n"));
            startActivity(intent);
        } else {
            // Dismiss loading and show the error
            libraryManager.loadingDismiss();
            value = gson.fromJson(response, JsonObject.class);
            libraryManager.errorShow(value.get("response").toString().replace("\"", "").replace("KILLME:", ""), this);
        }
    }

}
