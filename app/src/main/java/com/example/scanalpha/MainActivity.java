package com.example.scanalpha;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

// implements onClickListener for the onclick behaviour of button
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView scanBtn,launchBtn,urlBtn;
    ImageView logoImg;
    String fUrl="https://www.youtube.com/watch?v=MfjHYgfaeoY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // referencing and initializing
        // the button and textviews
        scanBtn = findViewById(R.id.tv_scan);
        launchBtn=findViewById(R.id.tv_launch);
        urlBtn=findViewById(R.id.tv_url);
        logoImg=findViewById(R.id.iv_logo);
        launchBtn.setVisibility(View.INVISIBLE);
        urlBtn.setVisibility(View.INVISIBLE);

        // adding listener to the button
        scanBtn.setOnClickListener(this);
        launchBtn.setOnClickListener(this);
        urlBtn.setOnClickListener(this);
        logoImg.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.tv_launch)
            launchSite();
        else if(v.getId()==R.id.tv_scan  || v.getId()==R.id.iv_logo) {
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setPrompt("Scan a barcode or QR Code");
            intentIntegrator.setOrientationLocked(false);
            intentIntegrator.initiateScan();
        }
        else if(v.getId()==R.id.tv_url){
            Toast.makeText(this, "link copied", Toast.LENGTH_SHORT).show();
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", urlBtn.getText());
            clipboard.setPrimaryClip(clip);
        }


    }

    private void launchSite() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(fUrl));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                String url = intentResult.getContents();
                fUrl=url;
                urlBtn.setText(fUrl);
                launchBtn.setVisibility(View.VISIBLE);
                urlBtn.setVisibility(View.VISIBLE);
                urlBtn.setTextSize(14);
                scanBtn.setText("Scan more");
                logoImg.setVisibility(View.INVISIBLE);


            }
        } else {
            Toast.makeText(getBaseContext(), "nothing", Toast.LENGTH_SHORT).show();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}