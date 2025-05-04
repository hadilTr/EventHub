package com.example.spectaclebooking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spectaclebooking.MainMenuActivity;
import com.example.spectaclebooking.models.BilletType;
import com.example.spectaclebooking.models.RepresentationResponseDTO;
import com.example.spectaclebooking.api.ApiClient;
import com.example.spectaclebooking.api.RepresentationApiService;
//import com.example.spectaclebooking.Paiement.PaiementActivity;
//import com.example.spectaclebooking.Profile.ProfileActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BilletActivity extends AppCompatActivity {

    private LinearLayout containerBillets;
    private Button btnContinuer;
    private RepresentationApiService apiService;

    private List<BilletType> billetList = new ArrayList<>();
    private Map<String, Integer> selectedQuantities = new HashMap<>();
    private LayoutInflater inflater;

    private long representationId; // 🛠 representation ID from Intent
    private Context context;
    private TextView tvLieu, tvDate;
    private ImageView ivMaps;
    private String lieuMapsUrl; // used for launching Google Maps


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billet);

        containerBillets = findViewById(R.id.containerBillets);
        btnContinuer = findViewById(R.id.btnContinuer);
        inflater = LayoutInflater.from(this);
        View header = findViewById(R.id.headerInclude);
        tvLieu = header.findViewById(R.id.tvLieu);
        tvDate = header.findViewById(R.id.tvDate);
        ivMaps = header.findViewById(R.id.ivMaps);

        apiService = ApiClient.getClient().create(RepresentationApiService.class);

        // 🛠 Get representationId from previous Intent
        representationId = getIntent().getLongExtra("representation_id", -1);
        if (representationId == -1) {
            Toast.makeText(this, "Erreur: représentation non trouvée", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        fetchRepresentationDetails();

        btnContinuer.setOnClickListener(v -> {

                //Intent intent = new Intent(BilletActivity.this, PaiementActivity.class);
                //startActivity(intent);
            Map<String, Integer> selectedBillets = new HashMap<>();
            for (BilletType billet : billetList) {
                int qty = selectedQuantities.getOrDefault(billet.getType(), 0);
                if (qty > 0) {
                    selectedBillets.put(billet.getType(), qty);
                }
            }

            if (selectedBillets.isEmpty()) {
                Toast.makeText(this, "Veuillez sélectionner au moins un billet", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(BilletActivity.this, ReservationActivity.class);
            intent.putExtra("representation_id", representationId);


            // Pass the selected billets as a Bundle
            Bundle bundle = new Bundle();
            for (Map.Entry<String, Integer> entry : selectedBillets.entrySet()) {
                bundle.putInt(entry.getKey(), entry.getValue());
            }
            intent.putExtra("selected_billets", bundle);

            // Construire résumé billets
            StringBuilder billetSummary = new StringBuilder();
            double total = 0;
            for (Map.Entry<String, Integer> entry : selectedBillets.entrySet()) {
                String type = entry.getKey();
                int qty = entry.getValue();
                double price = 0;
                for (BilletType billet : billetList) {
                    if (billet.getType().equals(type)) {
                        price = billet.getPrix();
                        break;
                    }
                }
                billetSummary.append(qty).append(" Billets ").append(type).append(", ");
                total += qty * price;
            }

            if (billetSummary.length() > 2) {
                billetSummary.setLength(billetSummary.length() - 2); // remove last comma
            }

            intent.putExtra("billet_summary", billetSummary.toString());
            intent.putExtra("total_amount", String.format("%.2f", total));
            startActivity(intent);
        });

        fetchBilletsFromBackend();

    }

    private void fetchBilletsFromBackend() {
        apiService.getAvailableBillets(representationId).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> data = response.body();

                    double bronzePrice = getDouble(data, "bronzePrice");
                    int bronzeAvailable = getInt(data, "bronzesAvailable");
                    double silverPrice = getDouble(data, "silverPrice");
                    int silverAvailable = getInt(data, "silversAvailable");
                    double goldPrice = getDouble(data, "goldPrice");
                    int goldAvailable = getInt(data, "goldsAvailable");

                    billetList.clear();
                    billetList.add(new BilletType("Bronze", bronzePrice, bronzeAvailable));
                    billetList.add(new BilletType("Silver", silverPrice, silverAvailable));
                    billetList.add(new BilletType("Gold", goldPrice, goldAvailable));

                    containerBillets.removeAllViews(); // 🧹 clean if any
                    for (BilletType billet : billetList) {
                        addBilletToLayout(billet);
                    }

                    updateTotal();
                } else {
                    Toast.makeText(BilletActivity.this, "Erreur chargement billets", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Toast.makeText(BilletActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRepresentationDetails() {
        apiService.getRepresentationById(representationId).enqueue(new Callback<RepresentationResponseDTO>() {
            @Override
            public void onResponse(Call<RepresentationResponseDTO> call, Response<RepresentationResponseDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RepresentationResponseDTO representation = response.body();
                    tvLieu.setText(representation.getLieuNom());
                    tvDate.setText(representation.getDate()); // format if needed
                    String lieuQuery = representation.getLieuNom() + " " + representation.getLieuAdresse();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + lieuQuery));
                    ivMaps.setOnClickListener(v -> {
                        intent.setPackage("com.google.android.apps.maps");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        } else {
                            Toast.makeText(BilletActivity.this, "Google Maps non disponible", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(BilletActivity.this, "Erreur chargement de la représentation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RepresentationResponseDTO> call, Throwable t) {
                Toast.makeText(BilletActivity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void addBilletToLayout(BilletType billet) {
        View view = inflater.inflate(R.layout.item_billet, containerBillets, false);

        TextView tvType = view.findViewById(R.id.tvType);
        TextView tvPrix = view.findViewById(R.id.tvPrix);
        TextView tvDisponibles = view.findViewById(R.id.tvDisponibles);
        TextView tvQuantite = view.findViewById(R.id.tvQuantite);
        TextView tvWarning = view.findViewById(R.id.tvWarning);
        Button btnPlus = view.findViewById(R.id.btnPlus);
        Button btnMinus = view.findViewById(R.id.btnMinus);

        tvType.setText(billet.getType());
        tvPrix.setText(String.format("%.2f DT", billet.getPrix()));
        tvDisponibles.setText(billet.getDisponibles() + " disponibles");

        selectedQuantities.put(billet.getType(), 0);
        tvQuantite.setText("0");

        btnPlus.setOnClickListener(v -> {
            int current = selectedQuantities.get(billet.getType());
            if (current < billet.getDisponibles()) {
                selectedQuantities.put(billet.getType(), current + 1);
                tvQuantite.setText(String.valueOf(current + 1));
                tvWarning.setVisibility(View.GONE);
                updateTotal();
            } else {
                tvWarning.setText("Quantité maximale atteinte !");
                tvWarning.setVisibility(View.VISIBLE);
            }
        });

        btnMinus.setOnClickListener(v -> {
            int current = selectedQuantities.get(billet.getType());
            if (current > 0) {
                selectedQuantities.put(billet.getType(), current - 1);
                tvQuantite.setText(String.valueOf(current - 1));
                tvWarning.setVisibility(View.GONE);
                updateTotal();
            }
        });

        containerBillets.addView(view);
    }

    private void updateTotal() {
        double total = 0;
        int count = 0;
        for (BilletType billet : billetList) {
            int qty = selectedQuantities.getOrDefault(billet.getType(), 0);
            total += qty * billet.getPrix();
            count += qty;
        }

        if (count == 0) {
            btnContinuer.setEnabled(false);
            btnContinuer.setAlpha(0.5f); // gray out
            btnContinuer.setText("Continuer");
        } else {
            btnContinuer.setEnabled(true);
            btnContinuer.setAlpha(1f);
            btnContinuer.setText("Continuer (" + count + " billets - " + String.format("%.2f DT", total) + ")");
        }
    }

    private double getDouble(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
    }

    private int getInt(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }







}