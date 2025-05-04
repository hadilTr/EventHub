package com.example.spectaclebooking;


import static androidx.core.content.ContextCompat.startActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.spectaclebooking.MainMenuActivity;

import com.example.spectaclebooking.models.BilletPurchaseRequest;
import com.example.spectaclebooking.models.BilletPurchaseRequestWithUser;
import com.example.spectaclebooking.models.RepresentationResponseDTO;
import com.example.spectaclebooking.models.Reservation;
import com.example.spectaclebooking.models.SpectacleHomeDTO;
import com.example.spectaclebooking.api.ApiClient;
import com.example.spectaclebooking.api.RepresentationApiService;
import com.example.spectaclebooking.api.ReservationApiService;
import com.example.spectaclebooking.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationActivity extends AppCompatActivity {

    private EditText editCustomer, editEmail, NumCompte, CVV,editPhone,Date;


    private Button payButton;


    private Drawable defaultBackground,errorBackground;

    private boolean isEmailValid, isCardValid, isCVVValid, isDateValid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reservation);

        editCustomer = findViewById(R.id.editCustomer);
        editEmail = findViewById(R.id.editEmail);
        NumCompte = findViewById(R.id.NumCompte);
        CVV = findViewById(R.id.CVV);
        payButton = findViewById(R.id.buttonConfirm);


        defaultBackground = ContextCompat.getDrawable(this, R.drawable.edittext_default);
        errorBackground = ContextCompat.getDrawable(this, R.drawable.edittext_error);

        // Récupération de l’intent
        Intent intent = getIntent();
        long representationId = intent.getLongExtra("representation_id", -1);
        long spectacleId = intent.getLongExtra("spectacle_id", -1);
        String billetSummary = intent.getStringExtra("billet_summary");
        String totalAmount = intent.getStringExtra("total_amount");
        RepresentationApiService representationService=ApiClient.getClient().create(RepresentationApiService.class);





        setupValidations();


        // In PaiementActivity, modify the payButton onClickListener:
        payButton.setOnClickListener(v -> {
            if (allValid()) {
                // Get the selected billets from the intent
                Bundle bundle = getIntent().getBundleExtra("selected_billets");
                Map<String, Integer> selectedBillets = new HashMap<>();
                if (bundle != null) {
                    for (String key : bundle.keySet()) {
                        selectedBillets.put(key, bundle.getInt(key));
                    }
                }

                // Create the basic BilletPurchaseRequest
                BilletPurchaseRequest billetRequest = new BilletPurchaseRequest();
                billetRequest.setRepresentationId(representationId);
                billetRequest.setBillets(selectedBillets);

                // Check if user is logged in

                String email = editEmail.getText().toString().trim();
                String fullName = editCustomer.getText().toString().trim();
                String tel=editPhone.getText().toString().trim();


                // Create enhanced request
                /*BilletPurchaseRequestWithUser request = new BilletPurchaseRequestWithUser(
                        billetRequest,
                        clientId,
                        email,
                        fullName
                );*/

                representationService.markBilletsAsSold(billetRequest).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("PaymentDebug", "Received response from markBilletsAsSold");

                        if (response.isSuccessful()) {
                            Log.d("PaymentDebug", "Payment successful - HTTP " + response.code());
                            showSuccessDialog();
                        } else {
                            // Log the error details
                            Log.e("PaymentDebug", "Payment failed - HTTP " + response.code());

                            try {
                                if (response.errorBody() != null) {
                                    String errorBody = response.errorBody().string();
                                    Log.e("PaymentDebug", "Error response body: " + errorBody);
                                }
                            } catch (IOException e) {
                                Log.e("PaymentDebug", "Error reading error body", e);
                            }

                            // More detailed error message
                            String errorMsg = "Erreur lors du paiement (Code: " + response.code() + ")";
                            Toast.makeText(ReservationActivity.this, errorMsg, Toast.LENGTH_LONG).show();

                            // Log the request that failed
                            Log.e("PaymentDebug", "Failed request details:");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("PaymentDebug", "Network failure", t);

                        String errorMsg = "Échec de la connexion: " + t.getMessage();
                        Toast.makeText(ReservationActivity.this, errorMsg, Toast.LENGTH_LONG).show();

                        // Log the request that failed
                        Log.e("PaymentDebug", "Failed request details:");
                        Log.e("PaymentDebug", "URL: " + call.request().url());
                    }
                });
            } else {
                Toast.makeText(this, "Veuillez corriger les erreurs.", Toast.LENGTH_SHORT).show();
            }
        });

        // Auto-fill for logged-in users


    }



    private void setupValidations() {
        editEmail.addTextChangedListener(new ValidationWatcher(editEmail) {
            @Override
            boolean validate(String text) {
                isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches();
                return isEmailValid;
            }
        });

        NumCompte.addTextChangedListener(new ValidationWatcher(NumCompte) {
            @Override
            boolean validate(String text) {
                String digitsOnly = text.replaceAll("\\s", "");
                isCardValid = digitsOnly.length() == 16;
                return isCardValid;
            }

            @Override
            public void afterTextChanged(Editable s) {
                NumCompte.removeTextChangedListener(this);
                String input = s.toString().replaceAll("\\s", "");
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < input.length(); i++) {
                    formatted.append(input.charAt(i));
                    if ((i + 1) % 4 == 0 && (i + 1) < input.length()) {
                        formatted.append(" ");
                    }
                }
                NumCompte.setText(formatted.toString());
                NumCompte.setSelection(formatted.length());
                NumCompte.addTextChangedListener(this);
                super.afterTextChanged(s);
            }
        });

        CVV.addTextChangedListener(new ValidationWatcher(CVV) {
            @Override
            boolean validate(String text) {
                isCVVValid = text.matches("\\d{3}");
                return isCVVValid;
            }
        });
    }

    private boolean allValid() {
        return isEmailValid && isCardValid && isCVVValid && isDateValid;
    }




    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_success, null);
        builder.setView(view);
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();

        View loader = view.findViewById(R.id.loader);
        View tick = view.findViewById(R.id.tick);
        TextView message = view.findViewById(R.id.successText);
        Button btnOk = view.findViewById(R.id.btnOk);

        loader.setVisibility(View.VISIBLE);
        tick.setVisibility(View.GONE);
        btnOk.setVisibility(View.GONE);
        message.setText("Traitement en cours...");

        // Après 3 secondes, afficher le succès
        loader.postDelayed(() -> {
            loader.setVisibility(View.GONE);
            tick.setVisibility(View.VISIBLE);
            btnOk.setVisibility(View.VISIBLE);
            message.setText("Paiement Réussi");

            // Gestion du clic sur le bouton OK
            btnOk.setOnClickListener(v -> {
                createReservationAndRedirect();
                dialog.dismiss();
            });
        }, 3000);
    }
    private void createReservationAndRedirect() {
        // Récupérer les données nécessaires
        Bundle bundle = getIntent().getBundleExtra("selected_billets");
        Map<String, Integer> selectedBillets = new HashMap<>();
        if (bundle != null) {
            for (String key : bundle.keySet()) {
                selectedBillets.put(key, bundle.getInt(key));
            }
        }

        BilletPurchaseRequest request = new BilletPurchaseRequest();
        request.setRepresentationId(getIntent().getLongExtra("representation_id", -1));
        request.setBillets(selectedBillets);


        ReservationApiService apiService = ApiClient.getClient().create(ReservationApiService.class);


            // Invité
            String email = editEmail.getText().toString().trim();
            String fullName = editCustomer.getText().toString().trim();
            String tel=editPhone.getText().toString().trim();
            Log.d("ReservationDebug", "Tentative création réservation invité - Email: " + email + ", Nom: " + fullName);
            Log.d("ReservationDebug", "Request: " + new Gson().toJson(request));

            apiService.createReservationForGuest(email, fullName,tel, request).enqueue(new Callback<Reservation>() {
                @Override
                public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                    Log.d("ReservationDebug", "Réponse reçue - Code: " + response.code());

                    if (response.isSuccessful()) {
                        Log.d("ReservationDebug", "Réservation invité créée: " + new Gson().toJson(response.body()));
                        redirectToHome();
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "null";
                            Log.e("ReservationDebug", "Erreur création réservation invité - Code: " + response.code()
                                    + "\nErreur: " + errorBody
                                    + "\nHeaders: " + response.headers());
                        } catch (IOException e) {
                            Log.e("ReservationDebug", "Erreur lecture errorBody", e);
                        }
                        redirectToHome();
                    }
                }

                @Override
                public void onFailure(Call<Reservation> call, Throwable t) {
                    Log.e("ReservationDebug", "Échec réseau invité", t);
                    Log.e("ReservationDebug", "URL appelée: " + call.request().url());
                    redirectToHome();
                }
            });

        }







    abstract class ValidationWatcher implements TextWatcher {
        View field;

        ValidationWatcher(View field) {
            this.field = field;
        }

        abstract boolean validate(String text);

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable s) {
            if (validate(s.toString())) {
                field.setBackground(defaultBackground);
            } else {
                field.setBackground(errorBackground);
            }
        }
    }


    private void redirectToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // remplace la stack
        startActivity(intent);
    }

    private void showReservationError() {
        new AlertDialog.Builder(this)
                .setTitle("Erreur")
                .setMessage("La création de la réservation a échoué. Veuillez réessayer.")
                .setPositiveButton("OK", null)
                .show();
    }
    }
