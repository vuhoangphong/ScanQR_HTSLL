package com.example.scanqr_htsll;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class ScanActivity extends AppCompatActivity  {
    private TextView textView;
    private TextView maHocSinh;
    private Button confirmScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        textView = findViewById(R.id.lblStatusScan);
        maHocSinh = findViewById(R.id.lblMaHocSinh2);
        confirmScan = findViewById(R.id.btnConfirmScan);
        confirmScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmScan.setEnabled(false);
            }
        });
    }
    public void ScanQR(View view){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.initiateScan();
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
                maHocSinh.setText(intentResult.getContents());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}