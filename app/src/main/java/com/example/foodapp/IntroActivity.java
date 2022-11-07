package com.example.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class IntroActivity extends AppCompatActivity {
//private ConstraintLayout startBtn;
    Handler handler;
    private ImageView imageViewBlade;
    private TextView textViewDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        View backgroundID =findViewById(R.id.background_id);
        backgroundID.getBackground().setAlpha(120);

        imageViewBlade=(ImageView)findViewById(R.id.imageView_bland);
        textViewDisplay=(TextView)findViewById(R.id.textView_display);
        textViewDisplay.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textViewDisplay.setSelected(true);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        },2500);


//        startBtn=findViewById(R.id.startBtn);
//        startBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(IntroActivity.this,MainActivity.class));
//            }
//        });

    }
}