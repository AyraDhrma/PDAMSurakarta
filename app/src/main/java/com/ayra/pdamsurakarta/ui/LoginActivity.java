package com.ayra.pdamsurakarta.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ayra.pdamsurakarta.R;
import com.ayra.pdamsurakarta.api.MySingleton;
import com.ayra.pdamsurakarta.entity.User;
import com.ayra.pdamsurakarta.entity.Version;
import com.ayra.pdamsurakarta.manager.LibraryManager;
import com.ayra.pdamsurakarta.manager.SharedPreferencesManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    // UI
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.btn_login)
    Button btnLogin;

    // Object
    Gson gson;
    Intent intent;
    Version version;
    TelephonyManager telephonyManager;
    LibraryManager libraryManager;
    SharedPreferencesManager sharedPreferencesManager;
    User user;
    MySingleton mySingleton;
    String unique, uuid, ppid, udata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Bind ButterKnife
        ButterKnife.bind(this);

        // Init Object
        initObject();

        // Check If User Have Login
        checkIfUserHasLogin();

        // Listener
        listener();
    }

    // Listener
    private void listener() {
        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (i) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            hitLoginApi();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hitLoginApi();
            }
        });
    }

    // Hit Login API
    private void hitLoginApi() {
        if (validate()) {
            if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // Show Loading
                libraryManager.loadingShow(getResources().getString(R.string.loading),
                        getResources().getString(R.string.sign_in), LoginActivity.this);
                checkUniqueDevice();

                // Parameter API POST
                uuid = getResources().getString(R.string.tele_android) + sharedPreferencesManager.loadUniqueDevice() + getResources().getString(R.string.indonesia);
                ppid = username.getText().toString() + "|" + password.getText().toString();
                udata = sharedPreferencesManager.loadTokenFCM();

                // Hit API Login
                mySingleton.connectApi(version.getUri() + getResources().getString(R.string.auth_login), uuid, ppid, udata, this::responseResult, error -> {
                    libraryManager.onError(error, LoginActivity.this);
                    libraryManager.loadingDismiss();
                });
            } else {
                checkUniqueDevice();
            }

        }

    }

    // Check Device Permission
    private void checkUniqueDevice() {
        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            getUniqueDevice();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                libraryManager.alertDialog(LoginActivity.this, "Permission Dibutuhkan!", "Klik allow untuk melanjutkan proses login", "Saya Megerti", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
                        dialogInterface.dismiss();
                    }
                });
            } else {
                ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
            }
        }
    }

    // Get Device Permission
    @SuppressLint("HardwareIds")
    private void getUniqueDevice() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                String UNIQUEID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
                sharedPreferencesManager.saveUniqueDevice(UNIQUEID);
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    checkUniqueDevice();
                } else {
                    unique = telephonyManager.getDeviceId();
                    if (unique == null || unique.length() == 0) {
                        unique = Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                    }
                    sharedPreferencesManager.saveUniqueDevice(unique);
                }
            }
        }
    }

    // Response From API
    private void responseResult(JSONObject response) {
        Log.d(libraryManager.TAG_SUCCESS, response.toString());
        user = gson.fromJson(response.toString(), User.class);
        if (user.getResponse().equals("SWT:0000")) {
            sendTokenFCM();
            sharedPreferencesManager.saveDataUser(user);
            sharedPreferencesManager.saveUserPass(username.getText().toString(), password.getText().toString());
            sharedPreferencesManager.updateSaldo(user.getSaldo());
            if (getIntent().getStringExtra("notif") == null) {
                intent = new Intent(LoginActivity.this, MainActivity.class);
                Log.d("TAGG", "show0");
            } else {
                if (getIntent().getStringExtra("notif").equals("login")) {
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                } else {
                    intent = new Intent(LoginActivity.this, MainActivity.class);
                }
            }
            startActivity(intent);
            finish();
        } else {
            libraryManager.errorShow(user.getResponse().replace("KILLME:", ""), LoginActivity.this);
        }
        // Dismiss loading
        libraryManager.loadingDismiss();
    }

    // Get Token
    private void sendTokenFCM() {
        uuid = getResources().getString(R.string.tele_android) + sharedPreferencesManager.loadUniqueDevice() + getResources().getString(R.string.indonesia);
        ppid = username.getText().toString() + "|" + password.getText().toString();
        udata = sharedPreferencesManager.loadTokenFCM();

        // Hit API Register Token
        mySingleton.connectApi(version.getUri() + getResources().getString(R.string.register_token), uuid, ppid, udata, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("FCM TOKEN", "Token : " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                libraryManager.onError(error, LoginActivity.this);
                libraryManager.loadingDismiss();
            }
        });

    }

    // Check If User Have Login
    private void checkIfUserHasLogin() {
        String userHasLogin = sharedPreferencesManager.loadUserPass();
        if (!userHasLogin.equals("null|null")) {
            String[] split = userHasLogin.split("\\|");
            username.setText(split[0]);
            password.requestFocus();
        }
    }

    // Init Object
    private void initObject() {
        mySingleton = MySingleton.getInstance(LoginActivity.this);
        libraryManager = LibraryManager.getInstance();
        sharedPreferencesManager = SharedPreferencesManager.getInstance(LoginActivity.this);
        gson = new Gson();
        version = sharedPreferencesManager.loadVersion();
    }

    // Validation
    private boolean validate() {
        if (username.getText().length() == 0) {
            username.setError("Username tidak boleh kosong");
            return false;
        } else if (password.getText().length() == 0) {
            username.setError(null);
            password.setError("Password tidak boleh kosong");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    // On Request Permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(LoginActivity.this, "Terima kasih, Silahkan coba login kembali", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    getUniqueDevice();
                }

            } else {
                Toast.makeText(LoginActivity.this, "Klik allow jika ingin melanjutkan proses login", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
