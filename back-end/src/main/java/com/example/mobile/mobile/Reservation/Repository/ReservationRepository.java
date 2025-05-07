package com.example.mobile.mobile.Reservation.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.example.mobile.mobile.Representation.Model.Representation;
import com.example.mobile.mobile.Reservation.Model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    
    
    List<Reservation> findByRepresentation(Representation representation);

   
}