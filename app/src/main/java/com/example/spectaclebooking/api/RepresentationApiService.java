package com.example.spectaclebooking.api;

import com.example.spectaclebooking.models.BilletPurchaseRequest;
import com.example.spectaclebooking.models.RepresentationResponseDTO;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RepresentationApiService {

    @GET("api/representations/spectacle/{spectacleId}")
    Call<List<RepresentationResponseDTO>> getBySpectacle(@Path("spectacleId") Long spectacleId);

    @GET("api/representations/{id}/available-billets")
    Call<Map<String, Object>> getAvailableBillets(@Path("id") Long id);
    @GET("api/representations/{id}")
    Call<RepresentationResponseDTO> getRepresentationById(@Path("id") Long id);

    @POST("api/representations/mark-billets-as-sold")
    Call<Void> markBilletsAsSold(@Body BilletPurchaseRequest billetPurchaseRequest);

}