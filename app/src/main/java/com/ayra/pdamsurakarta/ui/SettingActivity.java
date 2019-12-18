package com.ayra.pdamsurakarta.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ayra.pdamsurakarta.R;
import com.ayra.pdamsurakarta.manager.LibraryManager;
import com.ayra.pdamsurakarta.manager.SharedPreferencesManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.type_printer)
    TextView typePrinter;
    @BindView(R.id.choosePrinter)
    TextView choosePrinter;
    @BindView(R.id.btn_test)
    Button btnTest;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    // Init Object
    LibraryManager libraryManager;
    SharedPreferencesManager sharedPreferencesManager;
    String settingData;
    String[] arraySettingData;
    ArrayList<String> options;
    SpinnerDialog spinner;
    BluetoothAdapter bluetoothAdapter;
    Intent intent;
    ArrayList<String> devices;
    BluetoothDevice bluetoothDevice;
    boolean successPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Init UI ButterKnife
        ButterKnife.bind(this);

        // Setup Toolbar
        setupToolbar();

        // Init Object
        initObject();

        // Get Setting Printer
        getSetting();

        // Listener
        listener();

    }

    // Listener
    private void listener() {
        // TextView TypePrinter Listener
        typePrinter.setOnClickListener(view -> {
            // Handler Type Printer
            typePrinterHandler();
        });

        // TextView TypePrinter Listener
        choosePrinter.setOnClickListener(view -> {
            // Choose Printer Handler
            choosePrinterHandler();
        });

        // Button Test Listener
        btnTest.setOnClickListener(view -> {
            // Button Test Handler
            buttonTestHandler();
        });

        // Button Save Listener
        btnSave.setOnClickListener(view -> {
            // Button Save Listener
            buttonSaveHandler();
        });
    }

    // Button Save Listener
    private void buttonSaveHandler() {
        sharedPreferencesManager.saveSettingData(typePrinter.getText().toString(), choosePrinter.getText().toString());
        libraryManager.successShow(getResources().getString(R.string.success), getResources().getString(R.string.success_save), this);
    }

    // Button Test Handler
    private void buttonTestHandler() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, getResources().getString(R.string.device_not_support_bluetooth), Toast.LENGTH_SHORT).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 1);
            } else {
                libraryManager.loadingShow(getResources().getString(R.string.loading),
                        getResources().getString(R.string.printing), this);
                Thread thread = new Thread(() -> {
                    try {
                        String[] split = choosePrinter.getText().toString().split("\\r?\\n");
                        bluetoothDevice = bluetoothAdapter.getRemoteDevice(split[1]);
                        // Connect to Bluetooth
                        connectBluetooth(bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")));
                    } catch (Exception e) {
                        Log.d(SettingActivity.class.getSimpleName(), "buttonTestHandler: $e");
                        try {
                            connectBluetooth((BluetoothSocket) bluetoothDevice.getClass().getMethod("createRfcommSocket", int.class).invoke(bluetoothDevice, 1));
                        } catch (Exception e1) {
                            Log.d(SettingActivity.class.getSimpleName(), "buttonTestHandler: $e1");
                            libraryManager.loadingDismiss();
                            successPrint = false;
                        }
                    }
                    runOnUiThread(() -> {
                        if (successPrint) {
                            libraryManager.successShow(getResources().getString(R.string.success), getResources().getString(R.string.success_print), this);
                        } else {
                            libraryManager.errorShow(getResources().getString(R.string.failed_print), this);
                        }
                    });
                });
                thread.start();
            }
        }
    }

    // Connect To Bluetooth
    private void connectBluetooth(BluetoothSocket socket) throws IOException, InterruptedException {
        // Connect To Mac Address Printer Bluetooth
        socket.connect();
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();

        // Print
        String dummy = "PPOB TELEANJAR \n" +
                "15544001/TELEPOINT MISTER KOMO \n\n" +

                "STRUK PEMBAYARAN PRODUK DUMMY \n\n" +

                "NOPEL  : 000001000 \n" +
                "NAMA   : Dummy Name \n" +
                "ALAMAT : Alamat dummy No.115 \n\n" +

                "TAGIHAN: Rp.   999.999 \n" +
                "ADMIN  : Rp.     2.000 \n" +
                "TOTAL  : Rp. 1.001.999 \n\n" +

                "Struk ini merupakan \n" +
                "bukti pembayaran yang \n" +
                "sah mohon disimpan\n\n";
        outputStream.write(dummy.getBytes());
        Thread.sleep(dummy.length(), 4000);

        // Disconnect
        outputStream.close();
        inputStream.close();
        socket.close();
        libraryManager.loadingDismiss();
        successPrint = true;
    }

    // Choose Printer Handler
    private void choosePrinterHandler() {
        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                Toast.makeText(this, getResources().getString(R.string.device_not_support_bluetooth), Toast.LENGTH_SHORT).show();
            } else {
                if (!bluetoothAdapter.isEnabled()) {
                    intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, 0);
                } else {
                    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        devices = new ArrayList<>();
                        for (BluetoothDevice device : pairedDevices) {
                            devices.add(device.getName() + "\n" + device.getAddress());
                        }
                        spinner = new SpinnerDialog(this, devices, getResources().getString(R.string.type_printer),
                                R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.dialog_cancel));
                        spinner.bindOnSpinerListener((item, position) -> {
                            choosePrinter.setText(item);
                        });
                        spinner.showSpinerDialog();
                    } else {
                        ArrayList<String> emptyDevice = new ArrayList<>();
                        emptyDevice.add(getResources().getString(R.string.no_device_paired));
                        spinner = new SpinnerDialog(this, emptyDevice, getResources().getString(R.string.type_printer),
                                R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.dialog_cancel));
                        spinner.bindOnSpinerListener((item, position) -> {
                            choosePrinter.setText(getResources().getString(R.string.choose_printer));
                        });
                    }
                }
            }
        } catch (Exception e) {
            Log.d(SettingActivity.class.getSimpleName(), "choosePrinterHandler: $e");
        }
    }

    // Handler Type Printer
    private void typePrinterHandler() {
        options = new ArrayList<>();
        options.add(getResources().getString(R.string.dotmatrix));
        options.add(getResources().getString(R.string.thermal));
        spinner = new SpinnerDialog(this, options, getResources().getString(R.string.choose_printer), R.style.DialogAnimations_SmileWindow, getResources().getString(R.string.dialog_cancel));
        spinner.bindOnSpinerListener((option, position) -> typePrinter.setText(option));
        spinner.showSpinerDialog();
    }

    // Init Object
    private void initObject() {
        sharedPreferencesManager = SharedPreferencesManager.getInstance(this);
        libraryManager = LibraryManager.getInstance();
    }

    // Setup Toolbar
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.setting));
        }
        toolbar.setTitleTextColor(Color.WHITE);
    }

    // Handler Toolbar Back Arrow
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

    // Get Setting Printer
    private void getSetting() {
        String empty = "null";
        settingData = sharedPreferencesManager.getSettingData();
        arraySettingData = settingData.split("\\|");
        typePrinter.setText(arraySettingData[0]);
        choosePrinter.setText(arraySettingData[1]);
        if (typePrinter.getText().toString().equals(empty) || choosePrinter.getText().toString().equals(empty)) {
            typePrinter.setText(getResources().getString(R.string.choose));
            choosePrinter.setText(getResources().getString(R.string.choose));
        }
    }

}
