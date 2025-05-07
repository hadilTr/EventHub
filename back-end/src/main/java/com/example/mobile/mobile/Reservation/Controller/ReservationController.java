package com.example.mobile.mobile.Reservation.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import com.example.mobile.mobile.Representation.DTO.BilletPurchaseRequest;
import com.example.mobile.mobile.Reservation.DTO.ReservationDTO;
import com.example.mobile.mobile.Reservation.Model.Reservation;
import com.example.mobile.mobile.Reservation.Service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    

   
   
    

    // 🔹 Détails d’une réservation
    @GetMapping("/{id}")
    public Optional<Reservation> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }

    
    @PostMapping("/guest")
    public Reservation createReservationForGuest(@RequestParam String email, @RequestParam String fullName,@RequestParam String tel,@RequestBody BilletPurchaseRequest request) {
        return reservationService.createReservationForGuest(email, fullName ,tel, request);
    }
}