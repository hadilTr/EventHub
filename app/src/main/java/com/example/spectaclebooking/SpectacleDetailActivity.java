package com.example.spectaclebooking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spectaclebooking.RepresentationAdapter;


import com.example.spectaclebooking.MainMenuActivity;
import com.example.spectaclebooking.models.Lieu;
import com.example.spectaclebooking.models.Representation;
import com.example.spectaclebooking.models.RepresentationResponseDTO;

import com.example.spectaclebooking.Spectacle;
import com.example.spectaclebooking.models.SpectacleHomeDTO;
import com.example.spectaclebooking.api.ApiClient;
import com.example.spectaclebooking.api.RepresentationApiService;

import com.example.spectaclebooking.api.ApiService;

import com.example.spectaclebooking.Utils.ImageUtils;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpectacleDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RepresentationAdapter adapter;
    private List<Representation> representationList = new ArrayList<>(); // <-- init vide


    private Long spectacleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spectacle_detail);

        recyclerView = findViewById(R.id.recyclerViewRepresentations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialise l’adaptateur dès le début
        adapter = new RepresentationAdapter(this, representationList);
        recyclerView.setAdapter(adapter);
        adapter.setOnRepresentationSelectedListener(selectedRepresentation -> {
            Button reserverButton = findViewById(R.id.buttonReserve);
            reserverButton.setEnabled(true);
            reserverButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.black));
        });

        Button reserverButton = findViewById(R.id.buttonReserve);
        reserverButton.setEnabled(false); // initially disabled
        reserverButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.accent_color_light)); // add gray color if you want


        spectacleId = getIntent().getLongExtra("spectacle_id", -1);
        fetchSpectacleDetails();
        findViewById(R.id.buttonReserve).setOnClickListener(v -> {
            Representation selectedRepresentation = adapter.getSelectedRepresentation();
            if (selectedRepresentation != null) {
                Intent intent = new Intent(SpectacleDetailActivity.this, BilletActivity.class);
                System.out.println("Representation id: " + selectedRepresentation.getId());
                intent.putExtra("representation_id", selectedRepresentation.getId());
                startActivity(intent);
            } else {
                Toast.makeText(this, "Veuillez sélectionner une représentation.", Toast.LENGTH_SHORT).show();
            }
        });

        if (spectacleId != -1) {
            fetchRepresentations();

        } else {
            Toast.makeText(this, "Erreur: aucun spectacle sélectionné", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchRepresentations() {
        RepresentationApiService apiService = ApiClient.getClient().create(RepresentationApiService.class);
        Call<List<RepresentationResponseDTO>> call = apiService.getBySpectacle(spectacleId);
        Log.d("REP", "URL appelée : " + call.request().url()); // Ajoute ça pour debug

        call.enqueue(new Callback<List<RepresentationResponseDTO>>() {
            @Override
            public void onResponse(Call<List<RepresentationResponseDTO>> call, Response<List<RepresentationResponseDTO>> response) {
                Log.d("REP", "Code réponse HTTP: " + response.code());  // <-- Ajoute ça
                Log.d("REP", "Body: " + response.body());               // <-- Et ça
                if (response.isSuccessful() && response.body() != null) {
                    List<RepresentationResponseDTO> dtoList = response.body();
                    representationList.clear(); // clear old

                    for (RepresentationResponseDTO dto : dtoList) {
                        Representation rep = new Representation();
                        rep.setId(dto.getId());
                        //rep.setDuree(String.valueOf(dto.getDuree()));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            rep.setDate(LocalDate.parse(dto.getDate()));
                            //rep.setHeureDebut(LocalTime.parse(dto.getHeureDebut()));
                        }

                        Lieu lieu = new Lieu();
                        lieu.setNom(dto.getLieuNom());
                        lieu.setAdresse(dto.getLieuAdresse());
                        rep.setLieu(lieu);

                        representationList.add(rep);
                    }

                    adapter.notifyDataSetChanged(); // très important
                } else {
                    Toast.makeText(SpectacleDetailActivity.this, "Aucune représentation trouvée", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RepresentationResponseDTO>> call, Throwable t) {
                Log.e("REP", "Erreur : " + t.getMessage());
                Toast.makeText(SpectacleDetailActivity.this, "Erreur réseau : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void fetchSpectacleDetails() {
        Log.d("SPEC", "Fetching spectacle with ID: " + spectacleId);

        ApiService spectacleApi = ApiClient.getClient().create(ApiService.class);
        Call<SpectacleHomeDTO> call = spectacleApi.getById(spectacleId);
        Log.d("SPEC", "URL appelée: " + call.request().url());

        call.enqueue(new Callback<SpectacleHomeDTO>() {
            @Override
            public void onResponse(Call<SpectacleHomeDTO> call, Response<SpectacleHomeDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    SpectacleHomeDTO spectacle = response.body();

                    Log.d("REP", "Titre reçu : " + spectacle.getTitre());
                    Log.d("REP", "Image base64 reçue (taille) : " + spectacle.getImage().length());

                    TextView titreTextView = findViewById(R.id.Title);
                    ImageView imageView = findViewById(R.id.imageSpectacle);
                    TextView descriptionTextView = findViewById(R.id.textDescription);
                    TextView categoryTextView=findViewById(R.id.textCategory);
                    categoryTextView.setText(spectacle.getTypeSpectacle());
                    TextView dureeTextView=findViewById(R.id.textDuration);
                    dureeTextView.setText(spectacle.getDuree());
                    titreTextView.setText(spectacle.getTitre());
                    descriptionTextView.setText(spectacle.getDescription());

                    Bitmap bitmap = base64ToBitmap(spectacle.getImage());
                    imageView.setImageBitmap(bitmap);
                } else {
                    Log.e("REP", "Réponse non réussie : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SpectacleHomeDTO> call, Throwable t) {
                Log.e("REP", "Erreur réseau spectacle: " + t.getMessage());
            }
        });

    }



    private Bitmap base64ToBitmap(String base64Image) {
        byte[] decodedString = android.util.Base64.decode(base64Image, android.util.Base64.DEFAULT);
        return android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }







}
