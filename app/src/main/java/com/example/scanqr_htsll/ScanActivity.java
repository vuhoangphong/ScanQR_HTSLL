package com.example.scanqr_htsll;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class ScanActivity extends AppCompatActivity  {
    private TextView textView;
    private TextView maHocSinh;
    private Button confirmScan;
    private TextView TenHS;
    private TextView Lop;
    private TextView NgaySinh;
    private TextView GioiTinh;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.confirm_time:
                Toast.makeText(this,"Item 1",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        textView = findViewById(R.id.lblStatusScan);
        maHocSinh = findViewById(R.id.lblMaHocSinh2);
        confirmScan = findViewById(R.id.btnConfirmScan);
        TenHS = findViewById(R.id.lblTenHocSinh2);
        Lop = findViewById(R.id.lblLop2);
        NgaySinh = findViewById(R.id.lblNgaySinh2);
        GioiTinh = findViewById(R.id.lblGioiTinh2);
        confirmScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmScan.setEnabled(false);
            }
        });
    }



    public void ScanQR(View view){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setPrompt("Đưa Camera Vào QR Code Của Học Sinh");
        intentIntegrator.initiateScan();
    }
    public void GetTT(int maHS){
        OkHttpClient client = new OkHttpClient();
        String url = "https://eschool-usteam.tk/api/ScanQrAPI/GetStudents?iD="+maHS;
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    ScanActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(myResponse);
                                TenHS.setText(json.getString("Ten"));
                                Lop.setText(json.getString("Lop"));
                                if(Integer.parseInt(json.getString("GioiTinh"))==1) {
                                    GioiTinh.setText("Nam");
                                }else {
                                    GioiTinh.setText("Nữ");
                                }
                                String[] str = json.getString("NgaySinh").split("T");
                                String str1 = str[0];
                                NgaySinh.setText(str1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null){
            if (intentResult.getContents() == null){
                textView.setText("Đã Hủy");

            }else {
                textView.setText("Đã Quét");
                confirmScan.setEnabled(true);
                GetTT(Integer.parseInt(intentResult.getContents()));
                maHocSinh.setText(intentResult.getContents());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}