package com.armavi_bsd.robotispreconstructed_mega;


import android.annotation.SuppressLint;
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
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.armavi_bsd.robotispreconstructed_mega.urlStorage.URLStorage;
import com.armavi_bsd.robotispreconstructed_mega.util.Intentkey;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Set;
import java.util.UUID;

public class ESCPOSTest extends AppCompatActivity {

    URLStorage urlStorage = new URLStorage();
    SharedPreferences sharedPreferences;
    Pref pref = new Pref();
    Intentkey intentkey = new Intentkey();

    private static final String TAG = "ESC_POS";
    private static final int BLUETOOTH_PERMISSION_REQUEST = 1;

    private ImageView imageView;
    private Button printButton;


    private Bitmap pdfBitmap;
    private File pdfFile;
    String pdfUrl; // Replace with actual URL
    String token;
    private static final String PRINTER_NAME = "Printer"; // Change to your printer's name
    private static final UUID PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_escpostest);

        imageView = findViewById(R.id.imageView);
        printButton = findViewById(R.id.printButton);

        sharedPreferences = getSharedPreferences(pref.getPrefUserCred(), MODE_PRIVATE);
//        PRINTER_MAC_ADDRESS = sharedPreferences.getString(pref.getPrefPrintingDeviceMAC(), "");

        token = getIntent().getExtras().getString(intentkey.getPosAccountIDIntentKey());
