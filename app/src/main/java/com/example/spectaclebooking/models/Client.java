package com.example.spectaclebooking.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Data
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Client {

    private Integer idclt;

    @NonNull
    private String nomclt;

    @NonNull
    private String prenomclt;


    private int tel;

    private String email;

    @NonNull
    private String motp;

    public Client(String nomclt, String prenomclt,  int tel, String email, String motp) {
        this.nomclt = nomclt;
        this.prenomclt = prenomclt;
        this.tel = tel;

        this.email = email;
        this.motp = motp;
    }
}
