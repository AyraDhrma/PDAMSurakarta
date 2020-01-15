package com.ayra.pdamsurakarta.ui;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ayra.pdamsurakarta.R;
import com.ayra.pdamsurakarta.api.MySingleton;
import com.ayra.pdamsurakarta.entity.Version;
import com.ayra.pdamsurakarta.manager.LibraryManager;
import com.ayra.pdamsurakarta.manager.SharedPreferencesManager;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {

    // UI
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    // Object
    Gson gson;
    SharedPreferencesManager sharedPreferencesManager;
    LibraryManager libraryManager;
    MySingleton mySingleton;
    Version version;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // ButterKnife
        ButterKnife.bind(this);

        // Init Object
        initObject();

        // Setup Progress Dialog
        setupProgressBar();

        // Handler Splash Screen
        splashScreeHandler();

    }

    // Init Object
    private void initObject() {
        gson = new Gson();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(SplashScreen.this);
        libraryManager = LibraryManager.getInstance();
        mySingleton = MySingleton.getInstance(getApplicationContext());
    }

    // Handler SplashScreen
    private void splashScreeHandler() {
        mySingleton.version(response -> {
            version = gson.fromJson(response.toString(), Version.class);
            if (version.getVersion() != null) {
                progressBar.setVisibility(View.GONE);
                processSplash();
            } else {
                new AlertDialog.Builder(SplashScreen.this)
                        .setTitle(getResources().getString(R.string.no_internet_connection))
                        .setMessage(getResources().getString(R.string.refresh_info))
                        .setNeutralButton(getResources().getString(R.string.refresh), (dialogInterface, i) -> {
                            progressBar.setVisibility(View.VISIBLE);
                            intent = getIntent();
                            startActivity(intent);
                            finish();
                        })
                        .create()
                        .show();
            }
        }, error -> {
            progressBar.setVisibility(View.GONE);
            libraryManager.onError(error, SplashScreen.this);
        });
    }

    // Process Splash Screen
    private void processSplash() {
        sharedPreferencesManager.saveVersion(version);

        intent = new Intent(SplashScreen.this, LoginActivity.class);
        startActivity(intent);
        finish();

        progressBar.setVisibility(View.GONE);
    }

    // Setup Progress Dialog
    private void setupProgressBar() {
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorBackground), PorterDuff.Mode.SRC_IN);
    }

}
