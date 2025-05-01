package com.example.spectaclebooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.spectaclebooking.api.ApiClient;
import com.example.spectaclebooking.api.ApiService;
import com.example.spectaclebooking.models.SpectacleHomeDTO;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuActivity extends AppCompatActivity {

    private RecyclerView spectaclesRecyclerView;
    private RecyclerView eventsRecyclerView;
    private SpectacleAdapter spectacleAdapter;

    private List<SpectacleHomeDTO> spectacleList;

    private TabLayout tabLayout;
    private ImageButton searchBar;
    private FloatingActionButton fabFilter;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Initialisation des vues
        spectaclesRecyclerView = findViewById(R.id.spectaclesRecyclerView);
        tabLayout = findViewById(R.id.tabLayout);
        searchBar = findViewById(R.id.searchBar);
        fabFilter = findViewById(R.id.fabFilter);
        context=this;

        // Configuration du RecyclerView pour les spectacles
        spectaclesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        spectaclesRecyclerView.setHasFixedSize(true);

        // Configuration du RecyclerView pour les événements

        // Chargement des données
        loadSpectacles();


        // Gestion des clics
        searchBar.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenuActivity.this, SpectacleDetailActivity.class);
            startActivity(intent);
        });

        fabFilter.setOnClickListener(v -> {
            // Afficher un dialogue de filtre
            showFilterDialog();
        });

        // Gestion des onglets
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterSpectaclesByCategory(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Non utilisé
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Non utilisé
            }
        });
    }


        private void loadSpectacles() {

            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<List<SpectacleHomeDTO>> call = apiService.getAllSpectacles();

            call.enqueue(new Callback<List<SpectacleHomeDTO>>() {
                @Override
                public void onResponse(Call<List<SpectacleHomeDTO>> call, Response<List<SpectacleHomeDTO>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        spectacleList = response.body();
                        spectacleAdapter = new SpectacleAdapter(context, spectacleList, spectacle -> {
                            Intent intent = new Intent(context, SpectacleDetailActivity.class); // 👈 Go to representations
                            intent.putExtra("spectacle_id", spectacle.getId());
                            startActivity(intent);
                        });

                        spectaclesRecyclerView.setAdapter(spectacleAdapter);

                    } else {
                        Toast.makeText(MainMenuActivity.this, "Erreur lors du chargement des spectacles", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<SpectacleHomeDTO>> call, Throwable t) {
                    Toast.makeText(MainMenuActivity.this, "Échec de connexion au serveur", Toast.LENGTH_SHORT).show();
                    Log.e("API_ERROR", t.getMessage(), t);
                }
            });
            

        // Création et configuration de l'adaptateur

    }

    private void filterSpectaclesByCategory(int tabPosition) {
        // Filtrer les spectacles en fonction de l'onglet sélectionné
        List<SpectacleHomeDTO> filteredList = new ArrayList<>();

        switch (tabPosition) {
            case 0: // Tous
                filteredList.addAll(spectacleList);
                break;
            case 1: // Concerts
                for (SpectacleHomeDTO spectacle : spectacleList) {
                    if (spectacle.getTypeSpectacle().equals("Concert")) {
                        filteredList.add(spectacle);
                    }
                }
                break;
            case 2: // Théâtre
                for (SpectacleHomeDTO spectacle : spectacleList) {
                    if (spectacle.getTypeSpectacle().equals("Théâtre") ||
                            spectacle.getTypeSpectacle().equals("Comédie Musicale")) {
                        filteredList.add(spectacle);
                    }
                }
                break;
            case 3: // Danse
                for (SpectacleHomeDTO spectacle : spectacleList) {
                    if (spectacle.getTypeSpectacle().equals("Ballet") ||
                            spectacle.getTypeSpectacle().equals("Danse")) {
                        filteredList.add(spectacle);
                    }
                }
                break;
            case 4: // Humour
                for (SpectacleHomeDTO spectacle : spectacleList) {
                    if (spectacle.getTypeSpectacle().equals("Humour")) {
                        filteredList.add(spectacle);
                    }
                }
                break;
        }

        // Mettre à jour l'adaptateur avec la liste filtrée


        spectacleAdapter = new SpectacleAdapter(context, filteredList, spectacle -> {
            Intent intent = new Intent(context, SpectacleDetailActivity.class); // 👈 Go to representations
            intent.putExtra("spectacle_id", spectacle.getId());
            startActivity(intent);

        });
        spectaclesRecyclerView.setAdapter(spectacleAdapter);
    }

    private void showFilterDialog() {
        // Cette méthode afficherait un dialogue de filtre
        // Pour simplifier, nous ne l'implémentons pas complètement ici
    }
}