package com.example.spectaclebooking;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Masquer la barre de statut pour une expérience immersive
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        setContentView(R.layout.activity_main);

        // Initialiser les vues
        ImageView appLogo = findViewById(R.id.appLogo);
        TextView appName = findViewById(R.id.appName);
        TextView appSlogan = findViewById(R.id.appSlogan);
        MaterialButton getStartedButton = findViewById(R.id.getStartedButton);
        MaterialButton loginButton = findViewById(R.id.loginButton);

        // Animer l'apparition des éléments
        appLogo.setAlpha(0f);
        appName.setAlpha(0f);
        appSlogan.setAlpha(0f);
        getStartedButton.setAlpha(0f);
        loginButton.setAlpha(0f);

        // Animation du logo
        appLogo.animate()
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(300)
                .start();

        // Animation du nom de l'application
        appName.animate()
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(600)
                .start();

        // Animation du slogan
        appSlogan.animate()
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(900)
                .start();

        // Animation du bouton principal
        getStartedButton.animate()
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(1200)
                .start();

        // Animation du bouton de connexion
        loginButton.animate()
                .alpha(1f)
                .setDuration(1000)
                .setStartDelay(1500)
                .start();

        // Animation au clic sur le bouton principal
        getStartedButton.setOnClickListener(view -> {
            Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
            view.startAnimation(pulse);

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
            }, 300);
        });

        // Animation au clic sur le bouton de connexion
        loginButton.setOnClickListener(view -> {
            Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);
            view.startAnimation(pulse);

            new Handler().postDelayed(() -> {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
            }, 300);
        });
    }
}
