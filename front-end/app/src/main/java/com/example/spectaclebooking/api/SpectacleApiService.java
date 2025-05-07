/*package com.example.spectaclebooking.api;

import android.content.Context;
import android.widget.Toast;

import com.example.spectaclebooking.Spectacle;
import com.example.spectaclebooking.models.RepresentationResponseDTO;
import com.example.spectaclebooking.models.SpectacleHomeDTO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpectacleApiService {
    private final ApiService apiService;
    private final Context context;

    public SpectacleApiService(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    public interface SpectaclesCallback {
        void onSuccess(List<Spectacle> spectacles);
        void onError(String message);
    }

    public void getAllSpectacles(final SpectaclesCallback callback) {
        apiService.getAllSpectacles().enqueue(new Callback<List<SpectacleHomeDTO>>() {
            @Override
            public void onResponse(Call<List<SpectacleHomeDTO>> call, Response<List<SpectacleHomeDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Spectacle> spectacles = new ArrayList<>();
                    for (SpectacleHomeDTO dto : response.body()) {
                        spectacles.add(dto.toSpectacle());
                    }
                    callback.onSuccess(spectacles);
                } else {
                    callback.onError("Erreur lors de la récupération des spectacles: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<SpectacleHomeDTO>> call, Throwable t) {
                callback.onError("Erreur réseau: " + t.getMessage());
            }
        });
    }

    public void getSpectacleById(Long id, final SpectaclesCallback callback) {
        apiService.getSpectacleById(id).enqueue(new Callback<SpectacleHomeDTO>() {
            @Override
            public void onResponse(Call<SpectacleHomeDTO> call, Response<SpectacleHomeDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Spectacle> spectacles = new ArrayList<>();
                    spectacles.add(response.body().toSpectacle());
                    callback.onSuccess(spectacles);
                } else {
                    callback.onError("Erreur lors de la récupération du spectacle: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<SpectacleHomeDTO> call, Throwable t) {
                callback.onError("Erreur réseau: " + t.getMessage());
            }
        });
    }

    public void getRepresentationsBySpectacle(Long spectacleId, final RepresentationsCallback callback) {
        apiService.getRepresentationsBySpectacle(spectacleId).enqueue(new Callback<List<RepresentationResponseDTO>>() {
            @Override
            public void onResponse(Call<List<RepresentationResponseDTO>> call, Response<List<RepresentationResponseDTO>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Erreur lors de la récupération des représentations: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<RepresentationResponseDTO>> call, Throwable t) {
                callback.onError("Erreur réseau: " + t.getMessage());
            }
        });
    }
}*/