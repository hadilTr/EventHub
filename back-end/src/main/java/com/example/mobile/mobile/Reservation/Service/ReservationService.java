package com.example.mobile.mobile.Reservation.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.mobile.mobile.Mail.EmailService;
import com.example.mobile.mobile.Representation.DTO.BilletPurchaseRequest;
import com.example.mobile.mobile.Representation.Model.Representation;
import com.example.mobile.mobile.Representation.Repository.RepresentationRepository;
import com.example.mobile.mobile.Reservation.DTO.ReservationDTO;
import com.example.mobile.mobile.Reservation.Model.Reservation;
import com.example.mobile.mobile.Reservation.Repository.ReservationRepository;
import com.example.mobile.mobile.Spectacle.Model.Spectacle;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private RepresentationRepository representationRepository;
    @Autowired
    private EmailService emailService; // Inject EmailService


    

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }


    public List<Reservation> getReservationsByRepresentation(Representation representation) {
        return reservationRepository.findByRepresentation(representation);
    }
    
    public Reservation createReservationForGuest(String email, String fullName,String tel,
                                              BilletPurchaseRequest request) {
        Representation representation = representationRepository.findById(request.getRepresentationId())
                .orElseThrow(() -> new EntityNotFoundException("Representation not found"));

        // Calculate total number of billets
        int totalBillets = request.getBillets().values().stream().mapToInt(Integer::intValue).sum();

        Reservation reservation = new Reservation();
        reservation.setEmail(email);
        reservation.setFullName(fullName);
        reservation.setTel(tel);
        reservation.setRepresentation(representation);
        reservation.setNombreBillets(totalBillets);
        reservation.setBillets(new HashMap<>(request.getBillets()));
        // client remains null for guests
        emailService.sendReservationConfirmation(email, fullName, reservation);

        return reservationRepository.save(reservation);
    }
}