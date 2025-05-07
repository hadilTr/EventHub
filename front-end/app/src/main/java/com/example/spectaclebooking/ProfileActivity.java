package com.example.spectaclebooking;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends AppCompatActivity {
    private ImageView profileImage;
    private TextView userName, userEmail, userPhone, userAddress;
    private MaterialButton editProfileButton, changePhotoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialisation des vues
        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userPhone = findViewById(R.id.userPhone);
        userAddress = findViewById(R.id.userAddress);
        editProfileButton = findViewById(R.id.editProfileButton);
        changePhotoButton = findViewById(R.id.changePhotoButton);

      /*  // Charger les données de l'utilisateur
        loadUserData();

        // Gestion des clics
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });

        changePhotoButton.setOnClickListener(v -> {
            // Ouvrir la galerie ou l'appareil photo
            openImagePicker();
        });
    }

    /*private void loadUserData() {
        // Ici vous récupéreriez les données depuis votre base de données ou API
        // Exemple avec des données fictives
        userName.setText("Jean Dupont");
        userEmail.setText("jean.dupont@example.com");
        userPhone.setText("+33 6 12 34 56 78");
        userAddress.setText("123 Rue de Paris, 75001");

        // Pour l'image (utiliser Glide ou Picasso)
        Glide.with(this)
                .load("https://example.com/profile.jpg")
                .placeholder(R.drawable.ic_profile_placeholder)
                .error(R.drawable.ic_profile_placeholder)
                .circleCrop()
                .into(profileImage);
    }

    private void openImagePicker() {
        // Implémentation pour choisir une image
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            profileImage.setImageURI(imageUri);
            // Ici vous enverriez l'image à votre serveur
        }*/
    }
}
