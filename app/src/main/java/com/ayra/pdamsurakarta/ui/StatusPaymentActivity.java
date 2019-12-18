package com.ayra.pdamsurakarta.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ayra.pdamsurakarta.R;
import com.ayra.pdamsurakarta.manager.LibraryManager;
import com.ayra.pdamsurakarta.manager.SharedPreferencesManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusPaymentActivity extends AppCompatActivity {

    // UI
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.struk)
    TextView struk;
    @BindView(R.id.cetak)
    Button btnPrint;
    @BindView(R.id.share)
    Button btnShare;
    @BindView(R.id.txt)
    TextView txt;

    // Object
    Intent intent;
    BluetoothAdapter bluetoothAdapter;
    BluetoothDevice bluetoothDevice;
    OutputStream outputStream;
    InputStream inputStream;
    LibraryManager libraryPackageManager;
    SharedPreferencesManager sharedPrefManager;
    Boolean success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_payment);

        // Init ButterKnife
        ButterKnife.bind(this);

        // Setup Toolbar
        setupToolbar();

        // Init New Object
        initNewObject();

        // Setup View
        setupView();

        // Listener
        listener();

    }

    // Listener
    private void listener() {
        // Button Share
        btnShare.setOnClickListener(view -> {
            intent = new Intent();
            intent.setAction(Intent.ACTION_SEND).putExtra(Intent.EXTRA_TEXT, struk.getText().toString()).setType("text/plain");
            startActivity(intent);
        });

        // Button Print
        btnPrint.setOnClickListener(view -> {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                Toast.makeText(this, getResources().getString(R.string.device_not_support_bluetooth), Toast.LENGTH_SHORT).show();
            } else if (!bluetoothAdapter.isEnabled()) {
                intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 0);
            } else if (sharedPrefManager.getSettingData().equals(null + "&" + null)) {
                libraryPackageManager.alertDialog(this, getResources().getString(R.string.setting_printer), getResources().getString(R.string.click_settings), getResources().getString(R.string.setting), (dialogInterface, i) -> {
                    intent = new Intent(StatusPaymentActivity.this, SettingActivity.class);
                    startActivity(intent);
                });
            } else {
                // Loading show
                libraryPackageManager.loadingShow("Loading...", "Printing", this);
                Thread thread = new Thread(() -> {
                    try {
                        String[] splits = sharedPrefManager.getSettingData().split("\\|");
                        String[] split = splits[1].split("\\r?\\n");
                        bluetoothDevice = bluetoothAdapter.getRemoteDevice(split[1]);
                        connectBluetooth(bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")));
                    } catch (final Exception e) {
                        try {
                            connectBluetooth((BluetoothSocket) Objects.requireNonNull(bluetoothDevice.getClass().getMethod("createRfcommSocket", int.class).invoke(bluetoothDevice, 1)));
                        } catch (final Exception ex) {
                            // Loading dismiss and set success to false
                            libraryPackageManager.loadingDismiss();
                            success = false;
                        }
                    }
                    // Check if print success or not
                    runOnUiThread(() -> {
                        if (success) {
                            libraryPackageManager.successShow(getResources().getString(R.string.success), getResources().getString(R.string.success_print), this);
                        } else {
                            libraryPackageManager.alertDialog(this, getResources().getString(R.string.setting_printer), getResources().getString(R.string.click_settings), getResources().getString(R.string.setting), (dialogInterface, i) -> {
                                intent = new Intent(StatusPaymentActivity.this, SettingActivity.class);
                                startActivity(intent);
                            });
                        }
                    });
                });
                thread.start();
            }
        });
    }

    //  Init New Object
    private void initNewObject() {
        libraryPackageManager = LibraryManager.getInstance();
        sharedPrefManager = SharedPreferencesManager.getInstance(StatusPaymentActivity.this);
    }

    // Setup View
    private void setupView() {
        String typeStatus = getIntent().getStringExtra("cek");
        String strukData = getIntent().getStringExtra("infoStruk");
        if (typeStatus != null) {
            switch (typeStatus) {
                case "laporan":
                    // Change txt title
                    txt.setText("LAPORAN");
                    // Set text and image
                    Picasso.get()
                            .load("http://103.245.181.220/sipoint/img/sipoint_icon/icon_payment/laporan-success.png")
                            .fit()
                            .into(image);
                    struk.setText(strukData);
                    break;
                case "cekstatus":
                    // Change txt title
                    txt.setText("CEK STATUS");
                    // Set text and image
                    Picasso.get()
                            .load("http://103.245.181.220/sipoint/img/sipoint_icon/icon_payment/laporan-success.png")
                            .fit()
                            .into(image);
                    struk.setText(strukData);
                    break;
                case "transaction":
                    txt.setText("PEMBAYARAN");
                    // Set text and image
                    Picasso.get()
                            .load("http://103.245.181.220/sipoint/img/sipoint_icon/icon_payment/payment-success.png")
                            .fit()
                            .into(image);
                    struk.setText(strukData);
                    break;
                default:
                    txt.setText("Failed Show Struk");
                    // Set text and image
                    Picasso.get()
                            .load(R.drawable.logo)
                            .fit()
                            .into(image);
                    struk.setText(strukData);
                    break;
            }
        }
    }

    // Print Struk
    private void connectBluetooth(BluetoothSocket bluetoothSocket) throws IOException, InterruptedException {
        // Connect to address printer
        bluetoothSocket.connect();
        outputStream = bluetoothSocket.getOutputStream();
        inputStream = bluetoothSocket.getInputStream();

        // Get Print Struk Data
        String printStruk = struk.getText().toString();

        // Print
        outputStream.write(printStruk.getBytes(),0, printStruk.getBytes().length);
        Thread.sleep(struk.getText().toString().length() + 4000);

        // Disconnect Bluetooth
        outputStream.close();
        inputStream.close();
        bluetoothSocket.close();
        libraryPackageManager.loadingDismiss();
        success = true;
    }

    // Setup Toolbar
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Struk Transaksi");
        }
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
    }

    // Handler Arrow Back Toolbar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

}
