package com.ayra.pdamsurakarta.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ayra.pdamsurakarta.R;
import com.ayra.pdamsurakarta.api.MySingleton;
import com.ayra.pdamsurakarta.entity.Payment;
import com.ayra.pdamsurakarta.entity.Version;
import com.ayra.pdamsurakarta.manager.LibraryManager;
import com.ayra.pdamsurakarta.manager.SharedPreferencesManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CetakUlangActivity extends AppCompatActivity {

    // UI
    @BindView(R.id.noMeter)
    MaterialEditText noMeter;
    @BindView(R.id.tanggal)
    MaterialEditText tanggal;
    @BindView(R.id.button_print)
    Button btnPrint;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // Object
    Calendar calendar;
    String uuid, ppid, udata, productCode, inputNumber, inputDate;
    MySingleton mySingleton;
    LibraryManager libraryManager;
    SharedPreferencesManager sharedPreferencesManager;
    Gson gson;
    Version version;
    Payment payment;
    Intent intent;
    JsonObject value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetak_ulang);

        // Init UI ButterKnife
        ButterKnife.bind(this);

        // Setup Toolbar
        setupToolbar();

        // Init New Object
        initNewObject();

        // Listener
        listener();
    }

    // Listener
    private void listener() {
        btnPrint.setOnClickListener(view -> {
            if (validationInput()) {
                hitCheckApi();
            }
        });

        tanggal.setOnClickListener(view -> {
            calendar = Calendar.getInstance(TimeZone.getDefault());
            new DatePickerDialog(CetakUlangActivity.this, (datePicker, year, monthOfYear, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

                // Set the text in tanggal edit text
                tanggal.setText(dateFormat.format(calendar.getTime()));

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    // Hit Check API
    private void hitCheckApi() {
        productCode = getResources().getString(R.string.product_pdam);
        inputNumber = Objects.requireNonNull(noMeter.getText()).toString();
        inputDate = Objects.requireNonNull(tanggal.getText()).toString();
        libraryManager.loadingShow("Loading...", "Cek Status", CetakUlangActivity.this);
        uuid = getResources().getString(R.string.tele_android) + sharedPreferencesManager.loadUniqueDevice() + getResources().getString(R.string.indonesia);
        ppid = sharedPreferencesManager.loadUserPass();
        udata = productCode + "|" + inputNumber + "|" + inputDate;

        mySingleton.connectApi(version.getUri() + getResources().getString(R.string.check_status), uuid, ppid, udata, response -> {
            Log.d(libraryManager.TAG_SUCCESS, response.toString());
            payment = gson.fromJson(response.toString(), Payment.class);
            if (payment.getResponse().equals("SWT:0000")) {
                // Set bundle and send data to transaction activity
                intent = new Intent(CetakUlangActivity.this, StatusPaymentActivity.class);
                intent.putExtra("cek", "cekstatus");
                intent.putExtra("infoStruk", payment.getInfoStruk().replace("|", "\r\n"));
                startActivity(intent);
            } else {
                value = gson.fromJson(response.toString(), JsonObject.class);
                libraryManager.errorShow(value.get("response").toString().replace("\"", "").replace("KILLME:", ""), CetakUlangActivity.this);
            }
            libraryManager.loadingDismiss();
        }, error -> {
            libraryManager.onError(error, CetakUlangActivity.this);
            libraryManager.loadingDismiss();
        });
    }

    // Setup Toolbar
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.reprint));
        }
        toolbar.setTitleTextColor(Color.WHITE);
    }

    // Handler Arrow Back
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    // Init New Object
    private void initNewObject() {
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        mySingleton = MySingleton.getInstance(this);
        libraryManager = LibraryManager.getInstance();
        gson = new Gson();
        version = sharedPreferencesManager.loadVersion();
    }

    // Validation Input
    private boolean validationInput() {
        if (noMeter.getText().toString().equals("")) {
            noMeter.setError(getResources().getString(R.string.error_input));
            return false;
        } else if (tanggal.getText().toString().equals("")) {
            tanggal.setError(getResources().getString(R.string.error_input));
            return false;
        }
        return true;
    }

}
