package com.ayra.pdamsurakarta.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ayra.pdamsurakarta.R;
import com.ayra.pdamsurakarta.adapter.ListViewAdapter;
import com.ayra.pdamsurakarta.api.MySingleton;
import com.ayra.pdamsurakarta.entity.Report;
import com.ayra.pdamsurakarta.entity.Version;
import com.ayra.pdamsurakarta.manager.LibraryManager;
import com.ayra.pdamsurakarta.manager.SharedPreferencesManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportActivity extends AppCompatActivity {

    // UI
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_from)
    MaterialEditText etFrom;
    @BindView(R.id.et_until)
    MaterialEditText etUntil;
    @BindView(R.id.lv_report)
    ListView lvReport;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btn_check)
    Button btnCheck;
    @BindView(R.id.data_not_found_tv)
    TextView tvNotFound;

    // Object
    Calendar calendar;
    String uuid, ppid, udata;
    MySingleton mySingleton;
    LibraryManager libraryManager;
    SharedPreferencesManager sharedPreferencesManager;
    Gson gson;
    Version version;
    JsonObject value;
    Report report;
    List<Report.Data> list;
    ArrayList<Object> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Init UI ButterKnife
        ButterKnife.bind(this);

        // Setup View
        setupView();

        // Init New Object
        initNewObject();

        // Setup Toolbar
        setupToolbar();

        // Listener
        listener();
    }

    // Listener
    private void listener() {
        etFrom.setOnClickListener(view -> calendarClick(etFrom));

        etUntil.setOnClickListener(view -> calendarClick(etUntil));

        btnCheck.setOnClickListener(view -> {
            if (validationInput()) {
                tvNotFound.setVisibility(View.GONE);
                hitApiReport();
            }
        });
    }

    // Validation Input
    private boolean validationInput() {
        if (etFrom.getText().toString().equals("")) {
            etFrom.setError(getResources().getString(R.string.error_input));
            return false;
        } else if (etUntil.getText().toString().equals("")) {
            etUntil.setError(getResources().getString(R.string.error_input));
            return false;
        }
        return true;
    }

    // Hit API Report
    private void hitApiReport() {
        String fromDate = etFrom.getText().toString();
        String untilDate = etUntil.getText().toString();

        progressBar.setVisibility(View.VISIBLE);

        uuid = getResources().getString(R.string.tele_android) + sharedPreferencesManager.loadUniqueDevice() + getResources().getString(R.string.indonesia);
        ppid = sharedPreferencesManager.loadUserPass();
        udata = fromDate + "|" + untilDate;

        mySingleton.connectApi(version.getUri() + getResources().getString(R.string.content_rekap_peruser), uuid, ppid, udata, response -> {
            arrayList = new ArrayList<>();
            report = gson.fromJson(response.toString(), Report.class);
            if (report.getResponse().equals("SWT:0000")) {
                list = gson.fromJson(gson.toJson(report.getData()), new TypeToken<ArrayList<Report.Data>>() {
                }.getType());
                if (report.getData() != null) {
                    for (Report.Data data : list) {
                        arrayList.add(data.getUserid() + "|" + data.getJml() + "|" + data.getProduk() + "|" + data.getTotal() + "|" + data.getKode_produk());
                    }
                    if (arrayList.isEmpty()) {
                        tvNotFound.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        etUntil.setVisibility(View.GONE);
                        etFrom.setVisibility(View.GONE);
                        btnCheck.setVisibility(View.GONE);
                        lvReport.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        lvReport.setAdapter(new ListViewAdapter(ReportActivity.this, arrayList));

                        // List View Click Listener
                        listViewListener(fromDate, untilDate);
                    }
                }
            } else {
                value = gson.fromJson(response.toString(), JsonObject.class);
                libraryManager.errorShow(value.get("response").toString().replace("\"", "").replace("KILLME:", ""), ReportActivity.this);
                progressBar.setVisibility(View.GONE);
            }
        }, error -> {
            libraryManager.onError(error, ReportActivity.this);
            progressBar.setVisibility(View.GONE);
        });
    }

    // List View Click Listener
    private void listViewListener(String fromDate, String untilDate) {
        lvReport.setOnItemClickListener((adapterView, view, i, l) -> {
            String[] split = arrayList.get(i).toString().split("\\|");
            if (!split[2].equals("SUBTOTAL")) {
                libraryManager.loadingShow(getResources().getString(R.string.loading), "", ReportActivity.this);
                uuid = "tele-android-" + sharedPreferencesManager.loadUniqueDevice() + "-indonesia";
                ppid = sharedPreferencesManager.loadUserPass();
                udata = split[4] + "|" + fromDate + "|" + untilDate + "|" + split[0].replace("TELE-", "");

                mySingleton.connectApi(version.getUri() + "content/report_detil", uuid, ppid, udata, response1 -> {
                    value = gson.fromJson(response1.toString(), JsonObject.class);
                    if (value.get("response").toString().replace("\"", "").equals("SWT:0000")) {
                        Intent intent = new Intent(ReportActivity.this, StatusPaymentActivity.class);
                        intent.putExtra("cek", "laporan");
                        intent.putExtra("infoStruk", value.get("info").toString().replace("\"", "").replace("|", "\r\n"));
                        startActivity(intent);
                        libraryManager.loadingDismiss();
                    } else {
                        libraryManager.loadingDismiss();
                        libraryManager.errorShow(value.get("response").toString().replace("\"", "").replace("KILLME:", ""), ReportActivity.this);
                    }
                }, error -> {
                    libraryManager.loadingDismiss();
                    libraryManager.onError(error, ReportActivity.this);
                });
            }
        });
    }

    // Calendar Click
    private void calendarClick(MaterialEditText materialEditText) {
        calendar = Calendar.getInstance(TimeZone.getDefault());
        new DatePickerDialog(ReportActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                materialEditText.setText(dateFormat.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Setup View
    private void setupView() {
        lvReport.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        tvNotFound.setVisibility(View.GONE);
    }

    // Init New Object
    private void initNewObject() {
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        mySingleton = MySingleton.getInstance(this);
        libraryManager = LibraryManager.getInstance();
        gson = new Gson();
        version = sharedPreferencesManager.loadVersion();
    }

    // Setup Toolbar
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.report));
        }
        toolbar.setTitleTextColor(Color.WHITE);
    }

    // Handler Toolbar Back Arrow
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

}
