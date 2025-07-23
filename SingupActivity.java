package com.example.tugasbesar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SingupActivity extends AppCompatActivity {

    // Komponen input dan tombol
    private EditText usernameField, passwordField;
    private Button btnCreateAccount, btnBackLogin;

    // Firebase Authentication
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        // Inisialisasi Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Hubungkan input dan tombol ke layout
        usernameField = findViewById(R.id.emailField);     // input username (bukan email asli)
        passwordField = findViewById(R.id.passwordField);  // input password
        btnCreateAccount = findViewById(R.id.btnCreateAccount); // tombol buat akun
        btnBackLogin = findViewById(R.id.btnBackLogin);         // tombol kembali ke login

        // Tombol buat akun ditekan
        btnCreateAccount.setOnClickListener(view -> {
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();

            // Firebase hanya menerima email, jadi kita buat email palsu dari username
            String fakeEmail = username + "@appdomain.com";

            // Validasi input tidak boleh kosong
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan Password wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            // Buat akun baru di Firebase
            mAuth.createUserWithEmailAndPassword(fakeEmail, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid(); // ambil UID user yang baru dibuat

                            // Simpan data tambahan (username & role) ke Realtime Database
                            Map<String, Object> dataUser = new HashMap<>();
                            dataUser.put("username", username);
                            dataUser.put("role", "mahasiswa");

                            FirebaseDatabase.getInstance()
                                    .getReference("users")
                                    .child(uid)
                                    .setValue(dataUser)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this, "Akun berhasil dibuat!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, MainActivity.class)); // kembali ke halaman login
                                        finish(); // tutup halaman signup
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Gagal simpan data user", Toast.LENGTH_SHORT).show();
                                    });

                        } else {
                            // Jika gagal membuat akun
                            Toast.makeText(this, "Gagal daftar: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Tombol kembali ke halaman login
        btnBackLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, MainActivity.class));
            finish(); // tutup halaman signup
        });
    }
}
