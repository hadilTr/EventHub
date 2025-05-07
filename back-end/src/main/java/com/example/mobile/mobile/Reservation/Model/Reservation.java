package com.example.mobile.mobile.Reservation.Model;

import java.util.HashMap;

import com.example.mobile.mobile.Representation.Model.Representation;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email; // Email of the user (client or guest)
    private String fullName; // Full name of the user (client or guest)
    private int nombreBillets; // Total number of tickets reserved
    private String tel;
    @ManyToOne
    @JoinColumn(name = "representation_id")
    private Representation representation;

    private HashMap<String, Integer> billets;


}