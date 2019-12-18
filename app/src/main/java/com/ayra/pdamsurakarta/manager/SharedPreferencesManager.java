package com.ayra.pdamsurakarta.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ayra.pdamsurakarta.entity.User;
import com.ayra.pdamsurakarta.entity.Version;
import com.ayra.pdamsurakarta.ui.LoginActivity;

public class SharedPreferencesManager {

    private static final String SHARED_PREF_NAME = "SharedPrefManager";

    private static final String FIRSTRUN = "firstrun";
    private static final String JENISPRINTER = "jenis";
    private static final String PILIHPRINTER = "pilih";
    private static final String RESPONSE = "response";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String INFO = "info";
    private static final String SALDO = "saldo";
    private static final String IDPP = "idpp";
    private static final String NAMAPP = "namapp";
    private static final String IDSETOR = "idsetor";
    private static final String NAMA_PRINTER = "nama_printer";
    private static final String JENIS_PRINTER = "jenis_printer";
    private static final String UNIQUE = "unique";
    private static final String VERSION = " version";
    private static final String LINKAPP = "linkapp";
    private static final String URI = "uri";
    private static final String TOKEN_FCM = "token_fcm";

    @SuppressLint("StaticFieldLeak")
    private static SharedPreferencesManager mInstance;
    @SuppressLint("StaticFieldLeak")
    private static Context mCtx;

    private SharedPreferencesManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPreferencesManager(context);
        }
        return mInstance;
    }

    public void updateSaldo(Integer saldo) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(SALDO, saldo).apply();
    }

    public boolean checkAppFirstRun() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean(FIRSTRUN, true)) {
            sharedPreferences.edit().putBoolean(FIRSTRUN, false).apply();
            return true;
        } else {
            return false;
        }
    }

    public void saveUniqueDevice(String unique) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(UNIQUE, unique);
        editor.apply();
    }

    public String loadUniqueDevice() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(UNIQUE, null);
    }

    public void saveSettingData(String jenisPrinter, String pilihPrinter) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(JENISPRINTER, jenisPrinter);
        editor.putString(PILIHPRINTER, pilihPrinter);
        editor.apply();
    }

    public String getSettingData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String jenis = sharedPreferences.getString(JENISPRINTER, null);
        String pilih = sharedPreferences.getString(PILIHPRINTER, null);
        return jenis + "|" + pilih;
    }

    public void saveUserPass(String username, String password) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    public String loadUserPass() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(USERNAME, null);
        String password = sharedPreferences.getString(PASSWORD, null);
        return username + "|" + password;
    }

    public void saveDataUser(User user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(RESPONSE, user.getResponse());
        editor.putString(IDPP, user.getIdpp());
        editor.putString(NAMAPP, user.getNamapp());
        editor.putString(IDSETOR, user.getIdsetor());
        editor.putInt(SALDO, user.getSaldo());
        editor.putString(NAMA_PRINTER, user.getNamaPrinter());
        editor.putString(JENIS_PRINTER, user.getJenisPrinter());
        editor.apply();
    }

    public User loadDataUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(RESPONSE, null),
                sharedPreferences.getString(IDPP, null),
                sharedPreferences.getString(NAMAPP, null),
                sharedPreferences.getString(IDSETOR, null),
                sharedPreferences.getInt(SALDO, 0),
                sharedPreferences.getString(NAMA_PRINTER, null),
                sharedPreferences.getString(JENIS_PRINTER, null)
        );
    }

    public void saveVersion(Version version) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(VERSION, version.getVersion());
        editor.putString(LINKAPP, version.getLinkApp());
        editor.putString(INFO, version.getInfo());
        editor.putString(URI, version.getUri());
        editor.apply();
    }

    public Version loadVersion() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new Version(
                sharedPreferences.getString(VERSION, null),
                sharedPreferences.getString(LINKAPP, null),
                sharedPreferences.getString(INFO, null),
                sharedPreferences.getString(URI, null)
        );
    }

    public void logout() {
        SharedPreferences sharedPreferences;
        sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();

        Version version = loadVersion();
        editor.putString(VERSION, version.getVersion());
        editor.putString(LINKAPP, version.getLinkApp());
        editor.putString(INFO, version.getInfo());
        editor.putString(URI, version.getUri());

        editor.putString(TOKEN_FCM, loadTokenFCM());

        String[] loadUserPass = loadUserPass().split("\\|");
        editor.putString(USERNAME, loadUserPass[0]);
        editor.putString(PASSWORD, loadUserPass[1]);

        String[] settingData = getSettingData().split("\\|");
        editor.putString(JENISPRINTER, settingData[0]);
        editor.putString(PILIHPRINTER, settingData[1]);

        editor.putBoolean(FIRSTRUN, false);

        editor.apply();
        Intent intent = new Intent(mCtx, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mCtx.startActivity(intent);
    }

    public void saveTokenFCM(String token_fcm) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_FCM, token_fcm);
        editor.apply();
    }

    public String loadTokenFCM() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TOKEN_FCM, null);
    }

}
