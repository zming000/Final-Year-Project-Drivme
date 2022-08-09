package com.example.finalyearproject_drivme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    //declare variables
    Animation rotateAnim, topAnim, btmAnim;
    ImageView ivWheel;
    TextView mtvDriv, mtvSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //obtaining the View with specific ID
        ivWheel = findViewById(R.id.wheelImg);
        mtvDriv = findViewById(R.id.tvDriv);
        mtvSlogan = findViewById(R.id.tvSlogan);

        //load animation
        rotateAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        btmAnim = AnimationUtils.loadAnimation(this, R.anim.btm_anim);

        //add animation into the widgets
        AnimationSet setAnim = new AnimationSet(false);//do not share interpolators if false
        setAnim.addAnimation(rotateAnim);
        setAnim.addAnimation(topAnim);
        ivWheel.startAnimation(setAnim);
        mtvDriv.startAnimation(btmAnim);
        mtvSlogan.startAnimation(btmAnim);

        //display a loading screen for 3 second
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, Role.class));
            finish();
        },3000);
    }
}