package com.ayra.pdamsurakarta.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ayra.pdamsurakarta.R;
import com.ayra.pdamsurakarta.api.MySingleton;
import com.ayra.pdamsurakarta.entity.Inquiry;
import com.ayra.pdamsurakarta.entity.Version;
import com.ayra.pdamsurakarta.manager.LibraryManager;
import com.ayra.pdamsurakarta.manager.SharedPreferencesManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PaymentActivity extends AppCompatActivity {

    // UI
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_product)
    MaterialEditText etProduct;
    @BindView(R.id.et_no_meter)
    MaterialEditText etNoMeter;
    @BindView(R.id.btn_check)
    Button btnCheck;

    // Object
    Gson gson;
    LibraryManager libraryManager;
    MySingleton mySingleton;
    SharedPreferencesManager preferencesManager;
    String uuid, ppid, udata;
    Version version;
    String idProduct;
    Inquiry inquiry;
    JsonObject value;
    List<Inquiry.RespData> respDataInquiry;
    Bundle bundle;
    String product = "PDAM SURAKARTA";
    private String TAG = PaymentActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Init UI ButterKnife
        ButterKnife.bind(this);

        // Init Object
        initObject();

        // Set
        etProduct.setText(product);
        idProduct = "TELE-PDAM-SURAKARTA_V2";

        // Setup Toolbar
        setupToolbar();

        // Listener
        listener();

    }

    // Init Object
    private void initObject() {
        preferencesManager = SharedPreferencesManager.getInstance(this);
        libraryManager = LibraryManager.getInstance();
        mySingleton = MySingleton.getInstance(this);
        version = preferencesManager.loadVersion();
        gson = new Gson();
    }

    // Listener
    private void listener() {
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNoMeter.getText().toString().equals("")) {
                    etNoMeter.setError(getResources().getString(R.string.error_pelanggan));
                } else {
                    hitApiInquiry(idProduct, product, "", new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            Log.d(TAG, "onResponse: " + response);
                            response(response.toString(), "pdam");
                        }
                    });
                }
            }
        });
    }

    // Hit API Inquiry
    private void hitApiInquiry(String idProduct, String product, String periode, Response.Listener listener) {

        // Show Loading
        libraryManager.loadingShow(getResources().getString(R.string.loading), product, this);

        // Setup Parameter
        uuid = getResources().getString(R.string.tele_android) + preferencesManager.loadUniqueDevice() + getResources().getString(R.string.indonesia);
        ppid = preferencesManager.loadUserPass();
        udata = idProduct + "|" + etNoMeter.getText().toString() + periode;

        // Hit API INQUIRY
        mySingleton.connectApi(version.getUri() + getResources().getString(R.string.trx_inquiry), uuid, ppid, udata, listener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                libraryManager.onError(error, PaymentActivity.this);
                libraryManager.loadingDismiss();
            }
        });

    }

    // Response Inquiry
    private void response(String response, String product) {
        // Store data to model
        inquiry = gson.fromJson(response, Inquiry.class);
        Log.d(libraryManager.TAG_SUCCESS, response);

        // Check if success or not
        if (inquiry.getResponse().equals("SWT:0000")) {
            // Update Saldo
            preferencesManager.updateSaldo(inquiry.getSaldo());
            // Store data to model list
            respDataInquiry = gson.fromJson(gson.toJson(inquiry.getRespdata()), new TypeToken<ArrayList<Inquiry.RespData>>() {
            }.getType());
            if (respDataInquiry == null) {
                respDataInquiry = new ArrayList<>();
            }

            setToInquiryDialog(product, idProduct, respDataInquiry.get(0).getNopel(), respDataInquiry.get(0).getNama(), respDataInquiry.get(0).getAlamat(), respDataInquiry.get(0).getTarif(), respDataInquiry.get(0).getStanAwal() + "-" + respDataInquiry.get(0).getStanAkhir(), gson.toJson(inquiry.getRespdata()), 0, inquiry.getRespid(), respDataInquiry.get(0).getDenda());
            // Dismiss loading
            libraryManager.loadingDismiss();
        } else {
            // Dismiss loading and show the error
            libraryManager.loadingDismiss();
            value = gson.fromJson(response, JsonObject.class);
            libraryManager.errorShow(value.get("response").toString().replace("\"", "").replace("KILLME:", "") + "\n", PaymentActivity.this);
        }
    }

    // Set To Bundle
    private void setToInquiryDialog(String pdam, String idProduct, String nopel, String nama, String alamat, String tarif, String awal, String akhir, int respdata, String respid, String denda) {
        bundle = new Bundle();
        bundle.putString("dialog", pdam);
        bundle.putString("idProduct", idProduct);
        bundle.putString("nopel", nopel);
        bundle.putString("nama", nama);
        bundle.putString("alamat", alamat);
        bundle.putString("tarif", tarif);
        bundle.putString("awal", awal);
        bundle.putString("akhir", akhir);
        bundle.putInt("respdata", respdata);
        bundle.putString("respid", respid);
        bundle.putString("denda", denda);

        Intent intent = new Intent(this, InquiryDialogActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // Setup Toolbar
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.payment));
        }
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
    }

    // Handler Toolbar Back Button
    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return false;
    }

}
