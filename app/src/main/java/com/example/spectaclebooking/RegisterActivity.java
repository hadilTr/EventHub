package com.example.spectaclebooking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.spectaclebooking.models.dto.SignupRequest;
import com.example.spectaclebooking.models.dto.SignupResponse;
import com.example.spectaclebooking.api.ApiService;
import com.example.spectaclebooking.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private RetrofitClient retrofitClient;

    private CardView registerFormCard;
    private TextInputEditText editTextLastName, editTextFirstName, editTextPhone, editTextEmail, editTextPassword, editTextConfirmPassword;
    private TextInputLayout textInputLayoutPassword, textInputLayoutConfirmPassword;
    private CheckBox checkboxTerms;
    private MaterialButton buttonContinue;
    private TextView textTermsLink;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialiser les vues
        initializeViews();

        // Animer l'apparition du formulaire
        animateFormAppearance();

        // Configurer les listeners
        setupListeners();
    }

    private void initializeViews() {
        registerFormCard = findViewById(R.id.registerFormCard);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        textInputLayoutConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);
        checkboxTerms = findViewById(R.id.checkboxTerms);
        buttonContinue = findViewById(R.id.buttonContinue);
        textTermsLink = findViewById(R.id.textTermsLink);
        buttonBack = findViewById(R.id.buttonBack);
    }

    private void animateFormAppearance() {
        new Handler().postDelayed(() -> {
            Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
            registerFormCard.startAnimation(slideUp);
            registerFormCard.setVisibility(View.VISIBLE);
        }, 300);
    }

    private void setupListeners() {
        // Bouton retour
        buttonBack.setOnClickListener(v -> {
            onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // Lien vers les conditions d'utilisation
        textTermsLink.setOnClickListener(v -> showTermsAndConditions());

        // Bouton continuer
        buttonContinue.setOnClickListener(v -> {
            // Animer le bouton
            Animation buttonPulse = AnimationUtils.loadAnimation(this, R.anim.button_pulse);
            v.startAnimation(buttonPulse);

            // Valider et soumettre le formulaire
            validateAndSubmitForm();
        });
    }

    private void showTermsAndConditions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Conditions d'utilisation");
        builder.setMessage("En utilisant l'application FORJA, vous acceptez nos conditions d'utilisation et notre politique de confidentialité. Nous collectons certaines informations pour améliorer votre expérience et vous fournir un service de qualité.\n\n" +
                "Vos données personnelles sont protégées conformément au RGPD et ne seront jamais partagées avec des tiers sans votre consentement explicite.\n\n" +
                "Pour plus d'informations, veuillez consulter notre site web.");

        builder.setPositiveButton("J'accepte", (dialog, which) -> {
            checkboxTerms.setChecked(true);
            dialog.dismiss();
        });

        builder.setNegativeButton("Fermer", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void validateAndSubmitForm() {
        // Récupérer les valeurs des champs
        String lastName = editTextLastName.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        // Réinitialiser les erreurs
        textInputLayoutPassword.setError(null);
        textInputLayoutConfirmPassword.setError(null);

        // Valider les champs
        if (lastName.isEmpty() || firstName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
            return;
        }

        // Valider le mot de passe
        if (password.length() < 6) {
            textInputLayoutPassword.setError("Le mot de passe doit contenir au moins 6 caractères");
            return;
        }

        // Valider la confirmation du mot de passe
        if (!password.equals(confirmPassword)) {
            textInputLayoutConfirmPassword.setError("Les mots de passe ne correspondent pas");
            return;
        }

        // Valider l'acceptation des conditions
        if (!checkboxTerms.isChecked()) {
            Toast.makeText(this, "Veuillez accepter les conditions d'utilisation", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tout est valide, procéder à l'inscription
        registerUser(lastName, firstName, phone, email, password);
    }

    private void registerUser(String lastName, String firstName, String phone, String email, String password) {
        try {
            // Afficher un indicateur de chargement
            showLoading(true);

            long telNumber = Long.parseLong(phone);
            SignupRequest request = new SignupRequest(lastName, firstName, telNumber, email, password);

            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
           Call<SignupResponse> call = apiService.registerClient(request);

            call.enqueue(new Callback<SignupResponse>() {
                @Override
                public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                    // Masquer l'indicateur de chargement
                    showLoading(false);

                    if (response.isSuccessful() && response.body() != null) {
                        String registeredEmail = response.body().getEmail();
                        showRegistrationSuccess(registeredEmail);
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Erreur d'inscription: " + response.message(),
                                Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<SignupResponse> call, Throwable t) {
                    // Masquer l'indicateur de chargement
                    showLoading(false);

                    Toast.makeText(RegisterActivity.this,
                            "Échec de connexion: " + t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
        } catch (NumberFormatException e) {
            // Masquer l'indicateur de chargement
            showLoading(false);

            Toast.makeText(this, "Numéro de téléphone invalide", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            buttonContinue.setText("Chargement...");
            buttonContinue.setEnabled(false);
        } else {
            buttonContinue.setText("Continuer");
            buttonContinue.setEnabled(true);
        }
    }

    private void showRegistrationSuccess(String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Inscription réussie !");
        builder.setMessage("Votre compte a été créé avec succès.\nEmail: " + email + "\n\nVous pouvez maintenant vous connecter à votre compte.");

        builder.setPositiveButton("Se connecter", (dialog, which) -> {
            dialog.dismiss();
            // Retourner à l'écran de connexion
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}