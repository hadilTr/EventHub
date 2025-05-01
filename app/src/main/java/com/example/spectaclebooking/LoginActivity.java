package com.example.spectaclebooking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private CardView loginFormCard;
    private MaterialButton buttonLogin;
    private MaterialButton buttonGoogleLogin;
    private MaterialButton buttonFacebookLogin;
    private TextView textRegister;
    private TextView textForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialiser les vues
        loginFormCard = findViewById(R.id.loginFormCard);
        TextInputEditText editTextEmail = findViewById(R.id.editTextEmail);
        TextInputEditText editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonGoogleLogin = findViewById(R.id.buttonGoogleLogin);
        buttonFacebookLogin = findViewById(R.id.buttonFacebookLogin);
        textRegister = findViewById(R.id.textRegister);
        textForgotPassword = findViewById(R.id.textForgotPassword);

        // Animer l'apparition du formulaire
        new Handler().postDelayed(() -> {
            Animation fadeInSlideUp = AnimationUtils.loadAnimation(this, R.anim.fade_in_slide_up);
            loginFormCard.startAnimation(fadeInSlideUp);
            loginFormCard.setVisibility(View.VISIBLE);
        }, 300);

        // Configurer le bouton de connexion
        buttonLogin.setOnClickListener(v -> {
            // Animer le bouton
            Animation buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_animation);
            v.startAnimation(buttonAnimation);

            // Récupérer les valeurs des champs
            String email = editTextEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            // Valider les champs
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            } else {
                // Simuler une connexion avec délai pour montrer l'animation
                new Handler().postDelayed(() -> {
                    // Vérifier les identifiants
                    if (isValidCredentials(email, password)) {
                        // Navigation vers MainMenuActivity
                        startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish(); // Ferme l'activité de login pour empêcher le retour
                    } else {
                        Toast.makeText(this, "Email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                    }
                }, 300);
            }
        });

        // Configurer le bouton de connexion Google
        buttonGoogleLogin.setOnClickListener(v -> {
            Animation buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_animation);
            v.startAnimation(buttonAnimation);
            Toast.makeText(this, "Connexion avec Google (à implémenter)", Toast.LENGTH_SHORT).show();
        });

        // Configurer le bouton de connexion Facebook
        buttonFacebookLogin.setOnClickListener(v -> {
            Animation buttonAnimation = AnimationUtils.loadAnimation(this, R.anim.button_animation);
            v.startAnimation(buttonAnimation);
            Toast.makeText(this, "Connexion avec Facebook (à implémenter)", Toast.LENGTH_SHORT).show();
        });

        // Configurer le lien d'inscription
        textRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Configurer le lien de mot de passe oublié
        textForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Fonctionnalité de récupération de mot de passe (à implémenter)", Toast.LENGTH_SHORT).show();
        });
    }

    // Méthode temporaire de validation - À REMPLACER par votre vraie logique
    private boolean isValidCredentials(String email, String password) {
        // Exemple basique - À adapter avec votre système d'authentification
        return email.equals("client@gmail.com") && password.equals("password123");
    }
}