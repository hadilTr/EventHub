package com.example.spectaclebooking.api;

/*import com.example.spectaclebooking.models.RepresentationResponseDTO;
import com.example.spectaclebooking.models.SpectacleHomeDTO;*/

import com.example.spectaclebooking.Spectacle;
import com.example.spectaclebooking.models.SpectacleHomeDTO;
import com.example.spectaclebooking.models.dto.SignupRequest;
import com.example.spectaclebooking.models.dto.SignupResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {


    @GET("/api/spectacles")
    Call<List<SpectacleHomeDTO>> getAllSpectacles();

    @GET("/api/spectacles/{id}")
    Call<SpectacleHomeDTO> getById(@Path("id") Long id);

    /*@GET("/api/spectacles/{id}")
    Call<SpectacleHomeDTO> getSpectacleById(@Path("id") Long id);

    @GET("/api/representations/spectacle/{spectacleId}")
    Call<List<RepresentationResponseDTO>> getRepresentationsBySpectacle(@Path("spectacleId") Long spectacleId);
*/}