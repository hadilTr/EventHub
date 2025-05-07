package com.example.mobile.mobile.Reservation.Model;

import com.example.mobile.mobile.Billet.Model.Billet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationRequest {
    private Long representationId;
    private Billet.BilletType categorie;
    private Long clientId;  // Remplacer Client par clientId
}