//        pdfUrl = "http://192.168.0.100/mega/pdf/pos_print.php?token="+token;
        pdfUrl = urlStorage.getHttpStd()+urlStorage.getBaseUrl()+urlStorage.getPosPrinting()
                +token;

        new DownloadAndRenderPDF().execute(pdfUrl);

        printButton.setOnClickListener(v -> {
            if (pdfBitmap != null) {
                requestBluetoothPermissions();
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    private class DownloadAndRenderPDF extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();

                pdfFile = new File(getCacheDir(), "downloaded.pdf");
                FileOutputStream output = new FileOutputStream(pdfFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                output.close();
                input.close();

                return renderPDF(pdfFile);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                pdfBitmap = result;
                imageView.setImageBitmap(result);
            }
        }
    }

//    private Bitmap renderPDF() {
//        try {
//            ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
//            PdfRenderer pdfRenderer = new PdfRenderer(fileDescriptor);
//            PdfRenderer.Page page = pdfRenderer.openPage(0);
//
////            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
//            Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
//
//            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//            page.close();
//            pdfRenderer.close();
//            return bitmap;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

//    private Bitmap renderPDF() {
//        try {
//            ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY);
//            PdfRenderer pdfRenderer = new PdfRenderer(fileDescriptor);
//            PdfRenderer.Page page = pdfRenderer.openPage(0);
//
//            int targetWidth = 384;
//            int targetHeight = (int) ((float) page.getHeight() / page.getWidth() * targetWidth);
//            Bitmap bitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
//
//            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//            page.close();
//            pdfRenderer.close();
//            return bitmap;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    private Bitmap renderPDF(File file) throws Exception {
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

    private void requestBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // Android 12+ requires runtime permissions
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.BLUETOOTH_CONNECT, android.Manifest.permission.BLUETOOTH_SCAN},
                        BLUETOOTH_PERMISSION_REQUEST);
                return;
            }
        }
        connectAndPrint();
    }

    private void connectAndPrint() {
        BluetoothDevice printer = getPairedPrinter();
        if (printer != null) {
            printBitmap(pdfBitmap, printer);
        } else {
            Toast.makeText(this, "Printer not found!", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private BluetoothDevice getPairedPrinter() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Bluetooth is not enabled", Toast.LENGTH_SHORT).show();
            return null;
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice device : pairedDevices) {
            if (device.getName() != null && device.getName().contains(PRINTER_NAME)) {
                return device;
            }
        }
        return null;
    }

//    @SuppressLint("MissingPermission")
//    private void printBitmap(Bitmap bitmap, BluetoothDevice printer) {
//        new Thread(() -> {
//            try {
//                BluetoothSocket socket = printer.createRfcommSocketToServiceRecord(PRINTER_UUID);
//                socket.connect();
//                OutputStream outputStream = socket.getOutputStream();
//
////                String bal = "Chutmarani\n"+
////                        "Dhur\n"+
////                        "Vala lage na\n";
////                outputStream.write(bal.getBytes("UTF-8"));
//                byte[] imageData = convertBitmapToESC(bitmap);
//                outputStream.write(imageData);
//                outputStream.flush();
//                socket.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }

//    @SuppressLint("MissingPermission")
//    private void printBitmap(Bitmap bitmap, BluetoothDevice printer) {
//        new Thread(() -> {
//            try {
//                if (socket != null && socket.isConnected()) {
//                    socket.close();
//                }
//
//                Thread.sleep(1000); // Small delay before reconnecting
//
//                socket = printer.createRfcommSocketToServiceRecord(PRINTER_UUID);
//                socket.connect();
//                OutputStream outputStream = socket.getOutputStream();
//
//                // Reset printer before each print
//                outputStream.write(new byte[]{0x1B, 0x40}); // ESC @
//
//                byte[] imageData = convertBitmapToESC(bitmap);
//                outputStream.write(imageData);
//                outputStream.flush();
//
//                // Close socket properly
//                socket.close();
//
//                // Release bitmap memory
//                bitmap.recycle();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }

//    @SuppressLint("MissingPermission")
//    private void printBitmap(Bitmap bitmap, BluetoothDevice printer) {
//        new Thread(() -> {
//            BluetoothSocket socket = null;
//            OutputStream outputStream = null;
//            try {
//                // ✅ Ensure the printer is paired
//                if (printer.getBondState() != BluetoothDevice.BOND_BONDED) {
//                    Log.e("PrintError", "Printer is not paired.");
//                    return;
//                }
//
//                // ✅ Use the correct SPP UUID
//                UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//                socket = printer.createRfcommSocketToServiceRecord(SPP_UUID);
//
//                // ✅ Cancel discovery before connection
//                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
//
//                // ✅ Connect to the socket with retry logic
//                boolean connected = false;
//                int attempts = 0;
//                while (!connected && attempts < 3) {
//                    try {
//                        socket.connect();
//                        connected = true;
//                    } catch (IOException e) {
//                        attempts++;
//                        Log.e("PrintError", "Connection attempt " + attempts + " failed: " + e.getMessage());
//                        if (attempts == 3) {
//                            throw e; // Give up after 3 attempts
//                        }
//                        try {
//                            Thread.sleep(1000); // ✅ Small delay before retrying
//                        } catch (InterruptedException ignored) {}
//                    }
//                }
//
//                // ✅ Get OutputStream after successful connection
//                outputStream = socket.getOutputStream();
//
//                // ✅ Reset printer (ESC @ command)
//                outputStream.write(new byte[]{0x1B, 0x40});
//                outputStream.flush();
//
//                // ✅ Convert and send bitmap
//                byte[] imageData = convertBitmapToESC(bitmap);
//                outputStream.write(imageData);
//                outputStream.flush();
//
//                // ✅ Add small delay before closing
//                Thread.sleep(500);
//
//                // ✅ Cut Paper (if supported)
//                outputStream.write(new byte[]{0x1D, 0x56, 0x41, 0x10});
//                outputStream.flush();
//
//            } catch (IOException e) {
//                Log.e("PrintError", "Bluetooth Print Error: " + e.getMessage());
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                Log.e("PrintError", "Thread sleep interrupted: " + e.getMessage());
//            } finally {
//                try {
//                    if (outputStream != null) {
//                        outputStream.close();
//                    }
//                    if (socket != null) {
//                        socket.close();
//                    }
//                } catch (IOException ex) {
//                    Log.e("PrintError", "Error closing socket: " + ex.getMessage());
//                }
//            }
//        }).start();
//    }

    @SuppressLint("MissingPermission")
    private void printBitmap(Bitmap bitmap, BluetoothDevice printer) {
        new Thread(() -> {
            BluetoothSocket socket = null;
            OutputStream outputStream = null;
            try {
                UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
                socket = printer.createRfcommSocketToServiceRecord(SPP_UUID);
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

                socket.connect();
                outputStream = socket.getOutputStream();

                // ✅ Reset Printer (Important)
                outputStream.write(new byte[]{0x1B, 0x40});
                outputStream.flush();
                Thread.sleep(200); // Allow printer to reset properly

                // ✅ Convert Bitmap to ESC/POS
                byte[] imageData = convertBitmapToESC(bitmap);
                outputStream.write(imageData);
                outputStream.flush();
                Thread.sleep(500); // Small delay to ensure full data transmission

                // ✅ Cut Paper (If supported)
                outputStream.write(new byte[]{0x1D, 0x56, 0x41, 0x10});
                outputStream.flush();

            } catch (IOException | InterruptedException e) {
                Log.e("PrintError", "Printing failed: " + e.getMessage());
            } finally {
                try {
                    if (outputStream != null) outputStream.close();
                    if (socket != null) socket.close();
                } catch (IOException ex) {
                    Log.e("PrintError", "Error closing socket: " + ex.getMessage());
                }
            }
        }).start();
    }

//    private byte[] convertBitmapToESC(Bitmap bitmap) {
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        int[] pixels = new int[width * height];
//        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
//
//        byte[] imageData = new byte[(width / 8) * height + 8];
//        int offset = 0;
//
//        imageData[offset++] = 0x1D;
//        imageData[offset++] = 0x76;
//        imageData[offset++] = 0x30;
//        imageData[offset++] = 0x00;
//        imageData[offset++] = (byte) (width / 8);
//        imageData[offset++] = (byte) ((width / 8) >> 8);
//        imageData[offset++] = (byte) height;
//        imageData[offset++] = (byte) (height >> 8);
//
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width / 8; x++) {
//                byte pixelByte = 0;
//                for (int bit = 0; bit < 8; bit++) {
//                    int color = pixels[y * width + (x * 8 + bit)];
//                    int grayscale = (color & 0xFF);
//                    if (grayscale < 128) {
//                        pixelByte |= (1 << (7 - bit));
//                    }
//                }
//                imageData[offset++] = pixelByte;
//            }
//        }
//        return imageData;
//    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == BLUETOOTH_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Bluetooth permission granted.");
                connectAndPrint();
            } else {
                Log.e(TAG, "Bluetooth permission denied.");
                Toast.makeText(this, "Bluetooth permission required for printing", Toast.LENGTH_SHORT).show();
            }
        }
    }
}