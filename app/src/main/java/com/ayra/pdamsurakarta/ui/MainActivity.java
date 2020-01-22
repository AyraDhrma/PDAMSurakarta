package com.ayra.pdamsurakarta.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ayra.pdamsurakarta.R;
import com.ayra.pdamsurakarta.api.MySingleton;
import com.ayra.pdamsurakarta.entity.Version;
import com.ayra.pdamsurakarta.manager.LibraryManager;
import com.ayra.pdamsurakarta.manager.SharedPreferencesManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    // UI
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_payment)
    Button btnPayment;
    @BindView(R.id.btn_report)
    Button btnReport;
    @BindView(R.id.btn_setting)
    Button btnSettings;
    @BindView(R.id.tv_saldo)
    TextView tvSaldo;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.swipe_refresh_main)
    SwipeRefreshLayout swipeRefreshLayout;
    //    @BindView(R.id.shimmer_container)
    //    ShimmerFrameLayout shimmerFrameLayout;

    // Object
    Intent intent;
    SharedPreferencesManager preferencesManager;
    Version version;
    Bundle bundle;
    MySingleton mySingleton;
    String uuid, ppid, udata;
    Gson gson;
    LibraryManager libraryManager;
    com.ayra.pdamsurakarta.entity.Menu menu;
    List<com.ayra.pdamsurakarta.entity.Menu.Data> dataList;
    Boolean backPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ButterKnife Init
        ButterKnife.bind(this);

        // Shimmer
//        shimmerFrameLayout.startShimmer();

        // Setup Toolbar
        setupToolbar();

        // Init New Object
        initNewObject();

        // Load Version And Set Saldo
        //        loadVersion();
        setSaldo();

        // Listener
        listener();
    }

    // Hit API For Saldo And Username
    @SuppressLint("StaticFieldLeak")
    private void setSaldo() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                uuid = getResources().getString(R.string.tele_android) + preferencesManager.loadUniqueDevice() + getResources().getString(R.string.indonesia);
                ppid = preferencesManager.loadUserPass();
                udata = "";

                mySingleton.connectApi(version.getUri() + getResources().getString(R.string.content_menu), uuid, ppid, udata, new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        menu = gson.fromJson(response.toString(), com.ayra.pdamsurakarta.entity.Menu.class);

                        if (menu.getResponse().equals("SWT:0000")) {
                            preferencesManager.updateSaldo(menu.getSaldo());

                            dataList = gson.fromJson(gson.toJson(menu.getDatas()),
                                    new TypeToken<ArrayList<com.ayra.pdamsurakarta.entity.Menu.Data>>() {
                                    }.getType());

//                    shimmerFrameLayout.stopShimmer();
//                    shimmerFrameLayout.setVisibility(View.GONE);
                            tvUsername.setText(dataList.get(0).getNamaLoket());
                            tvId.setText("Id Loket : " + dataList.get(0).getIdLoket());
                            tvSaldo.setText("Rp " + NumberFormat.getNumberInstance(new Locale("in", "ID")).format(Integer.valueOf(menu.getSaldo())));
                        } else {
                            libraryManager.errorShow(menu.getResponse().replace("KILLME:", ""), MainActivity.this);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        libraryManager.onError(error, MainActivity.this);
                    }
                });
                return null;
            }
        }.execute();
    }

    // Init New Object
    private void initNewObject() {
        preferencesManager = SharedPreferencesManager.getInstance(this);
        mySingleton = MySingleton.getInstance(this);
        version = preferencesManager.loadVersion();
        libraryManager = LibraryManager.getInstance();
        bundle = new Bundle();
        gson = new Gson();
    }

    // Listener
    private void listener() {
        btnPayment.setOnClickListener(view -> {
            intent = new Intent(MainActivity.this, PaymentActivity.class);
            startActivity(intent);
        });

        btnReport.setOnClickListener(view -> {
            intent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(view -> {
            intent = new Intent(MainActivity.this, CetakUlangActivity.class);
            startActivity(intent);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> new Handler().postDelayed(() -> {
            swipeRefreshLayout.setRefreshing(false);
            Intent intent = MainActivity.this.getIntent();
            startActivity(intent);
            MainActivity.this.finish();
        }, 1000));

        int color1 = getResources().getColor(R.color.colorPrimaryDark);
        int color2 = getResources().getColor(R.color.colorPrimary);
        int color3 = getResources().getColor(R.color.colorPrimaryDark);
        swipeRefreshLayout.setColorSchemeColors(color1, color2, color3);
    }

    // Inflate Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    // Option Menu Selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Logout")
                    .setMessage("Anda yakin ingin logout?")
                    .setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            preferencesManager.logout();
                            finish();
                        }
                    })
                    .setNegativeButton("Batal", null)
                    .create().show();

        } else if (item.getItemId() == R.id.menu_settings) {
            intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    // Setup Toolbar
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setSubtitleTextColor(Color.WHITE);
    }

    // Double Back Pressed Button
    @Override
    public void onBackPressed() {
        if (backPressedOnce) {
            finish();
            return;
        }
        backPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.double_back_info), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> backPressedOnce = false, 2000);
    }

}
