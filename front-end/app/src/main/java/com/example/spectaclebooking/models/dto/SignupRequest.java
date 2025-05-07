package com.example.spectaclebooking.models.dto;

import lombok.*;

@Data
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SignupRequest {
    private String nomclt;
    private String prenomclt;
    private long tel;  // Changé de int à long
    private String email;
    private String motp;

    public SignupRequest(String nomclt, String prenomclt, long tel, String email, String motp) {
        this.nomclt = nomclt;
        this.prenomclt = prenomclt;
        this.tel = tel;
        this.email = email;
        this.motp = motp;
    }
}