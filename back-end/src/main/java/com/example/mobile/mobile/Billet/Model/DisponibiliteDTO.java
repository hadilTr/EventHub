package com.example.mobile.mobile.Billet.Model;

import java.math.BigDecimal;

public class DisponibiliteDTO {
    
    private long disponible;
    private BigDecimal prix;

    public DisponibiliteDTO(long disponible, BigDecimal prix) {
        this.disponible = disponible;
        this.prix = prix;
    }

    public long getDisponible() {
        return disponible;
    }

    public void setDisponible(long disponible) {
        this.disponible = disponible;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }
}
