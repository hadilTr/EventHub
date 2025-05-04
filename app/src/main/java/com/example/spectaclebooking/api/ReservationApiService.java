package com.example.spectaclebooking.api;

// ReservationApiService.java


import com.example.spectaclebooking.models.BilletPurchaseRequest;
import com.example.spectaclebooking.models.Reservation;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservationApiService {

    @POST("api/reservations/guest")
    Call<Reservation> createReservationForGuest(
            @Query("email") String email,
            @Query("fullName") String fullName,
            @Query("tel") String tel,
            @Body BilletPurchaseRequest request
    );



}
