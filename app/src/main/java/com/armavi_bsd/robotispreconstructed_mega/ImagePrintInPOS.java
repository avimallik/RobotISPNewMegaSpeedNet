package com.armavi_bsd.robotispreconstructed_mega;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.armavi_bsd.robotispreconstructed_mega.databinding.ActivityImagePrintInPosBinding;
import com.armavi_bsd.robotispreconstructed_mega.util.Pref;
import com.bumptech.glide.Glide;
import com.dantsu.escposprinter.EscPosPrinter;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection;
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections;
import com.dantsu.escposprinter.textparser.PrinterTextParserImg;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Call;
import okhttp3.Callback;



public class ImagePrintInPOS extends AppCompatActivity {

    ActivityImagePrintInPosBinding binding;
    public static final int PERMISSION_BLUETOOTH = 1;

    private final Locale locale = new Locale("id", "ID");
    private final DateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a", locale);
    private final NumberFormat nf = NumberFormat.getCurrencyInstance(locale);

    SharedPreferences sharedPreferences;
    String PRINTER_ADDRESS;
    Pref pref = new Pref();

    private String pdfUrl = "http://192.168.0.122/mega/pdf/pos_print.php?token=110734";
    private RequestQueue requestQueue;
    private static final String PDF_FILE_NAME = "downloaded_pdf.pdf";

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImagePrintInPosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(pref.getPrefUserCred(), MODE_PRIVATE);
        PRINTER_ADDRESS = sharedPreferences.getString(pref.getPrefPrintingDeviceMAC(), "");

        new Thread(this::downloadPDF).start();

        binding.btnPrintImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPrint(v);
            }
        });
    }

    private void downloadPDF() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(pdfUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                File pdfFile = savePDF(inputStream);
                if (pdfFile != null) {
                    displayPDF(pdfFile);
                }
            } else {
                runOnUiThread(() ->
                        Toast.makeText(ImagePrintInPOS.this, "Failed to fetch PDF", Toast.LENGTH_SHORT).show()
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(() ->
                    Toast.makeText(ImagePrintInPOS.this, "Download Failed", Toast.LENGTH_SHORT).show()
            );
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private File savePDF(InputStream inputStream) {
        File pdfFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), PDF_FILE_NAME);
        try (FileOutputStream outputStream = new FileOutputStream(pdfFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            return pdfFile;
        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(() ->
                    Toast.makeText(ImagePrintInPOS.this, "Failed to save PDF", Toast.LENGTH_SHORT).show()
            );
            return null;
        }
    }

    private void displayPDF(File pdfFile) {
        runOnUiThread(() -> {
            try {
                PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));
                PdfRenderer.Page page = renderer.openPage(0);

                int width = (int) (page.getWidth() * 4.0);  // Scale width (increase DPI)
                int height = (int) (page.getHeight() * 4.0); // Scale height (increase DPI)
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);

//                Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_4444);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

                int newWidth = bitmap.getWidth();  // Set your desired width
                int newHeight = bitmap.getHeight(); // Set your desired height

                float scaleWidth = ((float) newWidth) / bitmap.getWidth();
                float scaleHeight = ((float) newHeight) / bitmap.getHeight();

                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                        bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                Bitmap newBitmap = resizedBitmap.copy(Bitmap.Config.ARGB_8888, true);

                Glide.with(getApplicationContext()).asBitmap().load(newBitmap).into(binding.imageOfPrint);
//                binding.imageOfPrint.setImageBitmap(newBitmap);


                page.close();
                renderer.close();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(ImagePrintInPOS.this, "Failed to display PDF", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void doPrint(View view) {
        try {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH}, ImagePrintInPOS.PERMISSION_BLUETOOTH);
            } else {

                BluetoothConnection connection = BluetoothPrintersConnections.selectFirstPaired();
                if (connection != null) {
                    EscPosPrinter printer = new EscPosPrinter(connection, 203, 48f, 32);

                    Bitmap bitmapImg = ((BitmapDrawable) binding.imageOfPrint.getDrawable()).getBitmap();
                    int width = bitmapImg.getWidth(), height = bitmapImg.getHeight();
                    StringBuilder textToPrint = new StringBuilder();

                    for (int y = 0; y < height; y += 256) {
                        Bitmap bitmap = Bitmap.createBitmap(bitmapImg, 0, y, width, (y + 256 >= height) ? height - y : 256);
                        textToPrint.append("[C]<img>").append(PrinterTextParserImg.bitmapToHexadecimalString(printer, bitmap)).append("</img>\n");
                    }
                    printer.printFormattedText(textToPrint.toString());

                } else {
                    Toast.makeText(this, "No printer was connected!", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            Log.e("APP", "Can't print", e);
        }
    }
}