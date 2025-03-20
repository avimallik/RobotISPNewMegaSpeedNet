package com.armavi_bsd.robotispreconstructed_mega;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.armavi_bsd.robotispreconstructed_mega.databinding.ActivityPrintServiceForExternalFileBinding;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.UUID;

public class PrintServiceForExternalFile extends AppCompatActivity {

    ActivityPrintServiceForExternalFileBinding binding;

    SharedPreferences sharedPreferences;
    Pref pref = new Pref();

    String PRINTER_MAC_ADDRESS; // Change to your printer MAC
    private static final UUID PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private OutputStream outputStream;
    private String pdfUrl = "http://192.168.0.100/mega/pdf/pos_print.php?token=108764";  // Your PDF URL
    private File pdfFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityPrintServiceForExternalFileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(pref.getPrefUserCred(), MODE_PRIVATE);
        PRINTER_MAC_ADDRESS = sharedPreferences.getString(pref.getPrefPrintingDeviceMAC(), "");

        binding.printPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadAndPrintTask().execute(pdfUrl);
            }
        });

    }

    private class DownloadAndPrintTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                File pdfFile = downloadPdf(urls[0]);
                if (pdfFile == null) return null;
                return renderPdfToBitmap(pdfFile);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                new Thread(() -> printImage(bitmap)).start();
            } else {
                Toast.makeText(PrintServiceForExternalFile.this, "Failed to download or render PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File downloadPdf(String pdfUrl) {
        try {
            URL url = new URL(pdfUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return null;
            }

            //Random number generator
            Random random = new Random();
            long randomLong = random.nextLong();
            String fileName = String.valueOf(randomLong);

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            File pdfFile = new File(getCacheDir(), fileName+".pdf");
            FileOutputStream fileOutputStream = new FileOutputStream(pdfFile);

            byte[] buffer = new byte[1024];
            int count;
            while ((count = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, count);
            }

            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();

            return pdfFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap renderPdfToBitmap(File file) throws Exception {
        ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        PdfRenderer renderer = new PdfRenderer(fileDescriptor);
        PdfRenderer.Page page = renderer.openPage(0);

        int targetWidth = 384;
        int targetHeight = (int) ((float) page.getHeight() / page.getWidth() * targetWidth);

        Bitmap bitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);

        page.close();
        renderer.close();
        return bitmap;
    }

    private boolean connectToPrinter() {
        try {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                runOnUiThread(() -> Toast.makeText(this, "Please enable Bluetooth", Toast.LENGTH_SHORT).show());
                return false;
            }

            // **Check Bluetooth Permissions (Android 10+ Support)**
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.BLUETOOTH_CONNECT}, 102);
                    return false;
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (ActivityCompat.checkSelfPermission(this,  android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{ android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
                    return false;
                }
            }

            BluetoothDevice printer = bluetoothAdapter.getRemoteDevice(PRINTER_MAC_ADDRESS);
            bluetoothSocket = printer.createRfcommSocketToServiceRecord(PRINTER_UUID);
            bluetoothSocket.connect();
            outputStream = bluetoothSocket.getOutputStream();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(() -> Toast.makeText(this, "Failed to connect to printer.", Toast.LENGTH_SHORT).show());
            return false;
        }
    }

    private void printImage(Bitmap bitmap) {
        if (!connectToPrinter()) return;

        try {
            resetPrinter(); // Reset before printing
            byte[] imageData = convertBitmapToESC(bitmap);
            outputStream.write(imageData);
            outputStream.flush();

            stopPrinter(); // Stop continuous printing
            resetPrinter(); // Reset after printing

            closePrinterConnection();
            runOnUiThread(() -> Toast.makeText(this, "Print Successful", Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(() -> Toast.makeText(this, "Print Failed", Toast.LENGTH_SHORT).show());
        }
    }

    private void resetPrinter() throws IOException, InterruptedException {
        if (outputStream != null) {
            byte[] resetCommand = new byte[]{0x1B, 0x40}; // ESC @ (Reset Printer)
            outputStream.write(resetCommand);
            outputStream.flush();
            Thread.sleep(500);
        }
    }

    private void stopPrinter() throws IOException {
        if (outputStream != null) {
            byte[] stopCommand = new byte[]{0x1D, 0x56, 0x42, 0x00}; // Stop/Cut command
            outputStream.write(stopCommand);
            outputStream.flush();
        }
    }

    private void closePrinterConnection() {
        try {
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
            }
            bluetoothSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] convertBitmapToESC(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        byte[] imageData = new byte[(width / 8) * height + 8];
        int offset = 0;

        imageData[offset++] = 0x1D;
        imageData[offset++] = 0x76;
        imageData[offset++] = 0x30;
        imageData[offset++] = 0x00;
        imageData[offset++] = (byte) (width / 8);
        imageData[offset++] = (byte) ((width / 8) >> 8);
        imageData[offset++] = (byte) height;
        imageData[offset++] = (byte) (height >> 8);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width / 8; x++) {
                byte pixelByte = 0;
                for (int bit = 0; bit < 8; bit++) {
                    int color = pixels[y * width + (x * 8 + bit)];
                    int grayscale = (color & 0xFF);
                    if (grayscale < 128) {
                        pixelByte |= (1 << (7 - bit));
                    }
                }
                imageData[offset++] = pixelByte;
            }
        }
        return imageData;
    }
}