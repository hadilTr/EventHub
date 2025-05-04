package com.example.spectaclebooking.models;
import lombok.*;

@Setter
@Getter
public class BilletPurchaseRequestWithUser {
    private BilletPurchaseRequest request;

    private String email; // for guest
    private String fullName;

    private String tel;// for guest
    public BilletPurchaseRequestWithUser() {}

    public BilletPurchaseRequestWithUser(BilletPurchaseRequest billetPurchaseRequest,
                                         String email, String fullName,String tel) {
        this.request = billetPurchaseRequest;
        this.email = email;
        this.fullName = fullName;
        this.tel=tel;
    }



    @Override
    public String toString() {
        return "BilletPurchaseRequestWithUser{" +
                "request=" + request +
                "tel="+tel+
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}